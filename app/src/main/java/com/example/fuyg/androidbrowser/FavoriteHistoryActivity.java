package com.example.fuyg.androidbrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fuyg on 20/01/2017.
 */

public class FavoriteHistoryActivity extends Activity {


    private TextView favorite;
    private TextView history;
    private ListView favoriteList;
    private ListView historyList;

    private ListItemPopupWindow listItemPopupWindow;

    private FavoriteHistoryManager favoriteHistoryManager;
    private Cursor favoriteCursor;
    private ListAdapter favoriteListAdapter;
    private Cursor historyCursor;
    private ListAdapter historyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorite_and_history);

        favorite = (TextView) findViewById(R.id.favorite_and_history_favorite);
        history = (TextView) findViewById(R.id.favorite_and_history_history);
        TabOnClickListener tabOnClickListener = new TabOnClickListener();
        favorite.setOnClickListener(tabOnClickListener);
        history.setOnClickListener(tabOnClickListener);

        ListItemLongClickListener listItemLongClickListener = new ListItemLongClickListener();
        ListItemClickListener listItemClickListener = new ListItemClickListener();

        favoriteList = (ListView) findViewById(R.id.favorite_list);
        favoriteList.setOnItemLongClickListener(listItemLongClickListener);
        favoriteList.setOnItemClickListener(listItemClickListener);

        historyList = (ListView) findViewById(R.id.history_list);
        historyList.setOnItemLongClickListener(listItemLongClickListener);
        historyList.setOnItemClickListener(listItemClickListener);

        initData();

        setResult(IntentCode.RESULT_FAVORITE_HISTORY_NULL);
    }

    private void initData() {
        if (favoriteCursor != null) {
            favoriteCursor.close();
        }
        favoriteHistoryManager = new FavoriteHistoryManager(this);
        favoriteCursor = favoriteHistoryManager.getAllFavority();
        favoriteListAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.list_item,
                favoriteCursor,
                new String[]{"_id", "name", "url"},
                new int[]{R.id.item_id, R.id.item_name, R.id.item_url}
        );
        favoriteList.setAdapter(favoriteListAdapter);

        if (historyCursor != null) {
            historyCursor.close();
        }

        historyCursor = favoriteHistoryManager.getAllHistory();
        historyListAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.list_item,
                historyCursor,
                new String[]{"_id", "name", "url", "date"},
                new int[]{R.id.item_id, R.id.item_name, R.id.item_url, R.id.item_date}
        );
        historyList.setAdapter(historyListAdapter);


    }

    @Override
    protected void onDestroy() {
        if (favoriteCursor != null) {
            favoriteCursor.close();
        }
        super.onDestroy();
    }

    private class TabOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.favorite_and_history_favorite:
                    if (!favoriteList.isShown()) {
                        favoriteList.setVisibility(View.VISIBLE);
                        historyList.setVisibility(View.GONE);
                    }
                    break;
                case R.id.favorite_and_history_history:
                    if (!historyList.isShown()) {
                        favoriteList.setVisibility(View.GONE);
                        historyList.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }
    }

    private class ListItemLongClickListener implements AdapterView.OnItemLongClickListener {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            int parentId = parent.getId();

            switch (parentId) {
                case R.id.favorite_list: {
                    listItemPopupWindow = new ListItemPopupWindow(FavoriteHistoryActivity.this,
                            ListItemPopupWindow.FAVORITE_ITEM_VIEW, 200, 330);
                    listItemPopupWindow.showAsDropDown(view, view.getWidth() / 2, view.getHeight() / 2);
                    TextView modify = (TextView) listItemPopupWindow.findViewById(R.id.favorite_item_modify);
                    TextView delete = (TextView) listItemPopupWindow.findViewById(R.id.favorite_item_delete);
                    PopupItemOnClickListener popupItemOnClickListener = new PopupItemOnClickListener(view);
                    modify.setOnClickListener(popupItemOnClickListener);
                    delete.setOnClickListener(popupItemOnClickListener);
                }
                break;
                case R.id.history_list: {
                    listItemPopupWindow = new ListItemPopupWindow(FavoriteHistoryActivity.this,
                            ListItemPopupWindow.HISTORY_ITEM_VIEW, 200, 330);
                    listItemPopupWindow.showAsDropDown(view, view.getWidth() / 2, view.getHeight() / 2);
                    TextView delete = (TextView) listItemPopupWindow.findViewById(R.id.history_item_delete);
                    TextView deleteAll = (TextView) listItemPopupWindow.findViewById(R.id.history_item_delete_all);
                    PopupItemOnClickListener popupItemOnClickListener = new PopupItemOnClickListener(view);
                    delete.setOnClickListener(popupItemOnClickListener);
                    deleteAll.setOnClickListener(popupItemOnClickListener);
                }
                break;
            }
            return false;
        }
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.favorite_list: {
                    Intent intent = new Intent();
                    intent.putExtra("url", ((TextView) view.findViewById(R.id.item_url)).getText().toString());
                    setResult(IntentCode.RESULT_FAVORITE_HISTORY_URL, intent);
                    finish();
                }
                break;
                case R.id.history_list: {
                    Intent intent = new Intent();
                    intent.putExtra("url", ((TextView) view.findViewById(R.id.item_url)).getText().toString());
                    setResult(IntentCode.RESULT_FAVORITE_HISTORY_URL, intent);
                    finish();
                    break;
                }

            }
        }
    }

    @Override
    public void finish() {
        if (favoriteCursor != null) {
            favoriteCursor.close();
        }
        super.finish();
    }

    private class PopupItemOnClickListener implements View.OnClickListener {

        private String itemId;
        private String itemName;
        private String itemUrl;

        public PopupItemOnClickListener(View item) {
            itemId = ((TextView) item.findViewById(R.id.item_id)).getText().toString();
            itemName = ((TextView) item.findViewById(R.id.item_name)).getText().toString();
            itemUrl = ((TextView) item.findViewById(R.id.item_url)).getText().toString();
        }

        @Override
        public void onClick(View v) {
            listItemPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.favorite_item_modify:
                    LayoutInflater modifyFavoriteInflater = LayoutInflater.from(FavoriteHistoryActivity.this);
                    View modifyFavoriteView = modifyFavoriteInflater.inflate(R.layout.dialog_modify, null);
                    final TextView nameInput = (TextView) modifyFavoriteView.findViewById(R.id.dialog_name_input);
                    final TextView urlInput = (TextView) modifyFavoriteView.findViewById(R.id.dialog_url_input);
                    nameInput.setText(itemName);
                    urlInput.setText(itemUrl);
                    new AlertDialog.Builder(FavoriteHistoryActivity.this)
                            .setTitle("编辑书签")
                            .setView(modifyFavoriteView)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (favoriteHistoryManager.modifyFavorite(itemId,
                                            nameInput.getText().toString(),
                                            urlInput.getText().toString())) {
                                        Toast.makeText(FavoriteHistoryActivity.this, "修改成功", Gravity.BOTTOM).show();
                                    } else {
                                        Toast.makeText(FavoriteHistoryActivity.this, "修改失败", Gravity.BOTTOM).show();
                                    }

                                }
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                    break;
                case R.id.favorite_item_delete:
                    new AlertDialog.Builder(FavoriteHistoryActivity.this).setTitle("删除书签")
                            .setMessage("是否要删除名为'" + itemName + "'的书签?")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (favoriteHistoryManager.deleteFavorite(itemId)) {
                                        Toast.makeText(FavoriteHistoryActivity.this, "删除成功", Gravity.BOTTOM).show();
                                        initData();
                                        favoriteList.invalidate();

                                    } else {
                                        Toast.makeText(FavoriteHistoryActivity.this, "删除失败", Gravity.BOTTOM).show();
                                    }
                                }
                            }).setNegativeButton("取消", null)
                            .create()
                            .show();
                    break;

                case R.id.history_item_delete:
                    new AlertDialog.Builder(FavoriteHistoryActivity.this).setTitle("删除历史记录")
                            .setMessage("是否要删除名为'" + "'的历史记录?")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (favoriteHistoryManager.deleteHistory(itemId)) {
                                        Toast.makeText(FavoriteHistoryActivity.this, "删除成功", Gravity.BOTTOM).show();
                                        initData();
                                        historyList.invalidate();
                                    } else {
                                        Toast.makeText(FavoriteHistoryActivity.this, "删除失败", Gravity.BOTTOM).show();
                                    }

                                }
                            }).setNegativeButton("取消", null)
                            .create()
                            .show();
                    break;
                case R.id.history_item_delete_all:
                    new AlertDialog.Builder(FavoriteHistoryActivity.this).setTitle("删除全部历史记录")
                            .setMessage("是否要删除全部的历史记录?")
                            .setPositiveButton("删除全部", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (favoriteHistoryManager.deleteAllHistory()) {
                                        Toast.makeText(FavoriteHistoryActivity.this, "删除成功", Gravity.BOTTOM).show();
                                        initData();
                                        historyList.invalidate();
                                    } else {
                                        Toast.makeText(FavoriteHistoryActivity.this, "删除失败", Gravity.BOTTOM).show();

                                    }
                                }
                            }).setNegativeButton("取消", null)
                            .create()
                            .show();
                    break;
            }
        }
    }
}
