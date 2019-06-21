package com.zyx.service;

import android.content.Context;

import com.zyx.bean.Shelf;
import com.zyx.dao.ShelfDao;
import com.zyx.dao.ShelfDaoImpl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShelfServiceImpl implements ShelfService {

    ShelfDao shelfDao = new ShelfDaoImpl();
    private static ShelfServiceImpl instance = null;
    private ShelfServiceImpl(){}
    public static ShelfServiceImpl getInstance(){
        if(instance == null)
            instance = new ShelfServiceImpl();
        return instance;
    }
    @Override
    public void SaveShelf(Context context, List<Shelf> shelves) {
        shelfDao.saveShelf(shelves,context);
    }

    @Override
    public List<Shelf> GetShelves(Context context) {
        return shelfDao.readShelf(context);
    }

    @Override
    public int InsertShelf(Context context, Shelf shelf) {
       return shelfDao.InsertShelf(context,shelf);
    }

    @Override
    public boolean RenameShelf(Context context, int id,String name) {
        ArrayList<Shelf>  shelves = (ArrayList<Shelf>) GetShelves(context);
        boolean isRename = false;
        for (Shelf s: shelves) {
            if(s.getId()==id) {
                s.setName(name);
                isRename =true;
            }
        }
        SaveShelf(context,shelves);
        return isRename;
    }

    @Override
    public boolean DeleteShelf(Context context, int id) {
        ArrayList<Shelf> shelves = (ArrayList<Shelf>) GetShelves(context);
        boolean isDelete =false;
        Iterator<Shelf> it = shelves.iterator();
        while (it.hasNext()){
            Shelf s = it.next();
            if(s.getId() == id){
                it.remove();
                isDelete = true;
            }
        }
        SaveShelf(context,shelves);
        return isDelete;
    }
}
