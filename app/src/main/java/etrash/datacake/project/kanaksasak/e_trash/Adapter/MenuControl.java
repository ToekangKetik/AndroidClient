package etrash.datacake.project.kanaksasak.e_trash.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import etrash.datacake.project.kanaksasak.e_trash.Fragment.HistoryFragment;
import etrash.datacake.project.kanaksasak.e_trash.Fragment.PostFragment;
import etrash.datacake.project.kanaksasak.e_trash.R;

/**
 * Created by IT on 3/13/2018.
 */

public class MenuControl {
    public FragmentManager f_manager;
    private Context mContext;

    public MenuControl() {
    }

    public MenuControl(Context mContext, FragmentManager f_manager) {
        this.mContext = mContext;
        this.f_manager = f_manager;
    }

    public void MenuEvent(int position) {
        FragmentTransaction transaction = f_manager.beginTransaction();
        switch (position) {
            case 0:

                transaction.replace(R.id.pager, new PostFragment());
                transaction.commit();
                break;
            case 1:
                transaction.replace(R.id.pager, new HistoryFragment());
                transaction.commit();
                break;
            case 2:

                break;

            case 3:

                break;

            case 4:

                break;

            case 5:

                break;

            default:
                Toast.makeText(mContext, "Terjadi Kesalahan..!", Toast.LENGTH_SHORT).show();
                break;


        }
    }


}
