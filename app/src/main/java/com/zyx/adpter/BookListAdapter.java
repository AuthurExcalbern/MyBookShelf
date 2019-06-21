package com.zyx.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyx.bean.Book;
import com.zyx.bookeshelf.R;

import java.util.ArrayList;
import java.util.List;


public class BookListAdapter extends BaseAdapter {
    private List<View> itemViews = new ArrayList<>();
    public BookListAdapter(ArrayList<Book> books,Context context){
        for (Book b:books) {
            itemViews.add(this.makeItemView(b,context));
        }
    }
    @Override
    public int getCount() {
        return itemViews.size();
    }

    @Override
    public Object getItem(int position) {
        return itemViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return itemViews.get(position);
    }

    public void updateView(List<Book> books,Context context){
        this.clearAll();
        for (Book b:books) {
            itemViews.add(this.makeItemView(b,context));
        }
        this.notifyDataSetChanged();
    }

    public View makeItemView(Book book,Context context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.book_list_layout, null);
        ImageView cover = itemView.findViewById(R.id.book_cover);
        TextView name = itemView.findViewById(R.id.book_name);
        name.setText(book.getName());
        TextView publishTime = itemView.findViewById(R.id.book_publish_time);
        TextView bookAuthor = itemView.findViewById(R.id.book_author);
        if(book.getAuthor()!="" && book.getPublisher()!=""){
            bookAuthor.setText(book.getAuthor()+"著，"+book.getPublisher());
        }else if (book.getPublisher()==""){
            bookAuthor.setText(book.getAuthor());
        }else {
            bookAuthor.setText(book.getPublisher());
        }
        publishTime.setText(book.getPublish_year()+"-"+book.getPublish_month());
        String imgid = String.valueOf(book.getCover()).equals("")?R.drawable.book1_pic+"":String.valueOf(book.getCover());
        int imgResId = Integer.parseInt(imgid);
        cover.setImageResource(imgResId);
        return itemView;
    }
    private void clearAll(){
        itemViews.clear();
    }

}
