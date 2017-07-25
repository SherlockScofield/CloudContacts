package com.contacts.conan.cloudcontacts.contacts.contactslistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contacts.conan.cloudcontacts.R;
import com.contacts.conan.cloudcontacts.javabean.Contacts;

import java.util.List;

/**
 * Created by Conan on 2016/11/10.
 */

public class ContactsAdapter extends BaseAdapter {

    /*private int resourceId;

    public ContactsAdapter(Context context, int resource, List<Contacts> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contacts contacts = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView contactsPhoto = (ImageView) view.findViewById(R.id.contacts_photo);
        TextView contactsName = (TextView) view.findViewById(R.id.contacts_name);
        contactsPhoto.setImageBitmap(contacts.getPhotoimg());
        contactsName.setText(contacts.getContactsName());
        return view;
    }*/

    private List<Contacts> list;
    private LayoutInflater inflater;

    public ContactsAdapter(Context context, List<Contacts> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_local_contacts_list, null);
            holder.tv_word = (TextView) convertView.findViewById(R.id.tv_contact_word);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_contact_name);
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_contacts_photo) ;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String word = list.get(position).getPinyinheaderWord();
        holder.tv_word.setText(word);
        holder.tv_name.setText(list.get(position).getContactsName());
        holder.iv_photo.setImageBitmap(createCircleImage(list.get(position).getPhotoimg()));//设置圆形头像
        //将相同字母开头的合并在一起
        if (position == 0) {
            //第一个是一定显示的
            holder.tv_word.setVisibility(View.VISIBLE);
        } else {
            //后一个与前一个对比,判断首字母是否相同，相同则隐藏
            String headerWord = list.get(position - 1).getPinyinheaderWord();
            if (word.equals(headerWord)) {
                holder.tv_word.setVisibility(View.GONE);
            } else {
                holder.tv_word.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_word;
        private TextView tv_name;
        private ImageView iv_photo;
    }


    /**
     * 将图片剪裁为圆形
     */
    public static Bitmap createCircleImage(Bitmap source) {
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
