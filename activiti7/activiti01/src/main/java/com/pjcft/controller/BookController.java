package com.pjcft.controller;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */


import com.pjcft.model.Book;
import com.pjcft.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public List<Book> getBooks(){
        List<Book> books = bookService.selectAllBook();
        System.out.println(books.toString());
        return books;
    }
}
