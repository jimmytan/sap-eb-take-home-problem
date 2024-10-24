package org.sap.wenjun.daos;

import org.sap.wenjun.models.TrailInfo;
import org.sap.wenjun.models.TrailSearchContext;
import org.sap.wenjun.models.TrailSearchResponse;

import java.util.List;

public interface TrailDao {

    TrailSearchResponse getTrails(TrailSearchContext trailSearchContext);
}
