package com.contacts.conan.cloudcontacts.contacts.contactsutil;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.util.Log;

import com.contacts.conan.cloudcontacts.common.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Conan on 2016/12/9.
 */

public class GetContactsInfo {

    private ContentResolver contentResolver;

    private Map map = new HashMap();

    private String[] MIMETYPES = new String[]{
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,//联系人名称
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,//联系人电话(可能包含多个)
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,//邮箱(多个)
            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,//公司
            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,//备注
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,//地址
            ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,//网站
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE
    };

    //Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);
    //cursor.getCount() 就是联系人的总数
    /**
     * 根据MIMETYPE类型, 返回对应联系人的data1字段的数据
     */
    public void getData1(ContentResolver contentResolver,Map mapinfo, String contactId, final String mimeType) {
        StringBuilder stringBuilder = new StringBuilder();

        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.DATA1,ContactsContract.Data.DATA2},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Data.MIMETYPE + "='" + mimeType + "'",
                new String[]{String.valueOf(contactId)}, null);

        if (dataCursor != null && dataCursor.getCount() > 0) {
            if (dataCursor.moveToFirst()) {
                do {
                    String value = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));//值
                    int type = dataCursor.getInt(dataCursor.getColumnIndex(ContactsContract.Data.DATA2));//type
                    getDetailInfo(mapinfo,mimeType,type,value);
                } while (dataCursor.moveToNext());
            }
            dataCursor.close();
        }
        //return stringBuilder.toString();
    }

    /**
     *  获取一个联系人信息分类
     *  */
    private void getDetailInfo(Map mapinfo,String mimetype,int type,String value){

        // 1 获取电话信息
        getPhoneInfo(mapinfo,mimetype,type,value);
        // 2 查找email地址
        getEmailInfo(mapinfo,mimetype,type,value);
        // 3 获取公司信息
        getOrganizationInfo(mapinfo,mimetype,type,value);
        // 4 获取昵称信息
        getNicknameInfo(mapinfo,mimetype,type,value);
        // 5 查找event信息
        getEventInfo(mapinfo,mimetype,type,value);
        // 6 获取地址信息
        getAddressInfo(mapinfo,mimetype,type,value);
        // 7 获取即时消息   -- 一些即时通讯(视频、语音等)软件的账号，如：QQ、SKYPE、雅虎、网络会议
        getIMInfo(mapinfo,mimetype,type,value);
        // 8 获取网站信息
        getWebsiteInfo(mapinfo,mimetype,type,value);
        // 9 获取关系信息
        getRelationInfo(mapinfo,mimetype,type,value);
        // 10 获取备注信息
        getNoteInfo(mapinfo,mimetype,type,value);

        //分组信息
        //getGroupInfo(mapinfo,mimetype,type,value);
    }

    //分组信息
    private void getGroupInfo(Map mapinfo,String mimetype,int type,String value) {
        if(GroupMembership.CONTENT_ITEM_TYPE.equals(mimetype)){

        }
    }

    // 10 获取备注信息
    private void getNoteInfo(Map mapinfo,String mimetype,int type,String value) {
        if (Note.CONTENT_ITEM_TYPE.equals(mimetype)) {
            String remark = value;
            if (!StringUtil.isBlank(remark)) {
                mapinfo.put("1001", remark);
            }
        }
    }

    // 9 获取关系信息
    private void getRelationInfo(Map mapinfo,String mimetype,int type,String value) {
        if(Relation.CONTENT_ITEM_TYPE.equals(mimetype)){
            int relationType = type;
            // 901 助理
            if(Relation.TYPE_ASSISTANT == relationType){
                String assistant = value;
                if(!StringUtil.isBlank(assistant)){
                    mapinfo.put("901", assistant);
                }
            }
            // 902 主管
            if(Relation.TYPE_MANAGER == relationType){
                String manager = value;
                if(!StringUtil.isBlank(manager)){
                    mapinfo.put("902", manager);
                }
            }
            // 903 搭档
            if(Relation.TYPE_PARTNER == relationType){
                String partner = value;
                if(!StringUtil.isBlank(partner)){
                    mapinfo.put("903", partner);
                }
            }
            // 904 配偶
            if(Relation.TYPE_SPOUSE == relationType){
                String spouse = value;
                if(!StringUtil.isBlank(spouse)){
                    mapinfo.put("904", spouse);
                }
            }
            // 905 兄弟
            if(Relation.TYPE_BROTHER == relationType){
                String brother = value;
                if(!StringUtil.isBlank(brother)){
                    mapinfo.put("905", brother);
                }
            }
            // 906 姐妹
            if(Relation.TYPE_SISTER == relationType){
                String sister = value;
                if(!StringUtil.isBlank(sister)){
                    mapinfo.put("906", sister);
                }
            }
            // 907 孩子
            if(Relation.TYPE_CHILD == relationType){
                String child = value;
                if(!StringUtil.isBlank(child)){
                    mapinfo.put("907", child);
                }
            }
            // 908 父亲
            if(Relation.TYPE_FATHER == relationType){
                String father = value;
                if(!StringUtil.isBlank(father)){
                    mapinfo.put("908", father);
                }
            }
            // 909 母亲
            if(Relation.TYPE_MOTHER == relationType){
                String mother = value;
                if(!StringUtil.isBlank(mother)){
                    mapinfo.put("909", mother);
                }
            }
            // 910 父母
            if(Relation.TYPE_PARENT == relationType){
                String parent = value;
                if(!StringUtil.isBlank(parent)){
                    mapinfo.put("910", parent);
                }
            }
            // 911 朋友
            if(Relation.TYPE_FRIEND == relationType){
                String friend = value;
                if(!StringUtil.isBlank(friend)){
                    mapinfo.put("911", friend);
                }
            }

            // 912 佣人
            if(Relation.TYPE_DOMESTIC_PARTNER == relationType){
                String domesticPartner = value;
                if(!StringUtil.isBlank(domesticPartner)){
                    mapinfo.put("912", domesticPartner);
                }
            }
            // 913 涉及人员
            if(Relation.TYPE_REFERRED_BY == relationType){
                String referred = value;
                if(!StringUtil.isBlank(referred)){
                    mapinfo.put("913", referred);
                }
            }
            // 914 相关人员
            if(Relation.TYPE_RELATIVE == relationType){
                String relative = value;
                if(!StringUtil.isBlank(relative)){
                    mapinfo.put("914", relative);
                }
            }

        }
    }

    // 8 获取网站信息
    private void getWebsiteInfo(Map mapinfo,String mimetype,int type,String value) {
        if (Website.CONTENT_ITEM_TYPE.equals(mimetype)) {
            // 取出组织类型
            int webType = type;
            // 801 主页
            if (webType == Website.TYPE_CUSTOM) {
                String home = value;
                if (!StringUtil.isBlank(home)) {
                    mapinfo.put("801", home);
                }
            }
            // 801 主页
            else if (webType == Website.TYPE_HOME) {
                String home = value;
                if (!StringUtil.isBlank(home)) {
                    mapinfo.put("801", home);
                }
            }
            // 802 博客地址
            if (webType == Website.TYPE_BLOG) {
                String blog = value;
                if (!StringUtil.isBlank(blog)) {
                    mapinfo.put("802", blog);
                }
            }
            // 803 人物介绍地址
            if (webType == Website.TYPE_PROFILE) {
                String profile = value;
                if (!StringUtil.isBlank(profile)) {
                    mapinfo.put("803", profile);
                }
            }
            // 804 个人主页
            if (webType == Website.TYPE_HOMEPAGE) {
                String homePage = value;
                if (!StringUtil.isBlank(homePage)) {
                    mapinfo.put("804", homePage);
                }
            }
            // 805 工作主页
            if (webType == Website.TYPE_WORK) {
                String workPage = value;
                if (!StringUtil.isBlank(workPage)) {
                    mapinfo.put("805", workPage);
                }
            }
            // 806 其他主页
            if (webType == Website.TYPE_OTHER) {
                String otherPage = value;
                if (!StringUtil.isBlank(otherPage)) {
                    mapinfo.put("806", otherPage);
                }
            }
        }
    }

    // 7 获取即时消息   -- 一些即时通讯(视频、语音等)软件的账号，如：QQ、SKYPE、雅虎、网络会议
    private void getIMInfo(Map mapinfo,String mimetype,int type,String value) {
        if (Im.CONTENT_ITEM_TYPE.equals(mimetype)) {
            // 701 取出即时消息类型
            int protocal = type;
            if (Im.TYPE_CUSTOM == protocal) {
                String workMsg = value;
                if (!StringUtil.isBlank(workMsg)) {
                    mapinfo.put("701", workMsg);
                }
            }
            // 701 MSN
            else if (Im.PROTOCOL_MSN == protocal) {
                String workMsg = value;
                if (!StringUtil.isBlank(workMsg)) {
                    mapinfo.put("701", workMsg);
                }
            }

            // 702 YAHOO
            if (Im.PROTOCOL_YAHOO == protocal) {
                String skypeMsg = value;
                if (!StringUtil.isBlank(skypeMsg)) {
                    mapinfo.put("702", skypeMsg);
                }
            }
            // 703 SKYPE
            if (Im.PROTOCOL_SKYPE == protocal) {
                String skypeMsg = value;
                if (!StringUtil.isBlank(skypeMsg)) {
                    mapinfo.put("703", skypeMsg);
                }
            }
            // 704 QQ
            if (Im.PROTOCOL_QQ == protocal) {
                String instantsMsg = value;
                if (!StringUtil.isBlank(instantsMsg)) {
                    mapinfo.put("704", instantsMsg);
                }
            }
            // 705 GOOGLE_TALK
            if (Im.PROTOCOL_GOOGLE_TALK == protocal) {
                String googleTalkMsg = value;
                if (!StringUtil.isBlank(googleTalkMsg)) {
                    mapinfo.put("705", googleTalkMsg);
                }
            }
            // 706 NETMEETING
            if (Im.PROTOCOL_NETMEETING == protocal) {
                String netMeetingMsg = value;
                if (!StringUtil.isBlank(netMeetingMsg)) {
                    mapinfo.put("706", netMeetingMsg);
                }
            }
        }
    }

    // 6 获取地址信息
    private void getAddressInfo(Map mapinfo,String mimetype,int type,String value) {
        if (StructuredPostal.CONTENT_ITEM_TYPE.equals(mimetype)) {
            // 取出地址类型
            int postalType = type;
            // 601 单位地址
            if (postalType == StructuredPostal.TYPE_WORK) {
                String companyAddress = value;
                if (!StringUtil.isBlank(companyAddress)) {
                    mapinfo.put("601", companyAddress);
                }
            }
            // 602 住宅地址
            if (postalType == StructuredPostal.TYPE_HOME) {
                String homeAddress = value;
                if (!StringUtil.isBlank(homeAddress)) {
                    mapinfo.put("602", homeAddress);
                }
            }
            // 603 其他地址
            if (postalType == StructuredPostal.TYPE_OTHER) {
                String otherAddress = value;
                if (!StringUtil.isBlank(otherAddress)) {
                    mapinfo.put("603", otherAddress);
                }
            }
        }
    }

    // 5 获取事件信息
    private void getEventInfo(Map mapinfo,String mimetype,int type,String value) {
        if (Event.CONTENT_ITEM_TYPE.equals(mimetype)) {
            // 取出时间类型
            int eventType = type;
            // 501 生日
            if (eventType == Event.TYPE_BIRTHDAY) {
                String birthday = value;
                if (!StringUtil.isBlank(birthday)) {
                    mapinfo.put("501", birthday);
                }
            }
            // 502 周年纪念日
            if (eventType == Event.TYPE_ANNIVERSARY) {
                String anniversary = value;
                if (!StringUtil.isBlank(anniversary)) {
                    mapinfo.put("502", anniversary);
                }
            }
            // 503 自定义纪念日
            if (eventType == Event.TYPE_OTHER) {
                String otherday = value;
                if (!StringUtil.isBlank(otherday)) {
                    mapinfo.put("503", otherday);
                }
            }
        }
    }

    // 4 获取昵称信息
    private void getNicknameInfo(Map mapinfo,String mimetype,int type,String value) {
        if (Nickname.CONTENT_ITEM_TYPE.equals(mimetype)) {
            String nickName = value;
            if (!StringUtil.isBlank(nickName)) {
                mapinfo.put("401", nickName);
            }
        }
    }

    // 3 获取组织信息
    private void getOrganizationInfo(Map mapinfo,String mimetype,int type,String value) {
        if (Organization.CONTENT_ITEM_TYPE.equals(mimetype)) {
            // 取出组织类型
            int orgType = type;
            // 301 公司
            if (orgType == Organization.TYPE_CUSTOM) {
                String company = value;
                if (!StringUtil.isBlank(company)) {
                    mapinfo.put("301", company);
                }
            }
        }
    }

    // 2 获取邮箱信息
    private void getEmailInfo(Map mapinfo,String mimetype,int type,String value) {
        if (Email.CONTENT_ITEM_TYPE.equals(mimetype)) {
            // 取出邮件类型
            int emailType = type;

            // 201 住宅邮件地址
            if (emailType == Email.TYPE_CUSTOM) {
                String homeEmail = value;
                if (!StringUtil.isBlank(homeEmail)) {
                    mapinfo.put("201", homeEmail);
                }
            }

            // 201 住宅邮件地址
            else if (emailType == Email.TYPE_HOME) {
                String homeEmail = value;
                if (!StringUtil.isBlank(homeEmail)) {
                    mapinfo.put("201", homeEmail);
                }
            }
            // 202 单位邮件地址
            if (emailType == Email.TYPE_WORK) {
                String jobEmail = value;
                if (!StringUtil.isBlank(jobEmail)) {
                    mapinfo.put("202", jobEmail);
                }
            }

            // 203 手机邮件地址
            if (emailType == Email.TYPE_MOBILE) {
                String mobileEmail = value;
                if (!StringUtil.isBlank(mobileEmail)) {
                    mapinfo.put("203", mobileEmail);
                }
            }
            // 204 其他邮件地址
            if (emailType == Email.TYPE_OTHER) {
                String otherEmail = value;
                if (!StringUtil.isBlank(otherEmail)) {
                    mapinfo.put("204", otherEmail);
                }
            }
        }
    }

    // 1 获取电话信息
    private void getPhoneInfo(Map mapinfo,String mimetype,int type,String value) {

        if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
            // 取出电话类型
            int phoneType = type;
            // 101 手机
            if (phoneType == Phone.TYPE_MOBILE) {
                String mobile = value;
                if (!StringUtil.isBlank(mobile)) {
                    mapinfo.put("101", mobile);
                }
            }
            // 102 住宅电话
            if (phoneType == Phone.TYPE_HOME) {
                String homeNum = value;
                if (!StringUtil.isBlank(homeNum)) {
                    mapinfo.put("102", homeNum);
                }
            }
            // 103 单位电话
            if (phoneType == Phone.TYPE_WORK) {
                String jobNum = value;
                if (!StringUtil.isBlank(jobNum)) {
                    mapinfo.put("103", jobNum);
                }
            }
            // 104 单位传真
            if (phoneType == Phone.TYPE_FAX_WORK) {
                String workFax = value;
                if (!StringUtil.isBlank(workFax)) {
                    mapinfo.put("104", workFax);
                }
            }
            // 105 住宅传真
            if (phoneType == Phone.TYPE_FAX_HOME) {
                String homeFax = value;
                if (!StringUtil.isBlank(homeFax)) {
                    mapinfo.put("105", homeFax);
                }
            }
            // 106 寻呼机
            if (phoneType == Phone.TYPE_PAGER) {
                String pager = value;
                if (!StringUtil.isBlank(pager)) {
                    mapinfo.put("106", pager);
                }
            }
            // 107 公司总机
            if (phoneType == Phone.TYPE_COMPANY_MAIN) {
                String jobTel = value;
                if (!StringUtil.isBlank(jobTel)) {
                    mapinfo.put("107", jobTel);
                }
            }
            // 108 助理
            if (phoneType == Phone.TYPE_ASSISTANT) {
                String assistantNum = value;
                if (!StringUtil.isBlank(assistantNum)) {
                    mapinfo.put("108", assistantNum);
                }
            }
            // 109 其他
            if (phoneType == Phone.TYPE_OTHER) {
                String other = value;
                if (!StringUtil.isBlank(other)) {
                    mapinfo.put("109", other);
                }
            }
        }
    }
}
