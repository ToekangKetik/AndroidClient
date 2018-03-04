package etrash.datacake.project.kanaksasak.e_trash.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import etrash.datacake.project.kanaksasak.e_trash.Adapter.MarketAdapter;
import etrash.datacake.project.kanaksasak.e_trash.Data.UploadInfo;
import etrash.datacake.project.kanaksasak.e_trash.R;


public class OrderFragment extends Fragment {

    protected static final Query sMarketQuery =
            FirebaseDatabase.getInstance().getReference("market").limitToLast(10);
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_order, container, false);
        mRecyclerView = rootView.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(adapter.getItemCount());

            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    protected RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<UploadInfo> options =
                new FirebaseRecyclerOptions.Builder<UploadInfo>()
                        .setQuery(sMarketQuery, UploadInfo.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<UploadInfo, MarketAdapter>(options) {


            @Override
            public MarketAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MarketAdapter(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.market_item_list, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull MarketAdapter holder, int position, @NonNull UploadInfo model) {
                holder.bindMarket(model, position);
            }
        };

    }
}
