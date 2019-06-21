package com.zyx.dao;

import android.content.Context;

import com.zyx.bean.Shelf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShelfDaoImpl implements ShelfDao {

    private Operator shelfOperator = new Operator("Shelfs");

    @Override
    public void saveShelf(List<Shelf> shelf, Context context) {
        shelfOperator.save(context, (Serializable) shelf);
    }

    @Override
    public List<Shelf> readShelf(Context context) {
        return (List<Shelf>) shelfOperator.load(context);
    }

    @Override
    public int InsertShelf(Context context, Shelf shelf) {
        List<Shelf> shelves = (List<Shelf>) shelfOperator.load(context);
        if (shelves == null) shelves = new ArrayList<>();
        if(shelves.size()!=0){
            shelf.setId(shelves.get(shelves.size()-1).getId()+1);
        }
        else {
            shelf.setId(1);
        }
        shelves.add(shelf);
        shelfOperator.save(context, (Serializable) shelves);
        return  shelf.getId();
    }
}
