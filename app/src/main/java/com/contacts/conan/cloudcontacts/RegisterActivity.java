package com.contacts.conan.cloudcontacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.conan.cloudcontacts.common.ActivityCollector;
import com.contacts.conan.cloudcontacts.javabean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Conan on 2016/10/30.
 */

public class RegisterActivity extends Activity {
    private ProgressDialog progressDialog;
    EditText register_et_phone;
    EditText register_et_email;
    EditText register_et_password;
    EditText register_et_check_password;
    Button register_bt_register;

    TextView register_tv_back ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initView();

        register_bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkdata()){
                    registerUser();
                }
            }
        });

        register_tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishOneActivity(RegisterActivity.this);
            }
        });

    }
    private boolean checkdata(){
        if(TextUtils.isEmpty(register_et_phone.getText())){
            dialogfun("手机号码不能为空!");
            return false;
        }
        else{
            if((register_et_phone.getText().toString()).length() != 11 || !(register_et_phone.getText().toString()).substring(0,1).equals("1")){
                dialogfun("手机号码格式错误!");
                return false;
            }
        }
        if(TextUtils.isEmpty(register_et_email.getText())){
            dialogfun("电子邮箱不能为空!");
            return false;
        }
        else{
            if(!((register_et_email.getText().toString()).indexOf("@") >= 0) || !((register_et_email.getText().toString()).indexOf(".") >= 0) ||
                    ((register_et_email.getText().toString()).indexOf("@") > (register_et_email.getText().toString()).indexOf("."))  ){
                dialogfun("电子邮箱格式错误!");
                return false;
            }
        }
        if(TextUtils.isEmpty(register_et_password.getText())){
            dialogfun("密码不能为空!");
            return false;
        }
        String password = register_et_password.getText().toString();
        if(password.length()<6 || password.length() >16){
            dialogfun("密码长度不在范围内!");
            return false;
        }
        if(!password.matches("[A-Za-z0-9_]+")){
            dialogfun("密码中含有非法字符");
            return false;
        }
        if(TextUtils.isEmpty(register_et_check_password.getText())){
            dialogfun("确认密码不能为空!");
            return false;
        }
        if(!TextUtils.isEmpty(register_et_password.getText()) && !TextUtils.isEmpty(register_et_check_password.getText()) &&
                !(register_et_password.getText().toString().equals(register_et_check_password.getText().toString()))){
            dialogfun("两次密码不同!");
            return false;
        }
        return true;
    }
    private void registerUser(){
        progressDialog();
        String phone = register_et_phone.getText().toString();
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("phone",phone);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null) {
                    if (list.size() > 0) {
                        progressDialog.dismiss();
                        dialogfun("该手机号已被注册！");
                    }
                    else{
                        String email = register_et_email.getText().toString();
                        BmobQuery<User> query = new BmobQuery<User>();
                        query.addWhereEqualTo("email",email);
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {
                                if(e==null) {
                                    if (list.size() > 0) {
                                        progressDialog.dismiss();
                                        dialogfun("该邮箱已被注册！");
                                    }
                                    else{
                                        String phone = register_et_phone.getText().toString();
                                        String email = register_et_email.getText().toString();
                                        String password = register_et_password.getText().toString();
                                        User user = new User();
                                        user.setPhone(phone);
                                        user.setEmail(email);
                                        user.setPassword(password);
                                        user.setPartition_id(phone.substring(7,11));
                                        user.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String objectId, BmobException e) {
                                                if(e==null){
                                                    progressDialog.dismiss();
                                                    dialogfun("注册成功！账户ID为："+objectId);
                                                }else{
                                                    progressDialog.dismiss();
                                                    dialogfun("注册失败：" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                                else{
                                    progressDialog.dismiss();
                                    dialogfun("网络连接失败！");
                                }
                            }
                        });
                    }
                }
                else{
                    progressDialog.dismiss();
                    dialogfun("网络连接失败！");
                }
            }
        });


    }

    private void toast(String message){
        Toast.makeText(RegisterActivity.this, message,Toast.LENGTH_SHORT).show();
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

    private void initView(){
        register_et_phone = (EditText) findViewById(R.id.register_et_phone);
        register_et_email = (EditText) findViewById(R.id.register_et_email);
        register_et_password = (EditText) findViewById(R.id.register_et_password);
        register_et_check_password = (EditText) findViewById(R.id.register_et_check_password);
        register_bt_register = (Button) findViewById(R.id.register_bt_register);
        register_tv_back = (TextView) findViewById(R.id.register_tv_back);
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void progressDialog(){
        progressDialog = new ProgressDialog
                (RegisterActivity.this);
        progressDialog.setTitle("Waiting");
        progressDialog.setMessage("正在注册...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
