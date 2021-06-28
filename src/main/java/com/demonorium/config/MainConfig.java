package com.demonorium.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.demonorium.utils")
@Import({DatabaseConfig.class, WebConfig.class})
public class MainConfig {
}
