package com.pjcft.service.impl;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */

import com.pjcft.mapper.mysql.BookMapper;
import com.pjcft.model.Book;
import com.pjcft.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookMapper bookMapper;


    @Override
    public List<Book> selectAllBook() {
        return bookMapper.selectAllBook();
    }
}
