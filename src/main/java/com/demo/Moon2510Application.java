package com.demo;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.awt.*;
import java.net.URI;

/**
 * name: moon-2510
 *
 */
@SpringBootApplication
public class Moon2510Application {
    private static final Logger log = LoggerFactory.getLogger(Moon2510Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Moon2510Application.class, args);
    }


    @Resource
    private Environment environment;


    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            String port = environment.getProperty("local.server.port");
            if (port == null) {
                port = environment.getProperty("server.port", "8080");
            }
            String url = "http://localhost:" + port;
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                log.info("已自动打开浏览器访问：{}", url);

            } else {
                log.warn("当前环境不支持 Desktop，无法自动打开浏览器。请手动访问：{}", url);
            }
        } catch (Exception e) {
            log.error("自动打开浏览器失败：{}", e.getMessage(), e);
        }
    }


}
