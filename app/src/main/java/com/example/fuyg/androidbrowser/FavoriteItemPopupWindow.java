package com.example.fuyg.androidbrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by fuyg on 21/01/2017.
 */

public class FavoriteItemPopupWindow extends PopupWindow {

    private LayoutInflater layoutInflater;
    private View popupContentView;
    private Context mContext;

    public FavoriteItemPopupWindow(Context context, int width, int height) {
        super(context);
        mContext = context;

        initTab();

        setWidth(width);
        setHeight(height);
        setContentView(popupContentView);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.favorite_popup_background));
    }

    public void initTab() {
        layoutInflater = LayoutInflater.from(mContext);
        popupContentView = layoutInflater.inflate(R.layout.favorite_item_popup_window, null);
    }

    public View findViewById(int id) {
        return popupContentView.findViewById(id);
    }
}
