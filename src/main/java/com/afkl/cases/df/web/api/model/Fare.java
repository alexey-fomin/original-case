package com.afkl.cases.df.web.api.model;

import lombok.Data;

@Data
public class Fare {

    private Double amount;
    private String currency;
    private String origin;
    private String destination;

}
