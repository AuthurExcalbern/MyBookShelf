package com.zyx.service;

import android.content.Context;

import com.zyx.bean.Shelf;
import com.zyx.bean.Tag;
import com.zyx.dao.TagDao;
import com.zyx.dao.TagDaoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagServiceImpl implements TagService {
    TagDao tagDao = new TagDaoImpl();
    private static  TagServiceImpl instance =null;
    private TagServiceImpl(){}
    public static TagServiceImpl getInstance(){
        if(instance == null)
            instance = new TagServiceImpl();
        return instance;

    }

    @Override
    public void SaveTags(Context context, List<Tag> tags) {
        tagDao.saveTag(tags,context);
    }

    @Override
    public List<Tag> GetTags(Context context) {
        return tagDao.readTag(context);
    }

    @Override
    public int InsertTag(Context context, Tag tag) {
       return tagDao.InsertTag(context,tag);
    }

    @Override
    public boolean RenameTag(Context context, int id, String name) {
        ArrayList<Tag> tags = (ArrayList<Tag>) GetTags(context);
        boolean isRename =false;
        for(Tag t:tags){
            if(t.getId()==id){
                t.setName(name);
                isRename = true;
            }

        }
        SaveTags(context,tags);
        return isRename;
    }

    @Override
    public boolean DeleteTag(Context context, int id) {
        ArrayList<Tag> tags = (ArrayList<Tag>) GetTags(context);
        boolean isDelete =false;
        Iterator<Tag> it = tags.iterator();
        while (it.hasNext()){
            Tag t = it.next();
            if(t.getId() == id){
                it.remove();
                isDelete = true;
            }
        }
        SaveTags(context,tags);
        return isDelete;
    }
}
