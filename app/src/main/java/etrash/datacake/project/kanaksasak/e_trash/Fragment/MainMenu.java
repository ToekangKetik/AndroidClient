package etrash.datacake.project.kanaksasak.e_trash.Fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import etrash.datacake.project.kanaksasak.e_trash.Adapter.MenuAdapter;
import etrash.datacake.project.kanaksasak.e_trash.Data.MenuModel;
import etrash.datacake.project.kanaksasak.e_trash.R;

public class MainMenu extends Fragment {
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<MenuModel> MenuList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu, container, false);

        recyclerView = rootView.findViewById(R.id.menulist);
        MenuList = new ArrayList<>();
        adapter = new MenuAdapter(this.getContext(), getFragmentManager(), MenuList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setData();

        return rootView;
    }

    private void setData() {
        int[] icons = new int[]{
                R.drawable.checklist,
                R.drawable.payment,
                R.drawable.wallet
        };

        MenuModel a = new MenuModel("Order", icons[0]);
        MenuList.add(a);

        MenuModel b = new MenuModel("Topup", icons[1]);
        MenuList.add(b);

        MenuModel c = new MenuModel("Payment", icons[2]);
        MenuList.add(c);

    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
