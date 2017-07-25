package com.contacts.conan.cloudcontacts.javabean;

import android.graphics.Bitmap;

import com.contacts.conan.cloudcontacts.common.PinYinUtils;

import cn.bmob.v3.BmobObject;

/**
 * Created by Conan on 2016/11/10.
 */

public class Contacts extends BmobObject {

    String contactsName;
    String phone;
    long photo_id;
    Bitmap photoimg;

    String lookupKey;
    long contactsId;//效果和lookupKey一样，更简单，标识同一个联系人
    long rawcontactsId;//用于修改删除


    //拼音
    private String pinyin;
    //拼音首字母
    private String pinyinheaderWord;

    public Contacts(String contactsName,String phone,long contactid, Bitmap photoimg,long photoid,long rawcontactsId){
        this.contactsName = contactsName;
        this.phone = phone;
        this.contactsId = contactid;
        this.photoimg = photoimg;
        this.photo_id = photoid;
        this.rawcontactsId = rawcontactsId;

        this.pinyin = PinYinUtils.getPinyin(contactsName);

        pinyinheaderWord = pinyin.substring(0, 1).toUpperCase();
        if (!pinyinheaderWord.matches("[A-Z]")) {
            pinyinheaderWord = "#";
        }
    }

    public void setPhotoimg(Bitmap photoimg) {
        this.photoimg = photoimg;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public void setPinyinheaderWord(String pinyinheaderWord) {
        this.pinyinheaderWord = pinyinheaderWord;
    }
    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }
    public void setContactsId(long contactsId) {
        this.contactsId = contactsId;
    }

    public void setRawcontactsId(long rawcontactsId) {
        this.rawcontactsId = rawcontactsId;
    }

    public long getRawcontactsId() {
        return rawcontactsId;
    }

    public long getContactsId() {
        return contactsId;
    }
    public String getLookupKey() {
        return lookupKey;
    }
    public String getContactsName() {
        return contactsName;
    }

    public String getPhone() {
        return phone;
    }

    public long getPhoto_id() {
        return photo_id;
    }

    public Bitmap getPhotoimg() {
        return photoimg;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getPinyinheaderWord() {
        return pinyinheaderWord;
    }
}
