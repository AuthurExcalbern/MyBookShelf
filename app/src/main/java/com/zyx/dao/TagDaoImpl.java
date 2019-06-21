package com.zyx.dao;

import android.content.Context;

import com.zyx.bean.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TagDaoImpl implements TagDao {
    private Operator tagOperator = new Operator("Tags");
    @Override
    public List<Tag> readTag(Context context) {
        return (List<Tag>) tagOperator.load(context);
    }

    @Override
    public void saveTag(List<Tag> tags, Context context) {
        tagOperator.save(context, (Serializable) tags);
    }

    @Override
    public int InsertTag(Context context, Tag tag) {
        List<Tag> tags = (List<Tag>) tagOperator.load(context);
        if(tags!=null){
            for(Tag t :tags){
                if(t.getName().equals(tag.getName())){
                    return -1;
                }
            }
            if(tags.size()!=0){
                tag.setId(tags.get(tags.size()-1).getId()+1);
            }
            else {
                tag.setId(1);
            }
        }else {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        tagOperator.save(context, (Serializable) tags);
        return tag.getId();
    }
}
