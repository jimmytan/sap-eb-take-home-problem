package org.sap.wenjun.daos;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.sap.wenjun.models.TrailInfo;
import org.sap.wenjun.models.TrailSearchContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class TrailDaoCsvFileImpl implements TrailDao{

    private static final String TRAIL_DATA_FILE_NAME = "BoulderTrailHeads.csv";
    private final URL resource = TrailDaoCsvFileImpl.class.getClassLoader().getResource(TRAIL_DATA_FILE_NAME);

    private final List<TrailInfo> trailInfos;

    public TrailDaoCsvFileImpl() throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = csvMapper
                .schemaFor(TrailInfo.class)
                .withHeader()
                .withColumnSeparator(',');
        MappingIterator<TrailInfo> mappingIterator = csvMapper
                .readerFor(TrailInfo.class)
                .with(schema)
                .readValues(new File(resource.getFile()));
        this.trailInfos = mappingIterator.readAll();
    }

    @Override
    public List<TrailInfo> getTrails(TrailSearchContext trailSearchContext) {

        return trailInfos.stream()
                .filter(trail -> trailSearchContext.getRestrooms() == null || trail.getRestrooms().equals(trailSearchContext.getRestrooms() ))
                .filter(trail -> trailSearchContext.getPicnic() == null ||  trail.getPicnic().equals(trailSearchContext.getPicnic() ))
                .filter(trail -> trailSearchContext.getTrailClass() == null || trail.getTrailClass().equals(trailSearchContext.getTrailClass()))
                .skip((trailSearchContext.getPageNumber() - 1) * trailSearchContext.getPageSize())
                .limit(trailSearchContext.getPageSize())
                .collect(Collectors.toList());
    }
}
