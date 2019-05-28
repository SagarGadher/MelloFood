package com.example.mellofood.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.example.mellofood.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(getString(R.string.FCM_TOKEN), s);
        ed.commit();
        Log.v("FCMLogToken", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}
//fdfS8uQaUxY:APA91bFA84eHwjm0HdYO1Rv3M6H71eJu6W73wyCCdnYOntxrlNx1iSDWAr-EUDa5dHEUjrZ6Vfx3g5u2JLJjTPeequHK7VyZJbKyV8WepQ47o8MLIX3SiF-P4UnTjCvgnkheIFq0qw3_