
package com.example.fuyg.androidbrowser.file;

import java.util.Comparator;
import java.util.HashMap;

import com.example.fuyg.androidbrowser.file.FileShowManager;


public class SortComparator implements Comparator<HashMap<String, Object>> {

    private FileShowManager.SortType sortType;

    public SortComparator(FileShowManager.SortType sortType) {
        this.sortType = sortType;
    }

    @Override
    public int compare(HashMap<String, Object> lhs, HashMap<String, Object> rhs) {
        switch (sortType) {
            case Alphabet:
                return String.valueOf(lhs.get("name")).compareTo(String.valueOf(rhs.get("name")));
            case Date:
                return String.valueOf(lhs.get("date")).compareTo(String.valueOf(rhs.get("datte")));
            case ChildCount:
                return String.valueOf(lhs.get("childCount")).compareTo(String.valueOf(rhs.get("childCount")));
        }
        return 0;
    }
}
