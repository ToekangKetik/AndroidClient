package etrash.datacake.project.kanaksasak.e_trash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import etrash.datacake.project.kanaksasak.e_trash.R;

public class UserQRFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_user_qr, container, false);

        return rootView;
    }
}
