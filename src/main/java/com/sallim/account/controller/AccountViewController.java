package com.sallim.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class AccountViewController {

    @GetMapping("/categories")
    public String accountPage() {
        return "account/accounts";
    }

}
