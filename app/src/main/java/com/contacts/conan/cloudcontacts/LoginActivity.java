package com.contacts.conan.cloudcontacts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.contacts.conan.cloudcontacts.common.ActivityCollector;
import com.contacts.conan.cloudcontacts.contacts.MainContactsActivity;
import com.contacts.conan.cloudcontacts.javabean.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 *  @author zhuwenjun
 *  @date 2016-10-30 15:12:01
 *  @version 1.0
 *  @see "用户登录界面"
 */
public class LoginActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor speditor;
    private EditText editText_username;
    private EditText editText_password;
    private CheckBox checkBox_rememberpwd;
    private CheckBox checkBox_loginself;
    private Button button_login;
    private Button button_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this, "5d027b2b46c6421f2955b75ead3f4394");

        initview();

        login_before();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickloginbutton();
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickregisterbutton();
            }
        });
    }

    private void initview(){
        editText_username = (EditText)findViewById(R.id.login_et_username);
        editText_password = (EditText)findViewById(R.id.login_et_password);
        checkBox_rememberpwd = (CheckBox)findViewById(R.id.login_cb_rememberpwd);
        checkBox_loginself = (CheckBox)findViewById(R.id.login_cb_loginself);
        button_login = (Button) findViewById(R.id.login_bt_login);
        button_register = (Button) findViewById(R.id.login_bt_register);
    }

    private boolean checkdata(){
        if(TextUtils.isEmpty(editText_username.getText())){
            dialogfun("账号不能为空!");
            return false;
        }
        if(TextUtils.isEmpty(editText_password.getText())){
            dialogfun("密码不能为空!");
            return false;
        }
        return true;
    }

    /**
     * 用户登录
     */
    private void clickloginbutton(){
        if(checkdata()){
            String phone = editText_username.getText().toString();
            String password = editText_password.getText().toString();
            progressDialog();
            BmobQuery<User> query = new BmobQuery<User>();
            query.addWhereEqualTo("phone",phone);
            query.addWhereEqualTo("password",password);
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {

                    if(e==null) {
                        if (list.size() > 0) {
                            login_success();
                        }
                        else{
                            String email = editText_username.getText().toString();
                            String password = editText_password.getText().toString();
                            BmobQuery<User> query = new BmobQuery<User>();
                            query.addWhereEqualTo("email",email);
                            query.addWhereEqualTo("password",password);
                            query.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                    if(e==null) {
                                        if (list.size() > 0) {
                                            login_success();
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            dialogfun("账户或密码错误");
                                        }
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "登录失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void login_before(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRememberpwd = sharedPreferences.getBoolean("remember_password",false);
        if (isRememberpwd){
            String username = sharedPreferences.getString("user_name","");
            String password = sharedPreferences.getString("password","");
            boolean isloginself = sharedPreferences.getBoolean("login_self",false);
            editText_username.setText(username);
            editText_password.setText(password);
            checkBox_rememberpwd.setChecked(true);
            if(isloginself){
                checkBox_loginself.setChecked(true);
            }
        }
    }

    private void login_success(){
        progressDialog.dismiss();

        speditor = sharedPreferences.edit();
        String username = editText_username.getText().toString();
        String password = editText_password.getText().toString();
        if (checkBox_rememberpwd.isChecked()){
            speditor.putString("user_name",username);
            speditor.putString("password",password);
            speditor.putBoolean("remember_password",true);
            if(checkBox_loginself.isChecked()){
                speditor.putBoolean("login_self",true);
            }else{
                speditor.putBoolean("login_self",false);
            }
        }
        else{
            speditor.putString("user_name_once",editText_username.getText().toString());
            speditor.putString("password_once",editText_password.getText().toString());
            speditor.putBoolean("remember_password",false);
            speditor.putBoolean("login_self",false);
        }
        speditor.commit();
        Intent loginintent = new Intent(LoginActivity.this,MainContactsActivity.class);
        startActivity(loginintent);
    }

    /**
     * 用户注册
     */
    private void clickregisterbutton(){
        Intent registerintent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerintent);
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

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void progressDialog(){
        progressDialog = new ProgressDialog
                (LoginActivity.this);
        progressDialog.setTitle("Waiting");
        progressDialog.setMessage("正在登录...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
