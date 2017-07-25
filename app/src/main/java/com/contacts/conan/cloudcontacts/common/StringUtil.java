package com.contacts.conan.cloudcontacts.common;

/**
 * Created by Conan on 2016/12/10.
 */

public class StringUtil {

    public static boolean isBlank(String value)
    {
        return ((value == null) || (value.trim().length() == 0));
    }
}
