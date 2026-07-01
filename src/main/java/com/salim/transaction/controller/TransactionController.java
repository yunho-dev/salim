package com.salim.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transactions")
class TransactionController {

    @GetMapping("")
    public String dashboardPage() {
        return "transaction/transactions";
    }

}
