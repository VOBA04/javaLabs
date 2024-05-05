package com.vova.laba.controller;

import com.vova.laba.service.RequestCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/counter")
public class RequestCounterController {
  private RequestCounterService requestCounterService;

  @Autowired
  public RequestCounterController(RequestCounterService requestCounterService) {
    this.requestCounterService = requestCounterService;
  }

  @GetMapping
  public ResponseEntity<Integer> getRequestCount() {
    return ResponseEntity.ok(requestCounterService.getCount());
  }
}
