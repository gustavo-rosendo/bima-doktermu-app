package com.bima.dokterpribadimu.messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import java.util.Map;


import com.google.firebase.messaging.RemoteMessage;

import com.bima.dokterpribadimu.utils.IntentUtils;

import com.bima.dokterpribadimu.R;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> message_data = remoteMessage.getData();

        String notification_command = message_data.get("command");

        if(notification_command != null) {

            if(notification_command.equals("doctor_assigned")) {
                IntentUtils.startDoctorCallAssignedActivityOnTop(this);
            } else if (notification_command.equals("doctor_call_complete")) {
                IntentUtils.startDoctorCallCompleteActivityOnTop(this);
            }
        }
        else {
            showNotification(remoteMessage.getNotification().getBody());
        }


    }

    private void showNotification(String message) {

//        Intent i = new Intent(this,DoctorCallAssignedActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setAutoCancel(true)
//                .setContentTitle("FCM Test")
//                .setContentText(message)
//                .setSmallIcon(R.drawable.bima_logo_icon)
//                .setContentIntent(pendingIntent);

//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        manager.notify(0,builder.build());
    }


}
