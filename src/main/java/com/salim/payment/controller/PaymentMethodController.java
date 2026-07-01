package com.salim.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment-methods")
class PaymentMethodController {

    @GetMapping("")
    public String dashboardPage() {
        return "payment/payment-methods";
    }

}
