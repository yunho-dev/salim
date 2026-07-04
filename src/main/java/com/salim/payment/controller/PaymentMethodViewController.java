package com.salim.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class PaymentMethodViewController {

    @GetMapping("/payment-methods")
    public String dashboardPage() {
        return "payment/payment-methods";
    }

}
