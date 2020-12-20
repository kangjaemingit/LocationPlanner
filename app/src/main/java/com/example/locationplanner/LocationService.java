package com.example.locationplanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class LocationService extends Service {

    NotificationManager Notifi_M;
    ServiceThread thread;

    LocationManager locationManager;
    LocationListener locationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        LocationServiceHandler handler = new LocationServiceHandler();
        thread = new ServiceThread(handler);
        thread.stopForever();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LocationServiceHandler handler = new LocationServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();

    }

    public void start(){
        LocationServiceHandler handler = new LocationServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
    }

    public void stop(){
        LocationServiceHandler handler = new LocationServiceHandler();
        thread = new ServiceThread(handler);
        thread.stopForever();
    }

    public class LocationServiceHandler extends Handler {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(LocationService.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(LocationService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if(Build.VERSION.SDK_INT >= 24) {
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel("my_notification", "n_channel", NotificationManager.IMPORTANCE_MAX);
                notificationChannel.setDescription("description");
                notificationChannel.setName("Channel Name");
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(LocationService.this)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                    .setContentTitle("근접위치알림")
                    .setContentText("근처에 등록하신 메모가 있습니다")
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setOnlyAlertOnce(true)
                    .setChannelId("my_notification")
                    .setColor(Color.parseColor("#ffffff"));

            assert notificationManager != null;
            int m = 888;
            notificationManager.notify(m, notificationBuilder.build());

//            locationListener = new LocationListener(){
//                @Override
//                public void onLocationChanged(@NonNull Location location) {
//                    notificationManager.notify(m, notificationBuilder.build());
//                    //thread.stopForever();
//                }
//            };



        }
        
    }
}
