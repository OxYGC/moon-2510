package com.demo.controller;

import com.demo.entity.Wish;
import com.demo.service.WishService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class WishController {

    @Resource
    private WishService wishService;
    private static final Logger log = LoggerFactory.getLogger(WishController.class);


    @PostMapping("/wish")
    public Wish makeWish(@RequestBody Map<String, String> payload) {
        // 打印当前线程信息
        Thread current = Thread.currentThread();
        log.info("""
            🟢 处理请求的线程: {}
               - 名称: {}
               - 是否虚拟线程: {}
               - 线程 ID: {}
            """,
                current,
                current.getName(),
                current.isVirtual(),
                current.threadId());


        return wishService.submitWish(payload.get("content"));
    }

    @GetMapping("/wishes/recent")
    public Object recent() {
        return wishService.getRecentWishes();
    }
}