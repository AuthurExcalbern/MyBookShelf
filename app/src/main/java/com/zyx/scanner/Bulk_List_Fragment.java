package com.zyx.scanner;
import android.annotation.SuppressLint;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zyx.adpter.BookListAdapter;
import com.zyx.bean.Book;
import com.zyx.bookeshelf.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by jszx on 2018/10/18.
 */

@SuppressLint("ValidFragment")
public class Bulk_List_Fragment extends Fragment {
    ListView lv;
    MyListAdapter bl;
    public static ArrayList<Book> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_batch_list, container, false);
        lv = (ListView)contentView.findViewById(R.id.Bulk_Add_List);

        bl = new MyListAdapter();
        lv.setAdapter(bl);

        return contentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (bl == null) {
                bl = new MyListAdapter();
                lv.setAdapter(bl);
            } else {
                bl.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onStart() {
        Log.d("TAG","onStart");
        super.onStart();
    }

    public class MyListAdapter extends BaseAdapter
    {
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
        public View getView(int position, View converView, ViewGroup parent) {
            if(converView==null)
            {
                converView=getLayoutInflater().inflate(R.layout.book_list_layout,parent,false);
            }
            Book b = (Book)getItem(position);
            TextView name = (TextView)converView.findViewById(R.id.book_name);
            TextView author = (TextView)converView.findViewById(R.id.book_author);
            TextView time = (TextView)converView.findViewById(R.id.book_publish_time);
            name.setText(b.getName());
            author.setText(b.getAuthor());
            time.setText(b.getPublish_year() + "-" + b.getPublish_month());
            return converView;
        }
    }

}