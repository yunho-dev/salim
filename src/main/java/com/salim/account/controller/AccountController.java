package com.salim.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
class AccountController {

    @GetMapping("")
    public String categoryPage() {
        return "account/accounts";
    }

}
