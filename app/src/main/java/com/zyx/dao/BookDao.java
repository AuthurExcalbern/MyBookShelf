package com.zyx.dao;

import android.content.Context;

import com.zyx.bean.Book;

import java.util.List;

public interface BookDao {
    void saveBook(List<Book> book, Context context);
    List<Book> readBook(Context context);
    int InsertBook(Context context,Book book);
}
