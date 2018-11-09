package com.kivsw.phonerecorder.os.jobs;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kivsw.phonerecorder.model.settings.ISettings;
import com.kivsw.phonerecorder.os.MyApplication;
import com.kivsw.phonerecorder.ui.notification.AntiTaskKillerNotification;

import javax.inject.Inject;

/**
 * this is en empty foreground service that does nothing
 * it's meant to keep Android task-killer off this app
 */
public class AntiTaskkillerService  extends android.app.Service {
        @Inject AntiTaskKillerNotification antiTaskKillerNotification;
        @Inject ISettings settings;
        private int lastStartId;

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        public void onCreate() {
            super.onCreate();
            MyApplication.getComponent().inject(this);
        };

        public void onDestroy()
        {
            super.onDestroy();
        };


        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            lastStartId = startId;
            String taskId = intent.getAction();
            if(taskId==null) taskId=STOP_SERVICE;

            if(taskId.equals(START_SERVICE))
            {
                startForeground(antiTaskKillerNotification.getNotificationId(), antiTaskKillerNotification.createNotification());
            }
            else
            {
                stopSelf();
            }

            return START_STICKY;

        }


        static private String START_SERVICE="AntiTaskkillerService.START_SERVICE",
                STOP_SERVICE="AntiTaskkillerService.STOP_SERVICE";
        protected static void doStartService(Context context, boolean start)
        {
            Intent intent=new Intent(context, AntiTaskkillerService.class);
            if(start)  intent.setAction(START_SERVICE);
            else    intent.setAction(STOP_SERVICE);
            context.startService(intent);
        }



        synchronized public static void start(Context context)
        {
            doStartService(context, true);
        }
        synchronized public static void stop(Context context)
        {
            doStartService(context, false);
        }


}
