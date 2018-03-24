package etrash.datacake.project.kanaksasak.e_trash.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import etrash.datacake.project.kanaksasak.e_trash.Config;
import etrash.datacake.project.kanaksasak.e_trash.R;


public class HistoryFragment extends Fragment {

    private static final String CLIENT_KEY = "SB-Mid-client-UQztUNrjlrcueE9C";
    //private static final String BASE_URL = "192.168.88.165:8080/payment";
    private String BASE_URL;
    private String mURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_history, container, false);

        Button btn = rootView.findViewById(R.id.button2);
        BASE_URL = mURL;

        SdkUIFlowBuilder.init()
                .setClientKey(CLIENT_KEY) // client_key is mandatory
                .setContext(getContext()) // context is mandatory
                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                    @Override
                    public void onTransactionFinished(TransactionResult result) {
                        if (result.getResponse() != null) {
                            switch (result.getStatus()) {
                                case TransactionResult.STATUS_SUCCESS:
                                    Toast.makeText(getContext(), "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                    break;
                                case TransactionResult.STATUS_PENDING:
                                    Toast.makeText(getContext(), "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                    break;
                                case TransactionResult.STATUS_FAILED:
                                    Toast.makeText(getContext(), "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                                    break;
                            }
                            result.getResponse().getValidationMessages();
                            Log.d("xresult", "xresponse:" + result.getResponse().getFraudStatus());
                        } else if (result.isTransactionCanceled()) {
                            Toast.makeText(getContext(), "Transaction Canceled", Toast.LENGTH_LONG).show();
                        } else {
                            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                                Toast.makeText(getContext(), "Transaction Invalid : " + result.getStatusMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(BASE_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .buildSDK();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + TimeUnit.MICROSECONDS.toMicros(System.currentTimeMillis()) + "";
                String order = timeStamp;
                TransactionRequest transactionRequest = new TransactionRequest(order, 14000);

                //------------- Item Detail ------------------//
                ItemDetails itemDetails1 = new ItemDetails("11111", 4000, 1, "Pop Mie");
                ItemDetails itemDetails2 = new ItemDetails("22222", 10000, 1, "Teh Botol Sosro");

                // Create array list and add above item details in it and then set it to transaction request.
                ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
                itemDetailsList.add(itemDetails1);
                itemDetailsList.add(itemDetails2);

                // Set item details into the transaction request.
                transactionRequest.setItemDetails(itemDetailsList);

                //------------- Billing Address------------------//
                BillingAddress billingAddress = new BillingAddress("Raynaldi Pratama Putra", "Lalu", "Jln MT Haryono", "Malang", "83115", "081999612020", "ID");


                // Create array list and add above billing details in it and then set it to transaction request.
                ArrayList<BillingAddress> billingAddressList = new ArrayList<>();
                billingAddressList.add(billingAddress);

                // Set billing address list into transaction request
                transactionRequest.setBillingAddressArrayList(billingAddressList);

                ShippingAddress shippingAddress = new ShippingAddress();

                // Create array list and add above shipping details in it and then set it to transaction request.
                ArrayList<ShippingAddress> shippingAddressList = new ArrayList<>();
                shippingAddressList.add(shippingAddress);

                // Set shipping address list into transaction request.
                transactionRequest.setShippingAddressArrayList(shippingAddressList);


//                BillInfoModel billInfoModel = new BillInfoModel("B1LL", "coba bill");
//                // Set the bill info on transaction details
//                transactionRequest.setBillInfoModel(billInfoModel);

//                CreditCard creditCardOptions = new CreditCard();
//                //...
//                // Set bank name when using MIGS channel. for example bank BRI
//                creditCardOptions.setBank(BankType.BCA);
//                // Set MIGS channel (ONLY for BCA, BRI and Maybank Acquiring bank)
//                creditCardOptions.setChannel(CreditCard.MIGS);
//                //...
//
//                transactionRequest.setCreditCard(creditCardOptions);

                MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
                MidtransSDK.getInstance().startPaymentUiFlow(getContext());

            }
        });

        return rootView;
    }

    private void GetPref() {

        SharedPreferences pref = this.getContext().getSharedPreferences(Config.URL, 0);
        mURL = pref.getString("URL", "0");

    }

}
