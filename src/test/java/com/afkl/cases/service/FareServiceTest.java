package com.afkl.cases.service;

import com.afkl.cases.df.service.FareService;
import com.afkl.cases.df.service.dto.FareDto;
import com.afkl.cases.df.web.api.WebClientApi;
import com.afkl.cases.df.web.api.model.Coordinates;
import com.afkl.cases.df.web.api.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FareServiceTest {

    private static final FareDto fareDto;

    @Mock
    private WebClientApi webClientApiMock;

    @InjectMocks
    private FareService fareService;

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

        fareDto = new FareDto();
        fareDto.setAmount(1234.4);
        fareDto.setCurrency("usd");
        fareDto.setOrigin(l1);
        fareDto.setDestination(l2);

    }

    @Test
    public void testCalculateFare_Success() {
        Mockito.when(webClientApiMock.calculateFare("", "", "", "")).thenReturn(Mono.just(fareDto));
        Mono<FareDto> result = fareService.calculateFare("", "", "", "");

        assertNotNull(result);
        assertNotNull(result.block());

        FareDto blockedFare = result.block();
        assert blockedFare != null;
        assertEquals("usd", blockedFare.getCurrency());
    }

}
