package com.contacts.conan.cloudcontacts.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.conan.cloudcontacts.R;
import com.contacts.conan.cloudcontacts.common.ActivityCollector;
import com.contacts.conan.cloudcontacts.common.Constant;
import com.contacts.conan.cloudcontacts.contacts.contactsutil.RGBLuminanceSource;
import com.contacts.conan.cloudcontacts.contacts.fragment.OperateContactsFragment;
import com.contacts.conan.cloudcontacts.contacts.fragment.ShowContactsFragment;
import com.contacts.conan.cloudcontacts.contacts.fragment.UserInfoFragment;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * Created by Conan on 2016/10/30.
 */

public class MainContactsActivity extends Activity implements View.OnClickListener{

    private static long exitTime = 0;

    //用于展示消息的Fragment
    private OperateContactsFragment operateContactsFragment;
    private ShowContactsFragment showContactsFragment;
    private UserInfoFragment userInfoFragment;

    //界面布局
    private View operateContactsLayout;
    private View showContactsLayout;
    private View userinfoLayout;

    //在Tab布局上显示消息图标的控件
    private ImageView operateContactsImage;
    private ImageView showContactsImage;
    private ImageView userInfoImage;

    //在Tab布局上显示消息文字的控件
    private TextView operateContactsText;
    private TextView showContactsText;
    private TextView userInfoText;

    //用于对Fragment进行管理
    private FragmentManager fragmentManager;

    //private int REQUEST_CODE_SCANNER_QRCODE = 0x0000c0de;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_contacts_mainframe);

        //初始化布局元素
        initViews();

        fragmentManager = getFragmentManager();

        setTabSelection(0);
    }

    private void initViews() {
        operateContactsLayout = findViewById(R.id.operate_contacts);
        showContactsLayout = findViewById(R.id.show_contacts);
        userinfoLayout = findViewById(R.id.user_info);
        operateContactsImage = (ImageView)findViewById(R.id.operate_contacts_image);
        showContactsImage = (ImageView)findViewById(R.id.show_contacts_image);
        userInfoImage = (ImageView)findViewById(R.id.operate_contacts_image);
        operateContactsText = (TextView) findViewById(R.id.operate_contacts_text);
        showContactsText = (TextView) findViewById(R.id.show_contacts_text);
        userInfoText = (TextView) findViewById(R.id.user_info_text);

        operateContactsLayout.setOnClickListener(this);
        showContactsLayout.setOnClickListener(this);
        userinfoLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.operate_contacts:
                setTabSelection(0);
                break;
            case R.id.show_contacts:
                setTabSelection(1);
                break;
            case R.id.user_info:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                //operateContactsImage.setImageResource(R.drawable.message_selected);
                operateContactsText.setTextColor(Color.WHITE);
                operateContactsLayout.setBackgroundColor(Color.parseColor("#bbbbbb"));
                if (operateContactsFragment == null) {
                    operateContactsFragment = new OperateContactsFragment();
                    transaction.add(R.id.three_activity_content, operateContactsFragment);
                } else {
                    transaction.show(operateContactsFragment);
                }
                break;
            case 1:
                //showContactsImage.setImageResource(R.drawable.message_selected);
                showContactsText.setTextColor(Color.WHITE);
                showContactsLayout.setBackgroundColor(Color.parseColor("#bbbbbb"));
                if (showContactsFragment == null) {
                    showContactsFragment = new ShowContactsFragment();
                    transaction.add(R.id.three_activity_content, showContactsFragment);
                } else {
                    transaction.show(showContactsFragment);
                }
                //getContacts();
                break;
            case 2:
            default:
                //userInfoImage.setImageResource(R.drawable.message_selected);
                userInfoText.setTextColor(Color.WHITE);
                userinfoLayout.setBackgroundColor(Color.parseColor("#bbbbbb"));
                if (userInfoFragment == null) {
                    userInfoFragment = new UserInfoFragment();
                    transaction.add(R.id.three_activity_content, userInfoFragment);
                } else {
                    transaction.show(userInfoFragment);
                }
                break;

        }
        transaction.commit();
    }

    private void clearSelection() {
        //operateContactsImage.setImageResource(R.drawable);
        //messageText.setTextColor(Color.parseColor("#82858b"));
        operateContactsText.setTextColor(Color.parseColor("#000000"));
        operateContactsLayout.setBackgroundColor(Color.parseColor("#cccccc"));

        showContactsText.setTextColor(Color.parseColor("#000000"));
        showContactsLayout.setBackgroundColor(Color.parseColor("#cccccc"));

        userInfoText.setTextColor(Color.parseColor("#000000"));
        userinfoLayout.setBackgroundColor(Color.parseColor("#cccccc"));
    }
    private void hideFragments(FragmentTransaction transaction) {
        if(operateContactsFragment != null){
            transaction.hide(operateContactsFragment);
        }
        if(showContactsFragment != null){
            transaction.hide(showContactsFragment);
        }
        if(userInfoFragment != null){
            transaction.hide(userInfoFragment);
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户修改头像
        if (requestCode == Constant.IntentRqcode.REQUEST_CODE_CHANGE_USER_PHOTO) {
            try {
                ImageView user_info_image = (ImageView)userInfoFragment.getView().findViewById(R.id.user_info_image);
                Uri uri = data.getData();
                Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                user_info_image.setImageBitmap(bit);
            }catch (Exception e){
                Toast.makeText(this, "Exception",Toast.LENGTH_SHORT).show();
            }
        }
        //二维码扫描
        if(requestCode == Constant.IntentRqcode.REQUEST_CODE_SCANNER_QRCODE){
            if(resultCode == Activity.RESULT_OK) {
                IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (intentResult != null) {
                    // ScanResult 为获取到的字符串
                    String ScanResult = intentResult.getContents();

                    dialogfun("扫描二维码的数据为：" + ScanResult);

                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }

        }
        //图片二维码识别信息返回
        if(resultCode == Constant.IntentRqcode.RESULT_CODE_SCANNER_QRIMAGE_BACK){

            if (data != null) {
                dialogfun("照片二维码的数据为："+data.getStringExtra("result"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dialogfun(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(message); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
            }
        });
        builder.create().show();
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}

