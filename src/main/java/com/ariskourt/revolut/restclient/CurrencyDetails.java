package com.ariskourt.revolut.restclient;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrencyDetails {

    private JsonNode rates;
    private String base;
    private String date;

}
