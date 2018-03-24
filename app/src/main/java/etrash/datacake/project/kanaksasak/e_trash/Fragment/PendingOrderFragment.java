package etrash.datacake.project.kanaksasak.e_trash.Fragment;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import etrash.datacake.project.kanaksasak.e_trash.Adapter.PendingOrderAdapter;
import etrash.datacake.project.kanaksasak.e_trash.Config;
import etrash.datacake.project.kanaksasak.e_trash.Data.OrderModel;
import etrash.datacake.project.kanaksasak.e_trash.R;

import static android.content.ContentValues.TAG;

public class PendingOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private PendingOrderAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<OrderModel> OrderList;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String mUID;
    private SweetAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_pending_order, container, false);

        recyclerView = rootView.findViewById(R.id.orderlist);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference();
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#135589"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);

        OrderList = new ArrayList<>();
        GetPref();


        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        pDialog.show();
        DataChangeListener();

        adapter = new PendingOrderAdapter(getFragmentManager(), getContext(), OrderList);

        recyclerView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void DataChangeListener() {
        mFirebaseDatabase.child("pending_order/" + mUID).addValueEventListener(new ValueEventListener() {

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
        System.out.println("snap -> " + dataSnapshot);
        OrderList.clear();
        for (DataSnapshot data : dataSnapshot.getChildren()) {

//            String alamat = (String) data.child("alamat").getValue();
//            String tgl = (String) data.child("date").getValue();
//            String wkt = (String) data.child("time").getValue();
//            String status = (String) data.child("status").getValue();
//
            //OrderModel obj = new OrderModel("", "", "",alamat,"",tgl,wkt,status);
            OrderModel obj = data.getValue(OrderModel.class);
            OrderList.add(obj);


            System.out.println("snap -> " + obj.getNama());
        }

        adapter.notifyDataSetChanged();
        pDialog.dismiss();


    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void GetPref() {

        SharedPreferences pref = this.getContext().getSharedPreferences(Config.UID, 0);
        mUID = pref.getString("UID", "0");

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
