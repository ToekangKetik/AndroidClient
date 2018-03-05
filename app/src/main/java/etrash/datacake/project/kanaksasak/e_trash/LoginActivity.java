package etrash.datacake.project.kanaksasak.e_trash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private com.google.android.gms.common.SignInButton btnsigningoogle;
    private SweetAlertDialog pDialog;
    private Button btnsignin_phone, verif;
    private EditText code;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private IntlPhoneInput phone;
    private String IdLogin = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnsigningoogle = findViewById(R.id.sign_in_google);
        btnsignin_phone = findViewById(R.id.sign_in_phone);
        verif = findViewById(R.id.verif);
        phone = findViewById(R.id.my_phone_input);
        code = findViewById(R.id.code);

        GetIdLogin();
        if (IdLogin.equals("1")) {
            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }

        phone.setDefault();
        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#135589"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        phone.setVisibility(View.VISIBLE);
        btnsignin_phone.setVisibility(View.VISIBLE);
        btnsigningoogle.setVisibility(View.VISIBLE);
        code.setVisibility(View.GONE);
        verif.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (user.getDisplayName() != null) {

                        StoreInPref("1", user.getUid().toString());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();

                        //Add your data to bundle
                        bundle.putString("user", user.getDisplayName().toString());
                        bundle.putString("email", user.getEmail().toString());
                        if (user.getPhotoUrl() != null) {
                            bundle.putString("photo", user.getPhotoUrl().toString());
                        }


                        //Add the bundle to the intent
                        intent.putExtras(bundle);
                        pDialog.dismiss();
                        startActivity(intent);
                        finish();
//                        passTextView.setText("HI " + user.getDisplayName().toString());
//                        emailTextView.setText(user.getEmail().toString());
                    }
                } else {
                    // User is signed out
                    pDialog.dismiss();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }

        };

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                Toast.makeText(LoginActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(LoginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(LoginActivity.this, "InValid Phone Number", Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(LoginActivity.this, "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                phone.setVisibility(View.GONE);
                btnsignin_phone.setVisibility(View.GONE);
                btnsigningoogle.setVisibility(View.GONE);
                code.setVisibility(View.VISIBLE);
                verif.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                // ...
            }
        };

        btnsigningoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                signIn();
            }
        });


        btnsignin_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                String myInternationalNumber = null;
                if (phone.isValid()) {
                    myInternationalNumber = phone.getNumber();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            myInternationalNumber.toString(),
                            60,
                            java.util.concurrent.TimeUnit.SECONDS,
                            LoginActivity.this,
                            mCallbacks);
                } else {
                    Toast.makeText(LoginActivity.this, "Mohon Maaf Untuk Sementara Hanya Melayani Provider Indonesia!!!", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }

            }
        });

        verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code.getText().toString());
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
        });


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                pDialog.dismiss();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }

                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            if (user.getUid() != null) {
                                Log.d(TAG, "signInWithCredential:success");
                                StoreInPref("1", user.getUid().toString());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "Verification Done UID -> " + user.getUid(), Toast.LENGTH_LONG).show();
                                pDialog.dismiss();
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Terjadi Kesalahan Dalah Get UID!!!", Toast.LENGTH_LONG).show();
                                pDialog.dismiss();
                            }
                            // ...
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity.this, "Invalid Verification", Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        }
                    }
                });
    }


    private void StoreInPref(String token, String token2) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.LOGIN, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("LOGIN", token);
        editor.commit();

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.UID, 0);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.putString("UID", token2);
        editor2.commit();

    }

    private void GetIdLogin() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.LOGIN, 0);
        IdLogin = pref.getString("LOGIN", "0");


    }
}
