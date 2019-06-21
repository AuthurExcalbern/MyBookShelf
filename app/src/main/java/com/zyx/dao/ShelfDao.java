package com.zyx.dao;

import android.content.Context;

import com.zyx.bean.Shelf;

import java.util.List;

public interface ShelfDao {
    void saveShelf(List<Shelf> shelf, Context context);
    List<Shelf> readShelf(Context context);
    int InsertShelf(Context context,Shelf shelf);
}
