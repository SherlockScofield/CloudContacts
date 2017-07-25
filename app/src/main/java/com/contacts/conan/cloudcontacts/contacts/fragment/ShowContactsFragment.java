package com.contacts.conan.cloudcontacts.contacts.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.widget.Toast;

import com.contacts.conan.cloudcontacts.R;
import com.contacts.conan.cloudcontacts.common.Constant;
import com.contacts.conan.cloudcontacts.common.MapUtil;
import com.contacts.conan.cloudcontacts.contacts.LocalContactsDetailActivity;
import com.contacts.conan.cloudcontacts.contacts.ScannerActivity;
import com.contacts.conan.cloudcontacts.contacts.contactslistview.ContactsAdapter;
import com.contacts.conan.cloudcontacts.contacts.contactsutil.GetContactsInfo;
import com.contacts.conan.cloudcontacts.contacts.view.ContactsWordsIndex;
import com.contacts.conan.cloudcontacts.contacts.view.SearchView;
import com.contacts.conan.cloudcontacts.javabean.Contacts;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Conan on 2016/11/2.
 */

public class ShowContactsFragment extends Fragment implements
        ContactsWordsIndex.onWordsChangeListener, AbsListView.OnScrollListener  ,SearchView.SearchViewListener{

    Context mContext = null;

    /**获取库Phone表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID ,
            Phone.CONTACT_STATUS,Phone.LOOKUP_KEY,Phone.PHOTO_FILE_ID,Phone.RAW_CONTACT_ID,Phone.SORT_KEY_PRIMARY};

    /**数据读取序号**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    private static final int PHONES_NUMBER_INDEX = 1;
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    private static final int PHONES_CONTACT_STATUS_INDEX = 4;
    private static final int PHONES_LOOKUP_KEY_INDEX = 5;
    private static final int PHONES_PHOTO_FILE_ID_INDEX = 6;
    private static final int PHONES_RAW_CONTACT_ID_INDEX = 7;

    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;

    TextView common_top_tv_middle_showcontacts;

    private List<Contacts> contactsList = new ArrayList<Contacts>();

    private List<Contacts> contactsListShow = new ArrayList<Contacts>();

    private Handler handler;
    private ListView listView;

    private TextView wordIndexShow;
    private ContactsWordsIndex wordIndex;

    /**     ------      search view -------*/
    /*** 搜索过程中自动补全数据*/
    private List<String> autoCompleteData;
    /*** 自动补全列表adapter*/
    private ArrayAdapter<String> autoCompleteAdapter;
    /*** 搜索view*/
    private SearchView local_contacts_search;

    public SearchView getLocal_contacts_search() {
        return local_contacts_search;
    }


    private int REQUEST_CODE_SCANNER_QRCODE = 0x0000c0de;
    private TextView contact_scaner_qrcode;
    private int MY_PERMISSIONS_REQUEST_CAMERA = 111;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View operateContactsLayout = inflater.inflate(R.layout.activity_main_show_contacts,container,false);
        return operateContactsLayout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();

        initview();

        getContacts();

        getContactsShowList();

        local_contacts_search.setEtInputText("搜索"+contactsListShow.size()+"位联系人");

        ContactsAdapter adapter = new ContactsAdapter(getActivity(),contactsListShow);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getOneContact(contactsListShow.get(position));
            }
        });

        contact_scaner_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().startActivityForResult(,REQUEST_CODE_SCANNER_QRCODE);
                startScanQrcode();

            }
        });

        //搜索结果下拉框隐藏
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                local_contacts_search.getLvTips().setVisibility(View.GONE);
                return false;
            }
        });

        //设置列表点击滑动监听
        handler = new Handler();
        wordIndex.setOnWordsChangeListener(this);

        local_contacts_search.setAutoCompleteAdapter(autoCompleteAdapter);
        //设置监听
        local_contacts_search.setSearchViewListener(this);
    }
    public void initview(){
        common_top_tv_middle_showcontacts = (TextView)getActivity().findViewById(R.id.common_top_tv_middle_showcontacts);
        wordIndexShow = (TextView)getActivity().findViewById(R.id.local_contacts_tv_word_show);
        wordIndex = (ContactsWordsIndex)getActivity().findViewById(R.id.local_contacts_words_index);
        listView = (ListView)getActivity().findViewById(R.id.local_contacts_list_view);
        local_contacts_search = (SearchView)getActivity().findViewById(R.id.local_contacts_search_view);
        contact_scaner_qrcode = (TextView)getActivity().findViewById(R.id.contact_scaner_qrcode);

        common_top_tv_middle_showcontacts.setText(Constant.FRAGMENT_TOPTITLE_SHOWCONTACTS);
        common_top_tv_middle_showcontacts.setTextColor(Color.WHITE);
        common_top_tv_middle_showcontacts.setTextSize(Constant.FRAGMENT_TOPTITLE_SIZE);

        local_contacts_search.setlvTipsId((ListView)getActivity().findViewById(R.id.search_lv_tips));

        //初始化自动补全数据
        getAutoCompleteData(null);
    }
/** --------二维码扫一扫-------*/
    private  void startScanQrcode(){
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else{
            scanQrcode();
        }
    }

    private  void scanQrcode(){
        //new IntentIntegrator(getActivity()).initiateScan();
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("将二维码放入框内，即可自动扫描")//写那句提示的话
                .setOrientationLocked(false)//扫描方向固定
                .setCaptureActivity(ScannerActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
    }

/** -----------二维码扫描结束 ------*/

    //过滤重复的联系人
    private  void getContactsShowList(){
        for(Contacts x : contactsList){
            //Log.i("----contactsList----", "getContactsShowList: "+x.getContactsName()+";    "+x.getPhone()+";    "+x.getContactsId()+";    "+x.getLookupKey());
            boolean flag =true ;
            for(int i = 0;i<contactsListShow.size();i++){
                if(x.getContactsId() == contactsListShow.get(i).getContactsId()){
                    flag = false;
                }
            }
            if(flag){
                contactsListShow.add(x);
            }
        }
    }

    public void getContacts()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else{
            getPhoneContacts();
        }
    }
    private  void getOneContact(Contacts contact) {
        Intent intent = new Intent(getActivity(),LocalContactsDetailActivity.class);
        //intent.putStringArrayListExtra("SORT_KEY",sortkey);
        /*Log.i("showcontact", contact.getContactsId()+"");
        Log.i("showcontact", contact.getContactsName()+"");
        Log.i("showcontact", contact.getPhoto_id()+"");
        Log.i("showcontact", contact.getRawcontactsId()+"");*/
        intent.putExtra("CONTACT_ID",contact.getContactsId());
        intent.putExtra("CONTACT_NAME",contact.getContactsName());
        intent.putExtra("CONTACT_PHOTOID",contact.getPhoto_id());
        intent.putExtra("RAW_CONTACT_ID",contact.getRawcontactsId());
        getActivity().startActivity(intent);
    }

    private  void getPhoneContacts() {
        ContentResolver resolver = mContext.getContentResolver();
            // 获取手机联系人  按SORT_KEY排序
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, Phone.SORT_KEY_PRIMARY);
            if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                String phone = phoneCursor.getString(PHONES_NUMBER_INDEX);
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                //String status = phoneCursor.getString(PHONES_CONTACT_STATUS_INDEX);
                //String lookupkey = phoneCursor.getString(PHONES_LOOKUP_KEY_INDEX);
                //Long photofileid = phoneCursor.getLong(PHONES_PHOTO_FILE_ID_INDEX);
                Long rawcontactid = phoneCursor.getLong(PHONES_RAW_CONTACT_ID_INDEX);

                Bitmap contactPhoto = null;
                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                } else {
                    contactPhoto = BitmapFactory.decodeResource(getResources(), R.mipmap.contacts_photo_default_image);
                }

                Log.e("CONTACTS", "getPhoneContacts: contactName:"+contactName+"phoneNumber     :"+phoneNumber+"    contactid:"+contactid+"  photoid:"+photoid+"  rawcontactid:"+rawcontactid);
                /*mContactsName.add(contactName);
                mContactsNumber.add(phoneNumber);
                mContactsPhonto.add(contactPhoto);*/
                Contacts contacts = new Contacts(contactName,phone,contactid,contactPhoto,photoid,rawcontactid);
                contactsList.add(contacts);
            }

            phoneCursor.close();
        }
    }


    /**-------------------------读写联系人权限 、访问相机权限 用户接受或拒绝处理逻辑判断------------------------------------------*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        //读写联系人权限
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoneContacts();
            } else{
                // Permission Denied
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        //访问相机权限
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                scanQrcode();
            } else{
                // Permission Denied
                Toast.makeText(getActivity(), "MY_PERMISSIONS_REQUEST_CAMERA Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

/**--------------------------右侧字母索引-----------------------------------------*/
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //当滑动列表的时候，更新右侧字母列表的选中状态
        wordIndex.setTouchIndex(contactsListShow.get(firstVisibleItem).getPinyinheaderWord());
    }

    @Override
    public void wordsChange(String words) {
        updateWord(words);
        updateListView(words);
    }
    /**
     * 更新中央的字母提示
     *
     * @param words 首字母
     */
    private void updateWord(String words) {
        wordIndexShow.setText(words);
        wordIndexShow.setVisibility(View.VISIBLE);
        //清空之前的所有消息
        handler.removeCallbacksAndMessages(null);
        //1s后让tv隐藏
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wordIndexShow.setVisibility(View.GONE);
            }
        }, 500);
    }
    /**
     * @param words 首字母
     */
    private void updateListView(String words) {
        for (int i = 0; i < contactsListShow.size(); i++) {
            String headerWord = contactsListShow.get(i).getPinyinheaderWord();
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(headerWord)) {
                //将列表选中哪一个
                listView.setSelection(i);
                //找到开头的一个即可
                return;
            }
        }
    }


/**---------------------------------搜索---------------------------------*/
    @Override
    public void onRefreshAutoComplete(String text) {
        getAutoCompleteData(text);
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>();
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0; i < contactsListShow.size(); i++) {
                CharSequence textinput = text.trim();
                if (contactsListShow.get(i).getContactsName().contains(textinput)) {
                    autoCompleteData.add(contactsListShow.get(i).getContactsName());
                }
                else if (contactsListShow.get(i).getPinyin().contains(textinput.toString().toLowerCase()) || contactsListShow.get(i).getPinyin().contains(textinput.toString().toUpperCase())) {
                    autoCompleteData.add(contactsListShow.get(i).getContactsName());
                }
                else if (contactsListShow.get(i).getPhone().contains(textinput) ) {
                    autoCompleteData.add(contactsListShow.get(i).getContactsName());
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }
}
