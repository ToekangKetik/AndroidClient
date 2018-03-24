package etrash.datacake.project.kanaksasak.e_trash.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import etrash.datacake.project.kanaksasak.e_trash.Config;
import etrash.datacake.project.kanaksasak.e_trash.Data.OrderModel;
import etrash.datacake.project.kanaksasak.e_trash.R;

public class PostFragment extends Fragment {

    private static final int CHOOSING_IMAGE_REQUEST = 1234;
    private StorageReference imageReference;
    private StorageReference fileRef;
    private Button btn_post;
    private EditText alamat_txt, bag_txt;
    private FirebaseDatabase database;
    private DatabaseReference mDataReference;
    private DatabaseReference mDataReference2;
    private ProgressDialog progressDialog;
    private String mUID, hp, nama, alamat, mAllowPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_post, container, false);

        btn_post = rootView.findViewById(R.id.btn_post);
        alamat_txt = rootView.findViewById(R.id.alamat_txt);
        bag_txt = rootView.findViewById(R.id.bag_txt);


        GetPref();
        alamat_txt.setText(alamat);
        alamat_txt.setEnabled(false);

        database = FirebaseDatabase.getInstance();
        mDataReference = database.getReference("pending_order/" + mUID + "");
        mDataReference2 = database.getReference("order/");
        fileRef = null;
        progressDialog = new ProgressDialog(this.getContext());


        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAllowPost.equals("1")) {
                    PushData();
                } else {
                    Toast.makeText(getContext(), "Anda Belum Melengkapi Profile!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Post Abort!!", Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }


    private boolean validateInputFileName(String hp, String nama, String alamat, String bag) {

        if (TextUtils.isEmpty(alamat)) {
            Toast.makeText(this.getContext(), "Enter Address!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(bag)) {
            Toast.makeText(this.getContext(), "Enter Bag Amount!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (hp.equals(null)) {
            Toast.makeText(this.getContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nama.equals(null)) {
            Toast.makeText(this.getContext(), "Enter Your Name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void PushData() {
        boolean validate = validateInputFileName(hp, nama, alamat, bag_txt.getText().toString());
        String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + TimeUnit.MICROSECONDS.toMicros(System.currentTimeMillis()) + "";
        String order = timeStamp;

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String sdate = sdfDate.format(now);
        String stime = sdfTime.format(now);


        if (validate == true) {
            OrderModel obj = new OrderModel(order, hp, nama, alamat, bag_txt.getText().toString(), sdate, stime, "pending");

            String key = mDataReference.push().getKey();
            mDataReference.child(key).setValue(obj);
            mDataReference2.child(key).setValue(obj);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.pager, new MainMenu());
            transaction.commit();

            Toast.makeText(getActivity(), "Sukses!", Toast.LENGTH_LONG).show();
        }

    }

    private void GetPref() {

        SharedPreferences pref = this.getContext().getSharedPreferences(Config.UID, 0);
        mUID = pref.getString("UID", "0");

        SharedPreferences pref2 = this.getContext().getSharedPreferences(Config.NAME, 0);
        nama = pref2.getString("NAME", "0");

        SharedPreferences pref3 = this.getContext().getSharedPreferences(Config.ADDR, 0);
        alamat = pref3.getString("ADDR", "0");

        SharedPreferences pref4 = this.getContext().getSharedPreferences(Config.HP, 0);
        hp = pref4.getString("HP", "0");

        SharedPreferences pref5 = this.getContext().getSharedPreferences(Config.PROFILE, 0);
        mAllowPost = pref5.getString("PROFILE", "0");


    }
}
