package com.demo.service;

import com.demo.controller.ws.WishWebSocket;
import com.demo.dao.WishRepository;
import com.demo.entity.Wish;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class WishService {

    @Resource
    private WishRepository repository;

    public WishService(WishRepository repository) {
        this.repository = repository;
    }

    // 1. 提交愿望（含祝福生成 + 保存 + 广播）
    public Wish submitWish(String content) {
        String blessing = generateBlessing(content);
        Wish wish = new Wish(content, blessing);
        Wish saved = repository.save(wish);
        WishWebSocket.broadcast(saved);
        return saved;
    }

    // 2. 生成祝福（模拟 I/O 阻塞，体现虚拟线程价值）
    private String generateBlessing(String wish) {
        try {
            // 模拟外部调用
            Thread.sleep(100);
            String[] templates = {
                    "🌕 月神回应：%s —— 心想事成！",
                    "🌙 嫦娥传音：%s —— 万事顺遂！",
                    "✨ 玉兔捎话：%s —— 平安喜乐！",
                    "🎑 桂花飘香：%s —— 梦想成真！"
            };
            return String.format(templates[new Random().nextInt(templates.length)], wish);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    // 3. 获取最近10条（用于页面初始化）
    public List<Wish> getRecentWishes() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }
}