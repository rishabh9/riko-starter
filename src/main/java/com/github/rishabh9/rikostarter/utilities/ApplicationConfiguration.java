package com.github.rishabh9.rikostarter.utilities;

import com.github.rishabh9.riko.upstox.common.UpstoxAuthService;
import com.github.rishabh9.riko.upstox.feed.FeedService;
import com.github.rishabh9.riko.upstox.historical.HistoricalService;
import com.github.rishabh9.riko.upstox.login.LoginService;
import com.github.rishabh9.riko.upstox.orders.OrderService;
import com.github.rishabh9.riko.upstox.users.UserService;
import com.github.rishabh9.riko.upstox.websockets.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

    @Autowired
    private UpstoxAuthService upstoxAuthService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(upstoxAuthService);
    }

    @Bean
    public FeedService feedService() {
        return new FeedService(upstoxAuthService);
    }

    @Bean
    public HistoricalService historicalService() {
        return new HistoricalService(upstoxAuthService);
    }

    @Bean
    public UserService userService() {
        return new UserService(upstoxAuthService);
    }

    @Bean
    public WebSocketService webSocketService() {
        return new WebSocketService(upstoxAuthService);
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(upstoxAuthService);
    }
}
