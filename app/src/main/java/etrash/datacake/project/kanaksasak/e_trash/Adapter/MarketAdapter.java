package etrash.datacake.project.kanaksasak.e_trash.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import etrash.datacake.project.kanaksasak.e_trash.Data.UploadInfo;
import etrash.datacake.project.kanaksasak.e_trash.R;

/**
 * Created by IT on 2/12/2018.
 */

public class MarketAdapter extends RecyclerView.ViewHolder {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    View mView;
    Context mContext;

    public MarketAdapter(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindMarket(final UploadInfo info, int position) {
        final int pos = position;
        ImageView ImageView = mView.findViewById(R.id.img_produk);
        ImageView cart = mView.findViewById(R.id.cart);
        TextView nameTextView = mView.findViewById(R.id.txt_price);
//        TextView categoryTextView = (TextView) mView.findViewById(R.id.categoryTextView);
//        TextView ratingTextView = (TextView) mView.findViewById(R.id.ratingTextView);

        Picasso.with(mContext)
                .load(info.getUrl())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .placeholder(R.drawable.imagedefault)
                .error(R.drawable.imagedefault)
                .centerCrop()
                .into(ImageView);

        nameTextView.setText(info.getPrice());

        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Display Item " + pos + " Harga " + info.getPrice(), Toast.LENGTH_LONG).show();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Add Item to Cart", Toast.LENGTH_LONG).show();
            }
        });

    }
}
