package com.vova.laba.serviceimpl;

import com.vova.laba.service.RequestCounterService;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class RequestCounterServiceImpl implements RequestCounterService {
  private AtomicInteger count = new AtomicInteger(0);

  @Override
  public void increment() {
    count.incrementAndGet();
  }

  @Override
  public Integer getCount() {
    return count.get();
  }
}
