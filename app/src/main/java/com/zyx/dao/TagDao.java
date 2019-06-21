package com.zyx.dao;

import android.content.Context;

import com.zyx.bean.Tag;

import java.util.List;

public interface TagDao {
    List<Tag> readTag(Context context);
    void saveTag(List<Tag> tags,Context context);
    int InsertTag(Context context,Tag tag);
}
