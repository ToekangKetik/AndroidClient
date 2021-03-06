package etrash.datacake.project.kanaksasak.e_trash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;
import etrash.datacake.project.kanaksasak.e_trash.Fragment.MainMenu;
import etrash.datacake.project.kanaksasak.e_trash.Fragment.PendingOrderFragment;
import etrash.datacake.project.kanaksasak.e_trash.Fragment.ProfileFragment;
import etrash.datacake.project.kanaksasak.e_trash.Fragment.UserQRFragment;

public class MainActivity extends AppCompatActivity {


    private static final int NUM_PAGES = 5;
    BottomNavigationBar bottomNavigationBar;
    ImageView image;
    Button logout;
    String coin;
    private FrameLayout mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        mPager = findViewById(R.id.pager);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_dashboard, "Dasboard"))
                .addItem(new BottomNavigationItem(R.drawable.ic_chart, "Order"))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile"))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Fragment selectedFragment = null;
                if (position == 0) {
                    selectedFragment = new MainMenu();
                } else if (position == 1) {
                    selectedFragment = new PendingOrderFragment();
                } else if (position == 2) {
                    selectedFragment = new ProfileFragment();
                } else {
                    Toast.makeText(MainActivity.this, "Undefined Menu Position!", Toast.LENGTH_LONG).show();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.pager, selectedFragment);
                transaction.commit();


            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                Fragment selectedFragment = null;
                if (position == 0) {
                    selectedFragment = new MainMenu();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.pager, selectedFragment);
                    transaction.commit();
                } else {
//                        Toast.makeText(MainActivity.this, "Undefined Menu Position!", Toast.LENGTH_LONG).show();
                }


            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, new MainMenu());
        transaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // setting
        } else if (id == R.id.logout) {
            ShowConfirmLogout();
        } else if (id == R.id.qrcode) {
            ShowQrCode();
        } else if (id == R.id.coin) {
            ShowCoin();
        } else {

        }

        return super.onOptionsItemSelected(item);
    }


    private void ShowConfirmLogout() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do you want to Logout ?!")
                .setConfirmText("Yes,Logout.!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog

                                .setTitleText("Logout!")
                                .setContentText("Success")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.LOGIN, 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Intent homeIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(homeIntent);
                            }
                        }, 1000);   //2 seconds
                    }
                })
                .show();
    }

    private void ShowQrCode() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, new UserQRFragment());
        transaction.commit();
    }

    private void ShowCoin() {
        GetCoin();
        Toast.makeText(this, "Saldo " + coin, Toast.LENGTH_LONG).show();

    }

    private void GetCoin() {

        SharedPreferences pref = this.getSharedPreferences(Config.COIN, 0);
        coin = pref.getString("COIN", "0");


    }


}
