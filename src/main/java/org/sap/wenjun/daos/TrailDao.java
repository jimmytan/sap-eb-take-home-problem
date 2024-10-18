package org.sap.wenjun.daos;

import org.sap.wenjun.models.TrailInfo;
import org.sap.wenjun.models.TrailSearchContext;

import java.util.List;

public interface TrailDao {

    List<TrailInfo> getTrails(TrailSearchContext trailSearchContext);
}
