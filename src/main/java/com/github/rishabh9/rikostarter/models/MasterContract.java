package com.github.rishabh9.rikostarter.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MasterContract {

    private String exchange;
    private String token;
    private String parentToken;
    private String symbol;
    private String name;
    private BigDecimal closingPrice;
    private String expiry;
    private BigDecimal strikePrice;
    private float tickSize;
    private int lotSize;
    private String instrumentType;
    private String isin;

}
