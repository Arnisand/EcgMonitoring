package com.arnisand.ecgmonitoring.ui.main.monitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arnisand.ecgmonitoring.background.SocketService
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper.Companion.SOCKET_BROADCAST_ACTION
import com.arnisand.ecgmonitoring.ui.base.BaseFragment
import com.arnisand.egcmonitoring.R
import java.lang.ref.WeakReference
import kotlin.reflect.jvm.jvmName
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.fragment_monitoring.*
import java.util.*
import javax.inject.Inject


class MonitoringFragment : BaseFragment(), IMonitoringFragment {

    @Inject
    lateinit var presenter: IMonitoringPresenter

    private var ecgReceiver: EcgReceiver = EcgReceiver(this)
    private val lineGraphSeries = LineGraphSeries<DataPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentComponent.inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_monitoring, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_graph.viewport.isYAxisBoundsManual = true
        view_graph.viewport.setMinY(-2.0)
        view_graph.viewport.setMaxY(2.0)

        btn_connect.setOnClickListener {
            it.isEnabled = false
            activity?.startService(Intent(context, SocketService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let { LocalBroadcastManager.getInstance(it).registerReceiver(ecgReceiver, IntentFilter(SOCKET_BROADCAST_ACTION)) }

        //todo for test
        startGenerateDate()
    }

    override fun onPause() {
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(ecgReceiver) }
        super.onPause()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun newDataEcg(message: String) {
        Log.d(TAG, message)
        message.toDoubleOrNull()?.let {
            lineGraphSeries.appendData(DataPoint(Date().time.toDouble(), it), true, 60)
            view_graph.addSeries(lineGraphSeries)
        }
    }

    override fun statusConnected(status: Boolean) {
        context?.let { context ->
            if (status) {
                indicator_connect.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndicatorOnline))
                btn_connect.visibility = View.INVISIBLE
                btn_connect.isEnabled = false
            } else {
                indicator_connect.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndicatorOffline))
                btn_connect.visibility = View.VISIBLE
                btn_connect.isEnabled = true
            }
        }
    }

    class EcgReceiver(monitoringFragment: MonitoringFragment) : BroadcastReceiver() {
        private val wMonitoringFragment = WeakReference(monitoringFragment)

        override fun onReceive(context: Context?, intent: Intent) {
            wMonitoringFragment.get()?.presenter?.handleMessage(intent)
        }
    }

    /** todo for test
    move to service */
    private val mHandler = Handler()
    private var mTimer1: Runnable? = null

    private val testDate = Date().time
    private var mRand = Random()

    private fun startGenerateDate() {
        mTimer1 = object : Runnable {
            override fun run() {
                val generateData = generateData()
                lineGraphSeries.appendData(generateData, true, 60)
                view_graph.addSeries(lineGraphSeries)
                view_graph.onDataChanged(true, true)
                mHandler.postDelayed(this, 300)
            }
        }
        mHandler.postDelayed(mTimer1, 300)
    }

    private fun generateData(): DataPoint {
        val x = (Date().time - testDate).toDouble()
        val f = mRand.nextDouble() * 100 + 0.3
        val y = Math.sin((Date().time - testDate).toDouble() * f + 2) + mRand.nextDouble() * 0.3
        return DataPoint(x, y)
    }

    companion object {
        val TAG: String = MonitoringFragment::class.jvmName
    }
}