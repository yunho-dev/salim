package com.salim.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class CategoryViewController {

    @GetMapping("/categories")
    public String categoryPage() {
        return "category/categories";
    }

}
