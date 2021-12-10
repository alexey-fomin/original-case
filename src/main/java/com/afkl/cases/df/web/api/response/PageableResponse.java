package com.afkl.cases.df.web.api.response;

import com.afkl.cases.df.web.api.model.Location;
import lombok.Data;

import java.util.List;

@Data
public class PageableResponse<T> {

    private CustomEmbedded _embedded;
    private PageMetadata page;

    public record CustomEmbedded(List<Location> locations) {
    }

    public record PageMetadata(long size, long totalElements, long totalPages, long number) {
    }

}
