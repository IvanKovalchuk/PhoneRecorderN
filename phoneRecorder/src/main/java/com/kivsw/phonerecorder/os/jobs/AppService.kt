package com.kivsw.phonerecorder.os.jobs

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.kivsw.phonerecorder.model.error_processor.IErrorProcessor
import com.kivsw.phonerecorder.model.persistent_data.IJournal
import com.kivsw.phonerecorder.model.task_executor.tasks.ITaskProvider
import com.kivsw.phonerecorder.os.MyApplication
import com.kivsw.phonerecorder.ui.notification.NotificationShower
import phonerecorder.kivsw.com.phonerecorder.R
import java.util.*
import javax.inject.Inject
import javax.inject.Named


//https://www.spiria.com/en/blog/mobile-development/hiding-foreground-services-notifications-in-android/
/**
 * this service is used just to indicate to the framework that we have some background work
 */

class AppService : Service() {

    @Inject
    lateinit var taskProvider: ITaskProvider

    @Inject
    lateinit var journal: IJournal

    //@Inject
    @field:[Inject Named("foreground")]//@Named("foreground")
    lateinit var serviceNotification: NotificationShower

    @Inject
    lateinit var errorProcessor: IErrorProcessor

    private val activeTasks: MutableMap<String, Int> = HashMap()

    override fun onBind(intent: Intent): IBinder? =null

    override fun onCreate() {
        super.onCreate()
        MyApplication.getComponent().inject(this)
        journal.journalAdd("AppService.onCreate()")
        instance = this
    }

    override fun onDestroy() {
        //disposable.dispose()
        instance = null
        journal.journalAdd("AppService.onDestroy()")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        journal.journalAdd("AppService.onStartCommand()", intent)
        doStartForeground()
        var taskId = intent?.action?:""
        var start = intent?.getBooleanExtra(EXTRA_START,false) ?:false

        if(start)
            doStartTask(taskId)
        else
            doStopTask(taskId)

        return START_STICKY
    }

    private fun doStartTask(taskId:String)
    {
        val task = taskProvider.getTask(taskId)
        if (task != null) {
            if (task.startTask())
                    doAddTaskToList(taskId)
            stopServiceIfNecessary() // stops this service
        }
    }

    private fun doStopTask(taskId:String)
    {
        val task = taskProvider.getTask(taskId)
        task?.let {
            task.stopTask()
            doRemoveTaskFromList(taskId)
            stopServiceIfNecessary() // stops this service
        }
    }

    private fun doAddTaskToList(action: String) {
        val count = activeTasks[action]
        val newCount: Int
        if (count != null)
            newCount = count.toInt() + 1
        else
            newCount = 1

        activeTasks[action] = Integer.valueOf(newCount)
    }

    private fun doRemoveTaskFromList(action: String) {
        val count = activeTasks[action]
        if (count != null) {
            val newCount = count.toInt() - 1
            if (newCount <= 0)
                activeTasks.remove(action)
            else
                activeTasks[action] = Integer.valueOf(newCount)
        }
    }

    private fun doStartForeground() {
        if (mayApplicationHideNotification()) {
            if (startService(Intent(this, ForegroundEnablingService::class.java)) == null)
                journal!!.journalAdd("can't start service ForegroundEnablingService")
        } else {
            startForeground(serviceNotification!!.notificationId, createForegroundNotification())
        }
    }
    internal fun createForegroundNotification(): Notification
    {
        return serviceNotification.createNotification("","",
                R.drawable.ic_notification_small_android,
                BitmapFactory.decodeResource(resources, R.drawable.ic_notification_android))
    }

    private fun stopServiceIfNecessary() {
        if (activeTasks!!.isEmpty()) {
            stopSelf()
            instance=null
            releaseWakeLock()
        }
    }

    companion object {
        private val EXTRA_START = "EXTRA_START"

        internal var instance: AppService? = null

        fun mayApplicationHideNotification(): Boolean {
            val r= Build.VERSION.SDK_INT <= Build.VERSION_CODES.N // ver 7.0
            return r
        }

        private var wl: PowerManager.WakeLock? = null
        private fun acquireWakeLock(context: Context) {
            if (wl == null) {
                val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "phoneRecorder:appService")
                wl!!.acquire()
            }
        }

        private fun releaseWakeLock() {
            if (wl != null) {
                wl!!.release()
                wl = null
            }
        }

        @Synchronized private fun doStartService(context: Context, start: Boolean, action: String) {
            val intent = Intent(context, AppService::class.java)
            intent.action = action
            intent.putExtra(EXTRA_START, start)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(intent)
            else
                context.startService(intent)

                acquireWakeLock(context)
        }


        fun startTask(context: Context, action: String) {
               doStartService(context, true, action)
        }

        fun stopTask(context: Context, action: String) {
            doStartService(context, false, action)
        }
    }
}


