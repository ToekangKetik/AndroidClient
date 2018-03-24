package etrash.datacake.project.kanaksasak.e_trash.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import etrash.datacake.project.kanaksasak.e_trash.Data.OrderModel;
import etrash.datacake.project.kanaksasak.e_trash.R;

/**
 * Created by IT on 3/18/2018.
 */

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.MyViewHolder> {

    public FragmentManager f_manager;
    private Context mContext;
    private List<OrderModel> mOrderList;

    public PendingOrderAdapter(FragmentManager f_manager, Context mContext, List<OrderModel> mOrderList) {
        this.f_manager = f_manager;
        this.mContext = mContext;
        this.mOrderList = mOrderList;
    }

    @Override
    public PendingOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PendingOrderAdapter.MyViewHolder holder, int position) {
        OrderModel order = mOrderList.get(position);
        String stgl, waktu;
        holder.status.setText(order.getStatus());
        holder.alamat.setText(order.getAlamat());
        stgl = order.getDate().toString();
        waktu = order.getTime().toString();
        holder.tanggal.setText(stgl + " : " + waktu);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView status, tanggal, alamat;

        public MyViewHolder(View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status_txt);
            tanggal = itemView.findViewById(R.id.tgl_txt);
            alamat = itemView.findViewById(R.id.alamat_txt);

        }
    }
}