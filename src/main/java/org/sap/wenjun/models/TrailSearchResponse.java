package org.sap.wenjun.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrailSearchResponse {

    private int totalPage;
    private int currentPage;
    private List<TrailInfo> trailInfos;
}
