package com.example.fuyg.androidbrowser.file;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.fuyg.androidbrowser.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by fuyg on 25/01/2017.
 */

public class FileShowManager extends AsyncTask<String, String, List<HashMap<String, Object>>> {


    public static final String SD_CARD_HOME = Environment.getExternalStorageDirectory().toString();
    public static final String DEFAULT_PATH = SD_CARD_HOME + "/androidBrowser/download/";
    private static final String TAG = "FileShowManager";
    private Activity activity;
    private ListView fileList;
    private Dialog waitDialog;
    public FileShowManager(Activity activity, ListView fileListView) {
        this.activity = activity;
        this.fileList = fileListView;
    }

    @Override
    protected List<HashMap<String, Object>> doInBackground(String... params) {
        List<HashMap<String, Object>> fileList = buildListForAdapter(params[0]);
        sortByKey(fileList, SortType.Alphabet);
        return fileList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, Object>> hashMaps) {
        initData(hashMaps);
        fileList.invalidate();
        waitDialog.dismiss();
        super.onPostExecute(hashMaps);
    }

    public void initData(List<HashMap<String, Object>> fileList) {
        SimpleAdapter adapter = new SimpleAdapter(
                activity.getApplicationContext(),
                fileList,
                R.layout.file_manager_list_item,
                new String[]{"name", "path", "childCount", "date", "img"},
                new int[]{
                        R.id.file_manager_item_name,
                        R.id.file_manager_item_path,
                        R.id.file_manager_item_child_count,
                        R.id.file_manager_item_date,
                        R.id.file_manager_item_icon
                }
        );
    }

    public List<HashMap<String, Object>> buildListForAdapter(String path) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        File rootFile = new File(path);
        if(!rootFile.exists()) {
            rootFile = new File(DEFAULT_PATH);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
        }

        File[] files = rootFile.listFiles();
        Log.d(TAG, rootFile.toString() + ": " + files.toString());
        if (!path.equals(SD_CARD_HOME)) {
            HashMap<String, Object> root = new HashMap<String, Object>();
            root.put("name", "/");
            root.put("img", android.R.drawable.folder_home_back);
            root.put("path", SD_CARD_HOME);
            root.put("childCount", "返回根目录");
            root.put("date", "");
            list.add(root);

            HashMap<String, Object> pmap = new HashMap<String, Object>();
            pmap.put("name", "..");
            pmap.put("img", android.R.drawable.folder_up_back);
            pmap.put("path", rootFile.getParent());
            pmap.put("childCount", "返回上一级");
            pmap.put("date", "");
            list.add(pmap);
        }

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("img", android.R.drawable.folder_back);
                    item.put("name", file.getName());
                    item.put("path", file.getPath());
                    item.put("childCount", "共有" + getDirectoryCount(file) + "项");
                    item.put("date",  (new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)).format(file.lastModified()));
                    list.add(item);
                }
            }
        }

        return list;
    }

    public int getDirectoryCount(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            return getDirectoryCount(files);
        }
        return 0;
    }

    public int getDirectoryCount(File[] files) {
        if (files == null) {
            return 0;
        }

        int count = 0;

        for (File file : files) {
            if (file.isDirectory()) {
                count++;
            }
        }

        return count;
    }

    public List<HashMap<String, Object>> sortByKey(List<HashMap<String, Object>> list, SortType sortType) {
        Collections.sort(list, new SortComparator(sortType));
        Log.d(TAG, "list.sort: [" + list + "]");
        return list;
    }


    @Override
    protected void onPreExecute() {

        waitDialog = new AlertDialog.Builder(activity)
                .setMessage("正在加载中...")
                .create();
        waitDialog.setCancelable(false);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.show();
        super.onPreExecute();
    }

    public enum SortType {
        Alphabet,
        Date,
        ChildCount
    }
}
