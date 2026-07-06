package com.sallim.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardViewController {

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard/dashboard";
    }
}
