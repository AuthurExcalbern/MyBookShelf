package com.zyx.dao;

import android.content.Context;

import com.zyx.bean.Book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    private Operator bookOperator = new Operator("Books");

    @Override
    public void saveBook(List<Book> book, Context context) {
        bookOperator.save(context, (Serializable) book);
    }

    @Override
    public List<Book> readBook(Context context) {
        return (List<Book>) bookOperator.load(context);
    }

    @Override
    public int InsertBook(Context context, Book book) {
        List<Book> books = (List<Book>) bookOperator.load(context);
        if(books == null) books = new ArrayList<>();
        if(books.size()!=0){
            book.setId(books.get(books.size()-1).getId()+1);
        }
        else {
            book.setId(1);
        }
        books.add(book);
        bookOperator.save(context, (Serializable) books);
        return book.getId();
    }
}
