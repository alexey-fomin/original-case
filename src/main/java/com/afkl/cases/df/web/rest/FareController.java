package com.afkl.cases.df.web.rest;

import com.afkl.cases.df.service.FareService;
import com.afkl.cases.df.service.dto.FareDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fares")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @GetMapping("/{origin}/{destination}")
    public Mono<FareDto> calculateFare(@PathVariable("origin") String origin,
                                       @PathVariable("destination") String destination,
                                       @RequestParam("lang") String lang,
                                       @RequestParam("currency") String currency) {
        return fareService.calculateFare(origin, destination, lang, currency);
    }

}
