package com.zyx.comparator;

import com.zyx.bean.Book;

import java.text.Collator;
import java.util.Comparator;

public class timeComparator implements Comparator<Book> {
    @Override
    public int compare(Book a, Book b){
        int res = Collator.getInstance(java.util.Locale.CHINA).compare(a.getPublish_year(), b.getPublish_year());
        return res != 0 ? res : Collator.getInstance(java.util.Locale.CHINA).compare(a.getPublish_month(), b.getPublish_month());
    }
}
