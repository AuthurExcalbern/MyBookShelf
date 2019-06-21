package com.zyx.service;

import android.content.Context;

import com.zyx.bean.Shelf;
import com.zyx.bean.Tag;

import java.util.List;

public interface TagService {
    void SaveTags(Context context, List<Tag> tags);
    List<Tag> GetTags(Context context);
    int InsertTag(Context context,Tag tag);
    boolean RenameTag(Context context,int id,String name);
    boolean DeleteTag(Context context,int id);
}
