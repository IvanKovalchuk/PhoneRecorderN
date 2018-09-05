package com.kivsw.phonerecorder.os.jobs;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.kivsw.phonerecorder.model.task_executor.tasks.ITask;
import com.kivsw.phonerecorder.model.task_executor.tasks.ITaskProvider;
import com.kivsw.phonerecorder.os.MyApplication;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * this service is used just to indicate to the framework that we have some background work
 */

public class AppService extends android.app.Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int lastStartId;
    private Map<String, Integer> activeTasks;
    @Inject
    public ITaskProvider taskProvider;
    final static String EXTRA_START="EXTRA_START";

    public void onCreate() {
        super.onCreate();
        activeTasks = new HashMap<>();
        MyApplication.getComponent().inject(this);
    };

    public void onDestroy()
    {
        super.onDestroy();
    };


   /* ITask getTask(String task)
    {
        switch(task)
        {
            case TASK_CALL_RECORDING:
                return MyApplication.getComponent().getCallRecorder();
            case TASK_SEND_FILES:
                return MyApplication.getComponent().getRecordSender();
            case TASK_SMS_READING:
                 return MyApplication.getComponent().getSmsReader();
        };

        return null;
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lastStartId = startId;
        String taskId = intent.getAction();
        boolean start=intent.getBooleanExtra(EXTRA_START,false);


        ITask task=taskProvider.getTask(taskId);
        if(task!=null)
        {
            if(start)
            {
                if(task.startTask())
                  addTask(taskId);
            }
            else
            {
                task.stopTask();
                removeTask(taskId);
            }
        }
        else
            removeTask(taskId);


        return START_NOT_STICKY;
    }

    protected void addTask(String action)
    {
        Integer count= activeTasks.get(action);
        int newCount;
        if(count!=null)
            newCount = count.intValue()+1;
        else
            newCount = 1;

        activeTasks.put(action, Integer.valueOf(newCount) );
    };

    protected void removeTask(String action)
    {
        Integer count= activeTasks.get(action);
        if(count!=null) {
            int newCount = count.intValue()-1;
            if(newCount<=0) activeTasks.remove(action);
            else activeTasks.put(action, Integer.valueOf(newCount) );
        }

        if(activeTasks.isEmpty())
        {
            stopSelfResult(lastStartId);
            releaseWakeLock();
        }
    };

    private static PowerManager.WakeLock wl=null;
    protected static void aquireWakeLock(Context context)
    {
        if(wl==null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "appService");
            wl.acquire();
        }
    }
    protected static void releaseWakeLock()
    {
        if(wl!=null) {
            wl.release();
            wl = null;
        }
    }

    protected static void startService(Context context, String action, boolean start)
    {
        Intent intent=new Intent(context, AppService.class);
        intent.setAction(action);
        intent.putExtra(EXTRA_START, start);
        context.startService(intent);
        if(start)
            aquireWakeLock(context);
    }



    synchronized public static void startTask(Context context, String action)
    {
        startService(context, action, true);
    }
    synchronized public static void stopTask(Context context, String action)
    {
        startService(context, action, false);
    }

}