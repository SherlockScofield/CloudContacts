package com.contacts.conan.cloudcontacts.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.conan.cloudcontacts.R;
import com.contacts.conan.cloudcontacts.common.ActivityCollector;
import com.contacts.conan.cloudcontacts.common.Constant;
import com.contacts.conan.cloudcontacts.contacts.contactsutil.RGBLuminanceSource;
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
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * Created by Conan on 2016/12/14.
 */

public class ScannerActivity extends Activity {

    DecoratedBarcodeView mDBV;
    private CaptureManager captureManager;

    private TextView back;
    private TextView more;

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        mDBV= (DecoratedBarcodeView) findViewById(R.id.scanner_box);
        back = (TextView) findViewById(R.id.scanner_top_tv_left);
        more = (TextView) findViewById(R.id.scanner_top_tv_right);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishOneActivity(ScannerActivity.this);
            }
        });

        //选择相册中的二维码
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //startActivityForResult(intent,2);
                Intent wrapperIntent = Intent.createChooser(intent, "选择二维码图片");
                ScannerActivity.this.startActivityForResult(wrapperIntent, Constant.IntentRqcode.REQUEST_CODE_SCANNER_QRIMAGE);

            }
        });

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //图片二维码识别
        if(requestCode == Constant.IntentRqcode.REQUEST_CODE_SCANNER_QRIMAGE){
            ScannerQRImageResult(data);
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

    private void ScannerQRImageResult(Intent data){
        Bitmap bitmap = null;
        try{
            Uri uri = data.getData();
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        }catch (Exception e){
            Toast.makeText(this, "Exception",Toast.LENGTH_SHORT).show();
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Intent intentdata = new Intent();
        try {
            Result result = reader.decode(bitmap1, hints);
            if (result != null) {
                String recode = recode(result.toString());
                intentdata.putExtra("result", recode);
                setResult(Constant.IntentRqcode.RESULT_CODE_SCANNER_QRIMAGE_BACK,intentdata);
                finish();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            intentdata.putExtra("result", "无法识别该图片");
            setResult(Constant.IntentRqcode.RESULT_CODE_SCANNER_QRIMAGE_BACK,intentdata);
            finish();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }

    }

    private String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }


}
