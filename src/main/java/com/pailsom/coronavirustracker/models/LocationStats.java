package com.pailsom.coronavirustracker.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LocationStats {
    private String state;
    private String country;
    private int lastTotalCases;
    private int diffFromPreDay;
}
