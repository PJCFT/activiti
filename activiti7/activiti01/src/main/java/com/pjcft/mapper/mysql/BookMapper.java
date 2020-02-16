package com.pjcft.mapper.mysql;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */

import com.pjcft.model.Book;

import java.util.List;

public interface BookMapper {

    public List<Book> selectAllBook();
}
