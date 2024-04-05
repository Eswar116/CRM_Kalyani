package com.crm.crmapp.FireBaseNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crm.crmapp.R;
import com.crm.crmapp.order.util.ConstantVariable;
import com.crm.crmapp.attendence.activity.AttendenceActivity;
import com.crm.crmapp.order.activity.Main2Activity;
import com.crm.crmapp.order.activity.OrderDetailActivity;
import com.crm.crmapp.order.util.PreferencesHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FireMsgService extends FirebaseMessagingService {
    private String title="";
    private String body="";
    private String bean_type="";
    private String bean_id="";
    private String user_id="";
    private String N_id="";
    private PreferencesHelper preferencesHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

         preferencesHelper = new PreferencesHelper(this);
        if (remoteMessage.getData().size() > 0) {
            int count= ConstantVariable.Companion.getNotificationCount();
            count=count+1;
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ConstantVariable.Companion.getNotificationIntentFilter()).putExtra("NotificationCount",count));


            Log.d("", "Message data payload: " + remoteMessage.getData());
            bean_type = remoteMessage.getData().get("bean_type");
            bean_id = remoteMessage.getData().get("bean_id");


            Log.d("Msg", "Message received [" + remoteMessage + "]");
            // Create Notification
            Intent intent = null;

            if (bean_type.equalsIgnoreCase("Expenses")) {
                intent = new Intent(this, Main2Activity.class);
                intent.putExtra(ConstantVariable.Companion.getFirebaseType(), bean_type);
                intent.putExtra(ConstantVariable.Companion.getFirebase_bean_id(), bean_id);

            } else if (bean_type.equalsIgnoreCase("Attendance")) {
                intent = new Intent(this, AttendenceActivity.class);
                intent.putExtra(ConstantVariable.Companion.getFirebaseType(), bean_type);
                intent.putExtra(ConstantVariable.Companion.getFirebase_bean_id(), bean_id);

            } else if (bean_type.equalsIgnoreCase("Order")) {
                intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(ConstantVariable.Companion.getFirebaseType(), bean_type);
                intent.putExtra(ConstantVariable.Companion.getFirebase_bean_id(), bean_id);

            } else if (bean_type.equalsIgnoreCase("Customer")) {
                intent = new Intent(this, Main2Activity.class);
                intent.putExtra(ConstantVariable.Companion.getFirebaseType(), bean_type);
                intent.putExtra(ConstantVariable.Companion.getFirebase_bean_id(), bean_id);

            } else {
                intent = new Intent(this, Main2Activity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                            | PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_icon)
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            int id = createID();
            notificationManager.notify(id, notificationBuilder.build());
        }
    }
    public String token;


    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        preferencesHelper = new PreferencesHelper(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token =instanceIdResult.getToken();
                preferencesHelper.setDeviceToken(token);
                Log.e("FCM TOKEN",token);
            }
        });
    }
}