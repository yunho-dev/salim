package com.salim.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class TransactionViewController {

    @GetMapping("/transactions")
    public String dashboardPage() {
        return "transaction/transactions";
    }

}
