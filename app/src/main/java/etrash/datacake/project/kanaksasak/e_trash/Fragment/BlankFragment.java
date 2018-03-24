package etrash.datacake.project.kanaksasak.e_trash.Fragment;


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

import etrash.datacake.project.kanaksasak.e_trash.Config;
import etrash.datacake.project.kanaksasak.e_trash.R;

public class BlankFragment extends Fragment {

    private EditText nama, alamat, url;
    private Button set_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_blank, container, false);

        nama = rootView.findViewById(R.id.nametxt);
        alamat = rootView.findViewById(R.id.alamat_txt);
        url = rootView.findViewById(R.id.url_txt);
        set_btn = rootView.findViewById(R.id.setbtn);

        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(nama.getText()) || TextUtils.isEmpty(alamat.getText()) || TextUtils.isEmpty(url.getText())) {
                    Toast.makeText(getActivity(), "Tidak Boleh Kosong!", Toast.LENGTH_LONG).show();
                } else {
                    setPref(nama.getText().toString(), alamat.getText().toString(), url.getText().toString(), "1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.pager, new ProfileFragment());
                    transaction.commit();

                    Toast.makeText(getActivity(), "Sukses!", Toast.LENGTH_LONG).show();
                }


            }
        });

        return rootView;
    }

    private void setPref(String token, String token2, String token3, String token4) {
        SharedPreferences pref = getContext().getSharedPreferences(Config.NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("NAME", token);
        editor.commit();

        SharedPreferences pref2 = getContext().getSharedPreferences(Config.ADDR, 0);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.putString("ADDR", token2);
        editor2.commit();

        SharedPreferences pref3 = getContext().getSharedPreferences(Config.URL, 0);
        SharedPreferences.Editor editor3 = pref3.edit();
        editor3.putString("URL", token3);
        editor3.commit();

        SharedPreferences pref4 = getContext().getSharedPreferences(Config.PROFILE, 0);
        SharedPreferences.Editor editor4 = pref4.edit();
        editor4.putString("PROFILE", token4);
        editor4.commit();

    }

}
