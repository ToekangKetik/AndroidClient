package etrash.datacake.project.kanaksasak.e_trash.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import etrash.datacake.project.kanaksasak.e_trash.Config;
import etrash.datacake.project.kanaksasak.e_trash.R;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final int CHOOSING_IMAGE_REQUEST = 1234;
    protected String param;
    private CircleImageView image;
    private ImageView edit_btn;
    private String mUID, coin;
    private Uri fileUri;
    private TextView saldotxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        image = rootView.findViewById(R.id.profile_image);
        edit_btn = rootView.findViewById(R.id.edit_btn);
        saldotxt = rootView.findViewById(R.id.saldotxt);


        GetUID();
        GetCoin();

        param = "https://firebasestorage.googleapis.com/v0/b/e-pul-b4984.appspot.com/o/Profile%2FPHuGaTAINvZaxrHRHPd8P4EiyZx2.jpg?alt=media&token=b2d4e050-9636-48db-a57d-8b23bbda5b71";


        Uri uri = Uri.parse(param);
        Picasso.with(this.getContext())
                .load(uri)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosingFile();

            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.pager, new BlankFragment());
                transaction.commit();
            }
        });


        saldotxt.setText(coin);


        return rootView;
    }


    private void showChoosingFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSING_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            Toast.makeText(this.getContext(), "URI : " + fileUri + "", Toast.LENGTH_LONG).show();
        }
    }

    private void GetUID() {

        SharedPreferences pref = this.getContext().getSharedPreferences(Config.UID, 0);
        mUID = pref.getString("UID", "0");


    }

    private void GetCoin() {

        SharedPreferences pref = this.getContext().getSharedPreferences(Config.COIN, 0);
        coin = pref.getString("COIN", "0");


    }

}
