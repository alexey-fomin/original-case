package com.afkl.cases.service;

import com.afkl.cases.df.service.AirportService;
import com.afkl.cases.df.web.api.WebClientApi;
import com.afkl.cases.df.web.api.model.Coordinates;
import com.afkl.cases.df.web.api.model.Location;
import com.afkl.cases.df.web.api.response.PageableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

    private static final PageableResponse<Location> locationPageableResponse;

    @Mock
    private WebClientApi webClientApiMock;

    @InjectMocks
    private AirportService airportService;

    static {
        Location l1 = new Location();
        l1.setCode("AAA");
        l1.setCoordinates(new Coordinates(123123.2, 1232.56));
        l1.setDescription("DDDD");
        l1.setName("AAA");
        l1.setParent(null);

        Location l2 = new Location();
        l2.setCode("BBB");
        l2.setCoordinates(new Coordinates(123123.2, 1232.56));
        l2.setDescription("AAA");
        l2.setName("BBB");
        l2.setParent(null);

        List<Location> locations = new ArrayList<>();
        locations.add(l1);
        locations.add(l2);

        PageableResponse<Location> pageableResponse = new PageableResponse<>();
        pageableResponse.setPage(new PageableResponse.PageMetadata(10, 1, 2, 1));
        pageableResponse.set_embedded(new PageableResponse.CustomEmbedded(locations));

        locationPageableResponse = pageableResponse;
    }

    @Test
    public void testFindAirports_Success() {

        Mockito.when(webClientApiMock.findLocations("", "", 0, 0)).thenReturn(Mono.just(locationPageableResponse));

        Mono<PageableResponse<Location>> result = airportService.findAirports("", "", 1, 10);

        assertNotNull(result);
        assertNotNull(result.block());

        PageableResponse<Location> blockedPageable = result.block();

        assert blockedPageable != null;
        Assertions.assertEquals("AAA", blockedPageable.get_embedded().locations().get(0).getCode());
    }

}
