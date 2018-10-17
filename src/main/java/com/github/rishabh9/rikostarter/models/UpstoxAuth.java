package com.github.rishabh9.rikostarter.models;

import com.github.rishabh9.riko.upstox.common.models.ApiCredentials;
import com.github.rishabh9.riko.upstox.login.models.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UpstoxAuth {

    private AccessToken accessToken;
    private ApiCredentials apiCredentials;
}
