package com.example.fuyg.androidbrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by fuyg on 21/01/2017.
 */

public class ListItemPopupWindow extends PopupWindow {

    // types
    public static final int FAVORITE_ITEM_VIEW = 1;
    public static final int FAVORITE_VIEW = 2;
    public static final int HISTORY_ITEM_VIEW = 3;
    public static final int HISTORY_VIEW = 4;
    public static final int IMAGE_HIT_TEST = 5;
    public static final int ANCHOR_HIT_TEST = 6;
    public static final int FILE_MANAGER_ITEM = 7;


    private LayoutInflater layoutInflater;
    private View popupContentView;
    private Context mContext;
    private int mType;

    public ListItemPopupWindow(Context context, int type, int width, int height) {
        super(context);
        mContext = context;
        mType = type;

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
        switch (mType) {
            case FAVORITE_ITEM_VIEW:
                popupContentView = layoutInflater.inflate(R.layout.favorite_item_popup_window, null);
                break;
            case FAVORITE_VIEW:
//                TODO
                break;
            case HISTORY_ITEM_VIEW:
                popupContentView = layoutInflater.inflate(R.layout.history_item_popup_window, null);
                break;
            case HISTORY_VIEW:
//                TODO
                break;
            case IMAGE_HIT_TEST:
                popupContentView = layoutInflater.inflate(R.layout.list_item_image_hit_test, null);
                break;
            case ANCHOR_HIT_TEST:
                break;
            case FILE_MANAGER_ITEM:
                popupContentView = layoutInflater.inflate(R.layout.list_item_file_manager, null);
                break;
        }

    }

    public View findViewById(int id) {
        return popupContentView.findViewById(id);
    }
}
