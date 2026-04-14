package com.zacharyab24.playground.ejb;

import jakarta.ejb.Stateless;

@Stateless
public class HealthService {

    public String getStatus() {
        return "your mum ok";
    }
}