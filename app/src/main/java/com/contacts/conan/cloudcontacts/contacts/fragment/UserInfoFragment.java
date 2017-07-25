package com.contacts.conan.cloudcontacts.contacts.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.conan.cloudcontacts.R;
import com.contacts.conan.cloudcontacts.common.Constant;
import com.contacts.conan.cloudcontacts.contacts.MainContactsActivity;

/**
 * Created by Conan on 2016/11/2.
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener{

    private ImageView user_info_image;
    private int MY_PERMISSIONS_REQUEST_READ_PHOTO = 123;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View operateContactsLayout = inflater.inflate(R.layout.activity_main_user_info,container,false);
        return operateContactsLayout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView common_top_tv_middle_userinfo = (TextView)getActivity().findViewById(R.id.common_top_tv_middle_userinfo);
        common_top_tv_middle_userinfo.setText(Constant.FRAGMENT_TOPTITLE_USERINFO);
        common_top_tv_middle_userinfo.setTextColor(Color.WHITE);
        common_top_tv_middle_userinfo.setTextSize(Constant.FRAGMENT_TOPTITLE_SIZE);

        LinearLayout user_info_ll_changeinfo = (LinearLayout)getActivity().findViewById(R.id.user_info_ll_changeinfo);
        LinearLayout user_info_ll_changepassword = (LinearLayout)getActivity().findViewById(R.id.user_info_ll_changepassword);
        LinearLayout user_info_ll_clouddustbin = (LinearLayout)getActivity().findViewById(R.id.user_info_ll_clouddustbin);
        LinearLayout user_info_ll_localdustbin = (LinearLayout)getActivity().findViewById(R.id.user_info_ll_localdustbin);
        LinearLayout user_info_ll_checkupdate = (LinearLayout)getActivity().findViewById(R.id.user_info_ll_checkupdate);
        LinearLayout user_info_ll_aboutus = (LinearLayout)getActivity().findViewById(R.id.user_info_ll_aboutus);
        Button user_info_bt_loginout = (Button)getActivity().findViewById(R.id.user_info_bt_loginout);

        user_info_image = (ImageView)getActivity().findViewById(R.id.user_info_image);

        user_info_ll_changeinfo.setOnClickListener(this);
        user_info_ll_changepassword.setOnClickListener(this);
        user_info_ll_clouddustbin.setOnClickListener(this);
        user_info_ll_localdustbin.setOnClickListener(this);
        user_info_ll_checkupdate.setOnClickListener(this);
        user_info_ll_aboutus.setOnClickListener(this);
        user_info_bt_loginout.setOnClickListener(this);
        user_info_image.setOnClickListener(this);
    }

    /*public void getPhoto()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_PHOTO);
        } else{
            getUserPhoto();
        }
    }*/
    /**-------------------------读写联系人权限 用户接受或拒绝处理逻辑判断------------------------------------------*/
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTO){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getUserPhoto();
            } else{
                // Permission Denied
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
*/
    public void getUserPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent,Constant.IntentRqcode.REQUEST_CODE_CHANGE_USER_PHOTO);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_info_ll_changeinfo:
                Toast.makeText(getActivity(),"修改资料",Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_info_ll_changepassword:
                Toast.makeText(getActivity(),"重置密码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_info_ll_clouddustbin:
                Toast.makeText(getActivity(),"云端垃圾箱",Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_info_ll_localdustbin:
                Toast.makeText(getActivity(),"本地垃圾箱",Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_info_ll_checkupdate:
                Toast.makeText(getActivity(),"检查更新",Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_info_ll_aboutus:
                Toast.makeText(getActivity(),"关于我们",Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_info_bt_loginout:
                Toast.makeText(getActivity(),"退出登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_info_image:
                getUserPhoto();
                break;
            default:
                break;
        }
    }
}
