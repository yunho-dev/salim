package com.sallim.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class PaymentMethodViewController {

    @GetMapping("/payment-methods")
    public String paymentMethodPage() {
        return "payment/payment-methods";
    }

}
