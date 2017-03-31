package com.example.fuyg.androidbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fuyg.androidbrowser.file.FileShowManager;
import com.example.fuyg.androidbrowser.ListItemPopupWindow;

import org.w3c.dom.Text;

import java.util.List;


/**
 * Created by fuyg on 25/01/2017.
 */

public class FileActivity extends Activity {
    private static final String TAG = "FileActivity";

    private ListView listView;
    private TextView sure;
    private TextView cancel;
    private Button createNewFolder;

    private FileShowManager fileShowManager;
    private String currentPath;

    private ListItemPopupWindow listItemPopupWindow;
    private FileManagerOnClickListener clickListener;
    private FileManagerOnItemClickListener itemClickListener;
    private FileManagerOnItemLongClickListener itemLongClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.file_manager_activity);

        listView = (ListView) findViewById(R.id.file_manager_list);
        sure = (TextView) findViewById(R.id.file_manager_toolbar_sure);
        cancel =  (TextView) findViewById(R.id.file_manager_toolbar_cancel);
        createNewFolder = (Button) findViewById(R.id.file_manager_create_dir);

        fileShowManager = new FileShowManager(this, listView);

        clickListener = new FileManagerOnClickListener();
        sure.setOnClickListener(clickListener);
        cancel.setOnClickListener(clickListener);
        createNewFolder.setOnClickListener(clickListener);

        itemClickListener = new FileManagerOnItemClickListener();
        itemLongClickListener = new FileManagerOnItemLongClickListener();
        listView.setOnItemClickListener(itemClickListener);
        listView.setOnItemLongClickListener(itemLongClickListener);
    }

    private class FileManagerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.file_manager_create_dir:
                    // TODO
                    break;
                case R.id.file_manager_toolbar_sure:
                    // TODO
                    break;
                case R.id.file_manager_toolbar_cancel:
                    // TODO
                    break;
            }

        }
    }

    private class FileManagerOnItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            TextView date = (TextView) view.findViewById(R.id.file_manager_item_date);
            if (date.getText().toString().equals("")) {
                return false;
            }

            listItemPopupWindow = new ListItemPopupWindow(FileActivity.this, ListItemPopupWindow.)


            return false;
        }
    }

    private class FileManagerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView path = (TextView) view.findViewById(R.id.file_manager_item_path);
            currentPath = path.getText().toString();
            Log.d(TAG, "current path: " + currentPath);
            fileShowManager = new FileShowManager(FileActivity.this, listView);
            fileShowManager.execute(currentPath);
        }
    }
}
