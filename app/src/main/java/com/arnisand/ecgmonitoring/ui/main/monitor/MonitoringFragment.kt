package com.arnisand.ecgmonitoring.ui.main.monitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper.Companion.SOCKET_BROADCAST_ACTION
import com.arnisand.ecgmonitoring.ui.base.BaseFragment
import com.arnisand.egcmonitoring.R
import java.lang.ref.WeakReference
import kotlin.reflect.jvm.jvmName
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.fragment_monitoring.*
import java.util.*


class MonitoringFragment : BaseFragment() {

    private var ecgReceiver: EcgReceiver = EcgReceiver(this)
    private val lineGraphSeries = LineGraphSeries<DataPoint>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_monitoring, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_graph.viewport.isYAxisBoundsManual = true
        view_graph.viewport.setMinY(-2.0)
        view_graph.viewport.setMaxY(2.0)
    }

    override fun onResume() {
        super.onResume()
        context?.let { LocalBroadcastManager.getInstance(it).registerReceiver(ecgReceiver, IntentFilter(SOCKET_BROADCAST_ACTION)) }
    }

    override fun onPause() {
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(ecgReceiver) }
        super.onPause()
    }

    fun newDataEcg(message: String) {
        Log.d(TAG, message)
        message.toDoubleOrNull()?.let {
            lineGraphSeries.appendData(DataPoint(Date().time.toDouble(), it), true, 60)
            view_graph.addSeries(lineGraphSeries)
        }
    }

    fun statusConnected(status: Boolean) {
        context?.let {context ->
            if (status) {
                indicator_connect.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndicatorOnline))
            } else {
                indicator_connect.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndicatorOffline))
            }
        }
    }

    class EcgReceiver(monitoringFragment: MonitoringFragment) : BroadcastReceiver() {

        private val wMonitoringFragment = WeakReference(monitoringFragment)

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf { it.hasExtra(EcgApiSocketHelper.SOCKET_MESSAGE_TYPE) }?.let {
                when (it.getIntExtra(EcgApiSocketHelper.SOCKET_MESSAGE_TYPE, 0)) {
                    EcgApiSocketHelper.SocketType.MESSAGE.index -> {
                        wMonitoringFragment.get()?.newDataEcg(it.getStringExtra(EcgApiSocketHelper.SOCKET_MESSAGE))
                    }

                    EcgApiSocketHelper.SocketType.OPEN.index -> {
                        wMonitoringFragment.get()?.statusConnected(true)
                    }

                    EcgApiSocketHelper.SocketType.CLOSE.index -> {
                        wMonitoringFragment.get()?.statusConnected(false)
                    }
                    else -> {
                    }
                }

            }
        }
    }

    companion object {
        val TAG: String = MonitoringFragment::class.jvmName
    }
}