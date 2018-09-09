package com.vzard.doutu.controller;


import com.vzard.doutu.model.ResponseTemplate;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CounterController {

    AtomicInteger counter = new AtomicInteger();

    @RequestMapping(value = "/counter", method = RequestMethod.GET)
    public ResponseTemplate getCounter() {

        return ResponseTemplate.builder()
                .code(200)
                .msg("Success")
                .data(counter.getAndIncrement())
                .build();
    }


}
