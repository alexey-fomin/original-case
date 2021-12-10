package com.afkl.cases.df.service;

import com.afkl.cases.df.web.api.WebClientApi;
import com.afkl.cases.df.web.api.model.Location;
import com.afkl.cases.df.web.api.response.PageableResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AirportService {

    private final WebClientApi clientApi;

    public AirportService(WebClientApi clientApi) {
        this.clientApi = clientApi;
    }

    public Mono<PageableResponse<Location>> findAirports(String lang, String term, Integer page, Integer size) {
        return clientApi.findLocations(lang, term, page, size);
    }

    public Mono<PageableResponse<Location>> getAirports(String lang, Integer page, Integer size) {
        return clientApi.getLocations(lang, page, size);
    }

}
