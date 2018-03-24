package etrash.datacake.project.kanaksasak.e_trash.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import etrash.datacake.project.kanaksasak.e_trash.Config;
import etrash.datacake.project.kanaksasak.e_trash.MainActivity;
import etrash.datacake.project.kanaksasak.e_trash.R;

public class FirebaseService extends Service {

    private static final String TAG = "FIREBASE LOG";
    private String mUID, coin, tmpcoin;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Boolean flag = false;

    public FirebaseService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference();
        GetUID();
        GetCoin();
        SaldoFirebase();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void SaldoFirebase() {
        mFirebaseDatabase.child("profile").orderByKey().equalTo(mUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "error!");
            }
        });
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        int xcoin, xtmpcoin;

        System.out.println("snap ->" + dataSnapshot);

        for (DataSnapshot child : dataSnapshot.getChildren()) {

            tmpcoin = (String) child.child("saldo").getValue();


        }

        xtmpcoin = Integer.parseInt(tmpcoin);
        xcoin = Integer.parseInt(coin);


        if (flag.equals(true)) {
            if (xtmpcoin > xcoin) {
                addNotification("Coin Berhasil di Tambahkan!");
                coin = tmpcoin;
            } else if (xtmpcoin < xcoin) {
                addNotification("Coin Berhasil di Kurangi!");
                coin = tmpcoin;
            }
        }

        flag = true;
        StoreInPref(coin);


    }

    private void addNotification(String param) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_coins)
                        .setContentTitle(param)
                        .setContentText("This is a test notification");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setVibrate(new long[]{1000, 1000});

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    private void GetUID() {

        SharedPreferences pref = this.getSharedPreferences(Config.UID, 0);
        mUID = pref.getString("UID", "0");


    }

    private void GetCoin() {

        SharedPreferences pref = this.getSharedPreferences(Config.COIN, 0);
        coin = pref.getString("COIN", "0");


    }

    private void StoreInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.COIN, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("COIN", token);
        editor.commit();

    }
}
