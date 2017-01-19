package com.example.fuyg.androidbrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TabHost;

/**
 * Created by fuyg on 19/01/2017.
 */

public class ToolsPopupWindow extends PopupWindow {

    private TabHost tabHost;
    private LayoutInflater layoutInflater;
    private View toolsTabView;
    private Context mContext;


    public ToolsPopupWindow(Context context, int width, int height) {
        super(context);

        mContext = context;
        layoutInflater = LayoutInflater.from(context);

        initTab();

        setWidth(width);
        setHeight(height);
        setContentView(tabHost);
        setOutsideTouchable(true);
        setFocusable(true);

    }

    private void initTab () {
        toolsTabView = layoutInflater.inflate(R.layout.tools, null);
        tabHost = (TabHost) toolsTabView.findViewById(android.R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("normal").setIndicator("常用").setContent(R.id.tools_normal));
        tabHost.addTab(tabHost.newTabSpec("settings").setIndicator("设置").setContent(R.id.tools_settings));
        tabHost.addTab(tabHost.newTabSpec("tool").setIndicator("工具").setContent(R.id.tools_tool));
    }

    public View getView(int id) {
        return tabHost.findViewById(id);
    }
}
