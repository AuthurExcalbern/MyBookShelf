package com.zyx.comparator;

import com.zyx.bean.Book;

import java.text.Collator;
import java.util.Comparator;

public class nameComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return Collator.getInstance(java.util.Locale.CHINA).compare(o1.getName(), o2.getName());
    }
}
