package com.example.fuyg.androidbrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by fuyg on 20/01/2017.
 */

public class FavoriteHistoryActivity extends Activity {

    private TextView favorite;
    private TextView history;
    private ListView favoriteList;
    private ListView historyList;

    private FavoriteItemPopupWindow favoriteItemPopupWindow;
    private FavoriteManager favoriteManager;
    private Cursor favoriteCursor;
    private ListAdapter favoriteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorite_and_history);

        favorite = (TextView) findViewById(R.id.favorite_and_history_favorite);
        history = (TextView) findViewById(R.id.favorite_and_history_history);
        TabOnClickListener tabOnClickListener = new TabOnClickListener();
        favorite.setOnClickListener(tabOnClickListener);
        history.setOnClickListener(tabOnClickListener);

        favoriteList = (ListView) findViewById(R.id.favorite_list);
        historyList = (ListView) findViewById(R.id.history_list);
        ListItemLongClickListener listItemLongClickListener = new ListItemLongClickListener();
        favoriteList.setOnItemLongClickListener(listItemLongClickListener);

        initData();
    }

    private void initData() {
        if (favoriteCursor != null) {
            favoriteCursor.close();
        }
        favoriteManager = new FavoriteManager(this);
        favoriteCursor = favoriteManager.getAllFavorites();
        favoriteListAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.list_item,
                favoriteCursor,
                new String[]{"_id", "name", "url"},
                new int[]{R.id.item_id, R.id.item_name, R.id.item_url}
        );

        favoriteList.setAdapter(favoriteListAdapter);
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
                    break;
                case R.id.favorite_and_history_history:
                    break;

            }
        }
    }

    private class ListItemLongClickListener implements AdapterView.OnItemLongClickListener {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            int parentId = parent.getId();

            switch (parentId) {
                case R.id.favorite_list:
                    favoriteItemPopupWindow = new FavoriteItemPopupWindow(FavoriteHistoryActivity.this, 200, 400);
//                    favoriteItemPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.));
                    favoriteItemPopupWindow.showAsDropDown(view, view.getWidth() / 2, view.getHeight() / 2);
                    TextView modify = (TextView) favoriteItemPopupWindow.findViewById(R.id.favorite_item_modify);
                    TextView delete = (TextView) favoriteItemPopupWindow.findViewById(R.id.favorite_item_delete);
                    ItemPopupOnClickListener itemPopupOnClickListener = new ItemPopupOnClickListener(view);
                    modify.setOnClickListener(itemPopupOnClickListener);
                    delete.setOnClickListener(itemPopupOnClickListener);
                    break;
                case R.id.history_list:
                    break;
            }
            return false;
        }
    }

    private class ItemPopupOnClickListener implements View.OnClickListener {

        private String itemId;
        private String itemName;
        private String itemUrl;

        public ItemPopupOnClickListener(View item) {
            itemId = ((TextView) item.findViewById(R.id.item_id)).getText().toString();
            itemName = ((TextView) item.findViewById(R.id.item_name)).getText().toString();
            itemUrl = ((TextView) item.findViewById(R.id.item_url)).getText().toString();
        }

        @Override
        public void onClick(View v) {
            favoriteItemPopupWindow.dismiss();
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
                            .setPositiveButton("确定", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (favoriteManager.modifyFavorite(itemId,
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
                                    if (favoriteManager.deleteFavorite(itemId)) {
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
            }
        }
    }
}
