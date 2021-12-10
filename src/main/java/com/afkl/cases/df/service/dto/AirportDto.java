package com.afkl.cases.df.service.dto;

import com.afkl.cases.df.web.api.model.Coordinates;
import com.afkl.cases.df.web.api.model.Location;
import lombok.Data;

@Data
public class AirportDto {

    private String code;
    private String name;
    private String description;
    private Coordinates coordinates;
    private Location parent;

}
