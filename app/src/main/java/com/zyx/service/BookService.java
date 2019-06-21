package com.zyx.service;

import android.content.Context;

import com.zyx.bean.Book;
import com.zyx.bean.Shelf;
import com.zyx.bean.Tag;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public interface BookService {

    List<Book> SelectByTag(Tag tag, List<Book> books);
    List<Book> SelectByShelf(Shelf shelf, List<Book> books);
    void SaveBooks(Context context,List<Book> books);
    List<Book> GetBooks(Context context);
    List<Book> SearchBook(List<Book> list,String s);
    int InsertBook(Context context,Book book);
    void SortByAuthor(List<Book> books);
    void SortByName(List<Book> books);
    void SortByTime(List<Book> books);
    void SortByPublisher(List<Book> books);
    boolean UpdateBook(Context context,Book book);
    boolean DeleteBook(Context context,int id);
}
