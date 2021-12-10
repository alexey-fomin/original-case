package com.afkl.cases.df.web.api.model;

import lombok.Data;

@Data
public class Location {

    private String code;
    private String name;
    private String description;
    private Coordinates coordinates;
    private Location parent;

}
