package com.afkl.cases.df.service.dto;

import com.afkl.cases.df.web.api.model.Location;
import lombok.Data;

@Data
public class FareDto {

    private double amount;
    private String currency;
    private Location origin;
    private Location destination;

}
