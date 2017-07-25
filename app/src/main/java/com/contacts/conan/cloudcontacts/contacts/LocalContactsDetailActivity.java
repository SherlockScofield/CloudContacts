package com.contacts.conan.cloudcontacts.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contacts.conan.cloudcontacts.R;
import com.contacts.conan.cloudcontacts.common.BusinessUtil;
import com.contacts.conan.cloudcontacts.common.Constant;
import com.contacts.conan.cloudcontacts.common.MapUtil;
import com.contacts.conan.cloudcontacts.contacts.contactsutil.GetContactsInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Conan on 2016/12/10.
 * 2016-12-11 00:05:58
 */

public class LocalContactsDetailActivity extends Activity {

    /** --------------------获取单个联系人信息--------------------*/
    //存放某一个联系人的详细信息
    private Map contactInfo;

    private String[] MIMETYPES = new String[]{
            //ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,//联系人名称
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,//联系人电话(可能包含多个)
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,//邮箱(多个)
            //ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,//公司
            ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE,//昵称
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,//事件
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,//地址
            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE,//即时消息
            //ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,//网站
            ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE,//该联系人的关系
            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,//备注
            //ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE
    };


    private TextView contact_top_tv_name;
    private TextView contact_tv_name;
    private ImageView contact_im_photo;
    private LinearLayout contactInfo_LL;

    private Button delete_bt;
    private Button call_bt;

    private long rawcontactId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_local_contacts_detail);
        initView();

        Intent intent = getIntent();
        Log.i("detailcontact", intent.getLongExtra("CONTACT_ID",-1) +"");
        Log.i("detailcontact", intent.getStringExtra("CONTACT_NAME"));
        Log.i("detailcontact", intent.getLongExtra("CONTACT_PHOTOID",-1)+"");
        long contactId = intent.getLongExtra("CONTACT_ID",-1);
        rawcontactId = intent.getLongExtra("RAW_CONTACT_ID",-1);
        /** ----- 获取联系人名称-----*/
        String contactsName = intent.getStringExtra("CONTACT_NAME");//需要显示的联系人名称
        long contactPhotoId = intent.getLongExtra("CONTACT_PHOTOID",-1);

        ContentResolver resolver = getContentResolver();
        /** ----- 获取联系人头像 -----*/
        Bitmap  contactPhoto = getContactPhoto(resolver,contactId,contactPhotoId);//需要显示的联系人头像

        /** ----- 获取联系人信息 -----*/
        getContactDetail(resolver,contactId);

        //删除联系人
        delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(rawcontactId);
            }
        });

        //进行排序
        ArrayList<String> sortkey = MapUtil.sortMapKey(contactInfo);
        Log.i("LocalContactsDetail", "onCreate: contactInfo"+contactInfo.toString());

        /** -----在界面上设置联系人信息 ----*/

        contact_top_tv_name.setText(contactsName);
        contact_tv_name.setText(contactsName);
        contact_im_photo.setImageBitmap(contactPhoto);

        for (int i = 0 ;i< sortkey.size();i++){

            Log.i("--contactData--", i+":"+sortkey.get(i));
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(100);

            TextView tvname = new TextView(this);
            tvname.setText(Constant.CONTACT_INFO_MAPPING.get(sortkey.get(i)));
            tvname.setTextSize(16);
            tvname.setWidth(320);

            TextView tvvalue = new TextView(this);
            tvvalue.setText((String)contactInfo.get(sortkey.get(i)));
            tvvalue.setTextSize(14);
            ll.addView(tvname);
            ll.addView(tvvalue);
            contactInfo_LL.addView(ll);
        }


    }

    private Bitmap getContactPhoto(ContentResolver resolver,long contactId,long contactPhotoId){
        Bitmap  contactPhoto = null;//
        //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
        if (contactPhotoId > 0) {
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
            contactPhoto = BitmapFactory.decodeStream(input);
        } else {
            contactPhoto = BitmapFactory.decodeResource(getResources(), R.mipmap.contacts_photo_default_image);
        }
        return contactPhoto;
    }

    private void getContactDetail(ContentResolver resolver,long contactId){
        contactInfo = new HashMap();//需要显示的联系人信息
        GetContactsInfo getContactsInfo = new GetContactsInfo();
        for (int i = 0;i<MIMETYPES.length;i++){
            getContactsInfo.getData1(resolver,contactInfo,String.valueOf(contactId),MIMETYPES[i]);
        }
    }

    private void initView(){
        contactInfo_LL = (LinearLayout) findViewById(R.id.contact_detailinfo_layout_info);
        contact_top_tv_name = (TextView) findViewById(R.id.contact_detailinfo_tv_top_name);
        contact_tv_name = (TextView) findViewById(R.id.contact_detailinfo_tv_name);
        contact_im_photo = (ImageView) findViewById(R.id.contact_detailinfo_iv_image);

        call_bt = (Button)findViewById(R.id.local_contacts_call_bt);
        delete_bt = (Button) findViewById(R.id.local_contacts_delete_bt);

    }

    //删除联系人
    private void deleteContact(long rawcontactId){
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = this.getContentResolver();
        resolver.delete(uri, "raw_contact_id=?", new String[]{rawcontactId+""});
        dialogfun("删除成功");

    }

    //弹出对话框
    private void dialogfun(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(message); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                finish();
            }
        });
        builder.create().show();
    }
}
