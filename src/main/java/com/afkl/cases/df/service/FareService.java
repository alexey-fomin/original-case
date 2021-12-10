package com.afkl.cases.df.service;

import com.afkl.cases.df.service.dto.FareDto;
import com.afkl.cases.df.web.api.WebClientApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FareService {

    private final WebClientApi clientApi;

    public FareService(WebClientApi clientApi) {
        this.clientApi = clientApi;
    }

    public Mono<FareDto> calculateFare(String origin, String destination, String lang, String currency) {
        return clientApi.calculateFare(origin, destination, lang, currency);
    }

}
