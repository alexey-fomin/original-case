package com.afkl.cases.df.web.api;

import com.afkl.cases.df.service.dto.FareDto;
import com.afkl.cases.df.web.api.model.Fare;
import com.afkl.cases.df.web.api.model.Location;
import com.afkl.cases.df.web.api.response.OAuth2TokenResponse;
import com.afkl.cases.df.web.api.response.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Component
@Slf4j
public class WebClientApi {

    private static final String COLON = ":";
    private static final String BASIC_TOKEN_TYPE = "Basic";
    private static final String GRANT_TYPE_FORM_FIELD_NAME = "grant_type";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Value("${service.oauth2.client-id}")
    private String clientId;

    @Value("${service.oauth2.client-secret}")
    private String clientSecret;

    @Value("${service.oauth2.grant-type}")
    private String grantType;

    @Value("${service.api.mock-api-url}")
    private String mockApiUrl;

    private final WebClient webClient;

    public WebClientApi() {
        this.webClient = WebClient.create("http://localhost:8080");
    }

    public Mono<PageableResponse<Location>> getLocations(String lang, Integer page, Integer size) {
        return getOAuth2Token().flatMap(auth2TokenResponse ->
                webClient.get().uri(uriBuilder -> uriBuilder
                                .path("/airports")
                                .queryParam("lang", lang)
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .build())
                        .header(AUTHORIZATION, String.format("%s %s", BEARER_TOKEN_TYPE, auth2TokenResponse.getAccessToken()))
                        .exchangeToMono(resp -> {
                            ParameterizedTypeReference<PageableResponse<Location>> type = new ParameterizedTypeReference<>() {
                            };
                            return resp.bodyToMono(type);
                        })
        ).doOnError(err -> log.error(err.getMessage()));
    }

    public Mono<PageableResponse<Location>> findLocations(String lang, String term, Integer page, Integer size) {
        return getOAuth2Token().flatMap(auth2TokenResponse ->
                        webClient.get().uri(uriBuilder -> uriBuilder
                                        .path("/airports")
                                        .queryParam("lang", lang)
                                        .queryParam("term", term)
                                        .queryParamIfPresent("page", Optional.ofNullable(page))
                                        .queryParamIfPresent("size", Optional.ofNullable(size))
                                        .build())
                                .header(AUTHORIZATION, String.format("%s %s", BEARER_TOKEN_TYPE, auth2TokenResponse.getAccessToken()))
                                .exchangeToMono(resp -> {
                                    ParameterizedTypeReference<PageableResponse<Location>> type = new ParameterizedTypeReference<>() {
                                    };
                                    return resp.bodyToMono(type);
                                })
                )
                .doOnError(err -> log.error(err.getMessage()));
    }

    public Mono<Location> getLocation(String lang, String key, String token) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path(String.join("", "/airports/", key))
                        .queryParam("lang", lang)
                        .build())
                .header(AUTHORIZATION, String.format("%s %s", BEARER_TOKEN_TYPE, token))
                .exchangeToMono(resp -> resp.bodyToMono(Location.class))
                .doOnError(err -> log.error(err.getMessage()));
    }

    // I'm crying ;_;
    public Mono<FareDto> calculateFare(String origin, String destination, String lang, String currency) {
        return getOAuth2Token().flatMap(auth2TokenResponse -> {
                            Mono<Fare> fareMono = getFareMono(auth2TokenResponse.getAccessToken(), origin, destination, currency);
                            Mono<Location> originMono = getLocation(lang, origin, auth2TokenResponse.getAccessToken());
                            Mono<Location> destinationMono = getLocation(lang, destination, auth2TokenResponse.getAccessToken());
                            return Mono.zip(fareMono, originMono, destinationMono);
                        }
                ).flatMap(tuple -> {
                    // TODO Add mapper
                    Fare fare = tuple.getT1();
                    Location originLocation = tuple.getT2();
                    Location destinationLocation = tuple.getT3();
                    FareDto fareDto = new FareDto();
                    fareDto.setAmount(fare.getAmount());
                    fareDto.setCurrency(fare.getCurrency());
                    fareDto.setOrigin(originLocation);
                    fareDto.setDestination(destinationLocation);
                    return Mono.just(fareDto);
                })
                .doOnError(err -> log.error(err.getMessage()));
    }

    private Mono<Fare> getFareMono(String token, String origin, String destination, String currency) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path(String.join("", "/fares/", origin, "/", destination))
                        .queryParam("currency", currency)
                        .build())
                .header(AUTHORIZATION, String.format("%s %s", BEARER_TOKEN_TYPE, token))
                .exchangeToMono(resp -> resp.bodyToMono(Fare.class));
    }

    public Mono<OAuth2TokenResponse> getOAuth2Token() {
        return webClient
                .post()
                .uri(mockApiUrl + "/oauth/token")
                .header(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(AUTHORIZATION, encodeBasicCreds())
                .body(BodyInserters.fromFormData(GRANT_TYPE_FORM_FIELD_NAME, grantType))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(OAuth2TokenResponse.class);
                    }
                    return clientResponse.createException().flatMap(Mono::error);
                })
                .doOnError(err -> log.error(err.getMessage()));
    }

    private String encodeBasicCreds() {
        final String plainCreds = clientId + COLON + clientSecret;
        return String.format("%s %s", BASIC_TOKEN_TYPE, Base64.getEncoder().encodeToString(plainCreds.getBytes()));
    }

}
