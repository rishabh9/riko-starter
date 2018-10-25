package com.github.rishabh9.rikostarter.utilities;

import com.github.rishabh9.riko.upstox.common.models.ApiCredentials;
import com.github.rishabh9.riko.upstox.login.models.AccessToken;
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

    private final LoadingCache<String, Optional<?>> cache;

    public Cache() {
        final CacheLoader<String, Optional<?>> loader = new CacheLoader<>() {
            @Override
            public Optional<?> load(final String key) {
                // You shouldn't be using this class in the first place.
                // If you are still using this class, you need to put logic here for a headless login
                // and return the authentication details.
                // In doing so, the first time retrieval of auth details from cache will be the slowest,
                // because of the headless login.
                switch (key) {
                    case API_CRED:
                        return Optional.of(new ApiCredentials(API_KEY, API_SECRET));
                    case ACCESS_TOKEN:
                        final AccessToken accessToken = new AccessToken();
                        accessToken.setType(TOKEN_TYPE);
                        accessToken.setExpiresIn(TOKEN_EXPIRY);
                        accessToken.setToken(TOKEN);
                        return Optional.of(accessToken);
                    default:
                        return Optional.empty();
                }

            }
        };
        cache = CacheBuilder.newBuilder().build(loader);
    }

    @SuppressWarnings(value = "unchecked")
    public Optional<ApiCredentials> getApiCredentials() {
        return (Optional<ApiCredentials>) cache.getUnchecked(API_CRED);
    }

    public void updateApiCredentials(final ApiCredentials apiCredentials) {
        cache.put(API_CRED, Optional.ofNullable(apiCredentials));
    }

    @SuppressWarnings(value = "unchecked")
    public Optional<AccessToken> getAccessToken() {
        return (Optional<AccessToken>) cache.getUnchecked(ACCESS_TOKEN);
    }

    public void updateAccessToken(final AccessToken accessToken) {
        cache.put(ACCESS_TOKEN, Optional.ofNullable(accessToken));
    }
}
