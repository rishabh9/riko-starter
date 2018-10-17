package com.github.rishabh9.rikostarter.utilities;

import com.github.rishabh9.riko.upstox.feed.FeedService;
import com.github.rishabh9.riko.upstox.historical.HistoricalService;
import com.github.rishabh9.riko.upstox.users.UserService;
import com.github.rishabh9.riko.upstox.websockets.WebSocketService;
import com.github.rishabh9.rikostarter.exceptions.AuthenticationMissingException;
import com.github.rishabh9.rikostarter.models.UpstoxAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

    @Autowired
    private Cache cache;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
    }

    @Bean
    public FeedService feedService() {
        final UpstoxAuth auth = getUserAuth();
        return new FeedService(auth.getAccessToken(), auth.getApiCredentials());
    }

    @Bean
    public HistoricalService historicalService() {
        final UpstoxAuth auth = getUserAuth();
        return new HistoricalService(auth.getAccessToken(), auth.getApiCredentials());
    }

    @Bean
    public UserService userService() {
        final UpstoxAuth auth = getUserAuth();
        return new UserService(auth.getAccessToken(), auth.getApiCredentials());
    }

    @Bean
    public WebSocketService webSocketService() {
        final UpstoxAuth auth = getUserAuth();
        return new WebSocketService(auth.getAccessToken(), auth.getApiCredentials());
    }

    private UpstoxAuth getUserAuth() {
        final Optional<UpstoxAuth> maybeAuth = cache.get();
        return maybeAuth.orElseThrow(() ->
                new AuthenticationMissingException("User's authentication data is missing!!!"));
    }
}
