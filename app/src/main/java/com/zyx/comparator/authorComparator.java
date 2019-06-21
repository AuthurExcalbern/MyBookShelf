package com.zyx.comparator;

import com.zyx.bean.Book;

import java.text.Collator;
import java.util.Comparator;

public class authorComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return Collator.getInstance(java.util.Locale.CHINA).compare(o1.getAuthor(), o2.getAuthor());
    }
}
