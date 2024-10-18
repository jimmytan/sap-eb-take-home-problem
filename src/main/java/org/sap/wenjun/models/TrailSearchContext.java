package org.sap.wenjun.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TrailSearchContext {

    private String restrooms;
    private String picnic;
    private String trailClass;
    private int pageNumber;
    private int pageSize;
}

