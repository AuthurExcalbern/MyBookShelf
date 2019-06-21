package com.zyx.service;

import android.content.Context;

import com.zyx.bean.Shelf;

import java.util.List;

public interface ShelfService
{
    void SaveShelf(Context context, List<Shelf> shelves);
     List<Shelf> GetShelves(Context context);
     int InsertShelf(Context context,Shelf shelf);
     boolean RenameShelf(Context context,int id,String name);
     boolean DeleteShelf(Context context,int id);
}
