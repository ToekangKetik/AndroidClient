package etrash.datacake.project.kanaksasak.e_trash.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import etrash.datacake.project.kanaksasak.e_trash.Data.MenuModel;
import etrash.datacake.project.kanaksasak.e_trash.R;

/**
 * Created by IT on 3/12/2018.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context mContext;
    private List<MenuModel> mMenuList;

    public MenuAdapter(Context context, List<MenuModel> menuList) {
        this.mContext = context;
        this.mMenuList = menuList;
    }

    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MenuAdapter.MyViewHolder holder, int position) {
        MenuModel menu = mMenuList.get(position);
        holder.title.setText(menu.getTitle());

        Picasso.with(mContext)
                .load(menu.getIcon())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
