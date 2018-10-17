package com.github.rishabh9.rikostarter.utilities;

import com.github.rishabh9.riko.upstox.common.models.ApiCredentials;
import com.github.rishabh9.riko.upstox.login.models.AccessToken;
import com.github.rishabh9.rikostarter.models.UpstoxAuth;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.github.rishabh9.rikostarter.constants.RikoStarterConstants.*;

/**
 * Using this cache to represent storage of token into the database.
 * Storing tokens into a database is the correct thing to do.
 * This class is for demonstration of this starter project only.
 */
@Component
public class Cache {

    private final LoadingCache<String, Optional<UpstoxAuth>> cache;

    public Cache() {
        final CacheLoader<String, Optional<UpstoxAuth>> loader = new CacheLoader<>() {
            @Override
            public Optional<UpstoxAuth> load(final String key) {
                // You shouldn't be using this class in the first place.
                // If you are still using this class, you need to put logic here for a headless login
                // and return the authentication details as UpstoxAuth.
                // In doing so, the first time retrieval of auth details from cache will be the slowest,
                // because of the headless login.
                final ApiCredentials credentials = new ApiCredentials(API_KEY, API_SECRET);
                final AccessToken accessToken = new AccessToken();
                accessToken.setType(TOKEN_TYPE);
                accessToken.setExpiresIn(TOKEN_EXPIRY);
                accessToken.setToken(TOKEN);
                final UpstoxAuth auth = new UpstoxAuth();
                auth.setAccessToken(accessToken);
                auth.setApiCredentials(credentials);
                return Optional.of(auth);
            }
        };
        cache = CacheBuilder.newBuilder().build(loader);
    }

    public Optional<UpstoxAuth> get() {
        return cache.getUnchecked(USER_AUTH);
    }

    public void put(final UpstoxAuth upstoxAuth) {
        cache.put(USER_AUTH, Optional.ofNullable(upstoxAuth));
    }
}
