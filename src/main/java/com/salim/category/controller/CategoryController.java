package com.salim.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
class CategoryController {

    @GetMapping("")
    public String categoryPage() {
        return "category/categories";
    }

}
