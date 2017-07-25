package com.contacts.conan.cloudcontacts.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Conan on 2016/11/3.
 */

public class Constant {
    // 主界面中的标题栏
    public static String FRAGMENT_TOPTITLE_OPERATECONTACTS = "云备份通讯录";
    public static String FRAGMENT_TOPTITLE_SHOWCONTACTS = "本地联系人";
    public static String FRAGMENT_TOPTITLE_USERINFO = "我的信息";
    public static float FRAGMENT_TOPTITLE_SIZE = 18;




    public static final Map<String, String> CONTACT_INFO_MAPPING = new HashMap<String, String>();
    static {
        // 1 电话
        CONTACT_INFO_MAPPING.put("101","手机");
        CONTACT_INFO_MAPPING.put("102","住宅");
        CONTACT_INFO_MAPPING.put("103","单位");
        CONTACT_INFO_MAPPING.put("104","单位传真");
        CONTACT_INFO_MAPPING.put("105","住宅传真");
        CONTACT_INFO_MAPPING.put("106","寻呼机");
        CONTACT_INFO_MAPPING.put("107","公司总机");
        CONTACT_INFO_MAPPING.put("108","助理");
        CONTACT_INFO_MAPPING.put("109","其他电话");

        // 2 邮箱
        CONTACT_INFO_MAPPING.put("201","住宅邮件");
        CONTACT_INFO_MAPPING.put("202","单位邮件");
        CONTACT_INFO_MAPPING.put("203","手机邮件");
        CONTACT_INFO_MAPPING.put("204","其他邮件");

        // 3 公司
        CONTACT_INFO_MAPPING.put("301","公司");

        // 4 昵称
        CONTACT_INFO_MAPPING.put("401","昵称");

        // 5 事件
        CONTACT_INFO_MAPPING.put("501","生日");
        CONTACT_INFO_MAPPING.put("502","周年纪念日");
        CONTACT_INFO_MAPPING.put("503","其他日期");

        // 6 地址
        CONTACT_INFO_MAPPING.put("601","单位地址");
        CONTACT_INFO_MAPPING.put("602","住宅地址");
        CONTACT_INFO_MAPPING.put("603","其他地址");

        // 7 即时通讯
        CONTACT_INFO_MAPPING.put("701","MSN");
        CONTACT_INFO_MAPPING.put("702","YAHOO");
        CONTACT_INFO_MAPPING.put("703","SKYPE");
        CONTACT_INFO_MAPPING.put("704","QQ");
        CONTACT_INFO_MAPPING.put("705","GOOGLE_TALK");
        CONTACT_INFO_MAPPING.put("706","NETMEETING");

        // 8 网站
        CONTACT_INFO_MAPPING.put("801","主页");
        CONTACT_INFO_MAPPING.put("802","博客");
        CONTACT_INFO_MAPPING.put("803","人物介绍");
        CONTACT_INFO_MAPPING.put("804","个人主页");
        CONTACT_INFO_MAPPING.put("805","工作主页");
        CONTACT_INFO_MAPPING.put("806","其他主页");

        // 9 关系
        CONTACT_INFO_MAPPING.put("901","助理");
        CONTACT_INFO_MAPPING.put("902","主管");
        CONTACT_INFO_MAPPING.put("903","合作伙伴");
        CONTACT_INFO_MAPPING.put("904","配偶");
        CONTACT_INFO_MAPPING.put("905","兄弟");
        CONTACT_INFO_MAPPING.put("906","姐妹");
        CONTACT_INFO_MAPPING.put("907","子女");
        CONTACT_INFO_MAPPING.put("908","父亲");
        CONTACT_INFO_MAPPING.put("909","母亲");
        CONTACT_INFO_MAPPING.put("910","父母");
        CONTACT_INFO_MAPPING.put("911","朋友");
        CONTACT_INFO_MAPPING.put("912","同居伴侣");
        CONTACT_INFO_MAPPING.put("913","介绍人");
        CONTACT_INFO_MAPPING.put("914","亲属");

        // 10 备注
        CONTACT_INFO_MAPPING.put("1001","备注");

    }

    //INTENT REQUEST_CODE定义
    public interface IntentRqcode{
       int REQUEST_CODE_CHANGE_USER_PHOTO = 1;//用户修改头像
       int REQUEST_CODE_SCANNER_QRIMAGE = 2;//图片二维码识别
       int RESULT_CODE_SCANNER_QRIMAGE_BACK = 789;//图片二维码识别信息返回
       int REQUEST_CODE_SCANNER_QRCODE = 0x0000c0de;//二维码扫描
    }
}
