package com.afkl.cases.df.web.rest;

import com.afkl.cases.df.service.AirportService;
import com.afkl.cases.df.web.api.model.Location;
import com.afkl.cases.df.web.api.response.PageableResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public Mono<PageableResponse<Location>> getAirports(@RequestParam("lang") String lang,
                                                        @RequestParam("page") Integer page,
                                                        @RequestParam("size") Integer size) {
        return airportService.getAirports(lang, page, size);
    }

    @GetMapping(params = "term")
    public Mono<PageableResponse<Location>> findAirports(@RequestParam("lang") String lang,
                                                         @RequestParam("term") String term,
                                                         @RequestParam(value = "page", defaultValue = "") Integer page,
                                                         @RequestParam(value = "size", defaultValue = "") Integer size) {
        return airportService.findAirports(lang, term, page, size);
    }

}
