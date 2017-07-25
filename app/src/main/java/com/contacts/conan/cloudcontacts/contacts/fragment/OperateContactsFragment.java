package com.contacts.conan.cloudcontacts.contacts.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.conan.cloudcontacts.R;
import com.contacts.conan.cloudcontacts.common.Constant;

/**
 * Created by Conan on 2016/11/2.
 */

public class OperateContactsFragment extends Fragment implements View.OnClickListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View operateContactsLayout = inflater.inflate(R.layout.activity_main_operate_contacts,container,false);
        return operateContactsLayout;
    }

    public void setTopTitle(){
       // TextView topTitle = (TextView) f
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView common_top_tv_middle_opreatecontacts = (TextView)getActivity().findViewById(R.id.common_top_tv_middle_opreatecontacts);
        common_top_tv_middle_opreatecontacts.setText(Constant.FRAGMENT_TOPTITLE_OPERATECONTACTS);
        common_top_tv_middle_opreatecontacts.setTextColor(Color.WHITE);
        common_top_tv_middle_opreatecontacts.setTextSize(Constant.FRAGMENT_TOPTITLE_SIZE);

        LinearLayout operate_contacts_ll_cloudcontacts = (LinearLayout)getActivity().findViewById(R.id.operate_contacts_ll_cloudcontacts);
        LinearLayout operate_contacts_ll_localcontacts = (LinearLayout)getActivity().findViewById(R.id.operate_contacts_ll_localcontacts);
        LinearLayout operate_contacts_ll_cloudylocaln = (LinearLayout)getActivity().findViewById(R.id.operate_contacts_ll_cloudylocaln);
        LinearLayout operate_contacts_ll_cloudnlocaly = (LinearLayout)getActivity().findViewById(R.id.operate_contacts_ll_cloudnlocaly);
        LinearLayout operate_contacts_ll_mergesyn = (LinearLayout)getActivity().findViewById(R.id.operate_contacts_ll_mergesyn);
        LinearLayout operate_contacts_ll_onesyn = (LinearLayout)getActivity().findViewById(R.id.operate_contacts_ll_onesyn);

        operate_contacts_ll_cloudcontacts.setOnClickListener(this);
        operate_contacts_ll_localcontacts.setOnClickListener(this);
        operate_contacts_ll_cloudylocaln.setOnClickListener(this);
        operate_contacts_ll_cloudnlocaly.setOnClickListener(this);
        operate_contacts_ll_mergesyn.setOnClickListener(this);
        operate_contacts_ll_onesyn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.operate_contacts_ll_cloudcontacts:
                Toast.makeText(getActivity(),"云端联系人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.operate_contacts_ll_localcontacts:
                Toast.makeText(getActivity(),"本地联系人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.operate_contacts_ll_cloudylocaln:
                Toast.makeText(getActivity(),"云端有本地无",Toast.LENGTH_SHORT).show();
                break;
            case R.id.operate_contacts_ll_cloudnlocaly:
                Toast.makeText(getActivity(),"本地有云端无",Toast.LENGTH_SHORT).show();
                break;
            case R.id.operate_contacts_ll_mergesyn:
                Toast.makeText(getActivity(),"融合同步",Toast.LENGTH_SHORT).show();
                break;
            case R.id.operate_contacts_ll_onesyn:
                Toast.makeText(getActivity(),"一端同步",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
