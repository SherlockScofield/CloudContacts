package com.contacts.conan.cloudcontacts.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Conan on 2016/11/12.
 */

public class ActivityCollector {

    public static List<Activity> activityList = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishOneActivity(Activity activity){
        activity.finish();
    }

    public static void finishActivity(){
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
