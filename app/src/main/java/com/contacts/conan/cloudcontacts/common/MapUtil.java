package com.contacts.conan.cloudcontacts.common;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Conan on 2016/12/10.
 */

public class MapUtil {

    //map 按照KEY值排序
    public static ArrayList<String> sortMapKey(Map map){
        int[] intkey = new int[50];
        ArrayList<String> listkey = new ArrayList<String>();

        Set<String> set = map.keySet();
        Iterator it = set.iterator();
        int i = 0;
        for (;it.hasNext();i++){
            String key = (String) it.next();
            intkey[i] = Integer.parseInt(key);
        }
        Log.d("map i", String.valueOf(i));
        int j = i;
        for (int x = 0;x < j ;x++){
            for (int y = x;y < j;y++){
                if(intkey[x] > intkey[y]){
                    int temp = intkey[y];
                    intkey[y] = intkey[x];
                    intkey[x] = temp;
                }
            }
        }

        for (int p = 0;p < j;p++){
            listkey.add(String.valueOf(intkey[p]));
            //Log.d("map stringkey", stringkey[p]);
        }/*
        for (int q = 0; q< j;q++){
            mapresult.put(stringkey[q],map.get(stringkey[q]));
            Log.d("map mapresult", stringkey[q] +":"+ mapresult.get(stringkey[q]));
        }*/

        return listkey;
    }
}
