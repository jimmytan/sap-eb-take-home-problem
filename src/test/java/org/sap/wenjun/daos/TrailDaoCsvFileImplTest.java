package org.sap.wenjun.daos;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sap.wenjun.models.TrailInfo;
import org.sap.wenjun.models.TrailSearchContext;
import org.sap.wenjun.models.TrailSearchResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailDaoCsvFileImplTest {

    private static final String YES = "Yes";
    private static final String T1_TRAIL_CLASS = "T1";

    private static final String CSV_HEADER = "FID,RESTROOMS,PICNIC,FISHING,AKA,AccessType,AccessID,Class,Address,Fee,BikeRack,BikeTrail,DogTube,Grills,TrashCans,ParkSpaces,ADAsurface,ADAtoilet,ADAfishing,ADAcamping,ADApicnic,ADAtrail,ADAparking,ADAfacilit,ADAfacName,HorseTrail,DateFrom,DateTo,RecycleBin,DogCompost,AccessName,THLeash\n";
    private static final String TRAIL_1 = "0,Yes,Yes,No, ,TH,279,T3,621 Flagstaff Summit Rd,Yes,No,No,1,Yes,4,12,Asphalt,Yes,No,No,Yes,Moderate,Yes,Yes,Wood Shelter,Not Recommended,12/31/2005 0:00,12/31/2099 0:00,Yes,No,Flagstaff Summit West,Yes\n";
    private static final String TRAIL_2 = "1,Yes,Yes,No, ,TH,277,T3,790 Flagstaff Summit Rd,Yes,Yes,No,1,Yes,6,56,Asphalt,Yes,No,No,Yes,Difficult,Yes,Yes,Nature Center,Not Recommended,12/31/2005 0:00,12/31/2099 0:00,Yes,No,Flagstaff Summit East,Yes\n";
    private static final String TRAIL_3 = "2,No,No,No, ,TH,502a,T1,4705 95th St,No,Yes,Yes,0,No,1,6,No,No,No,No,No,No,No,No, ,Not Recommended,12/31/2005 0:00,12/31/2099 0:00,No,No,East Boulder Trail at White Rocks,Yes\n";

    private File tempCsvFile;
    private TrailDaoCsvFileImpl trailDaoCsvFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempCsvFile = File.createTempFile("trail_test_data", ".csv");
        try (FileWriter writer = new FileWriter(tempCsvFile)) {
            writer.write(CSV_HEADER);
            writer.write(TRAIL_1);
            writer.write(TRAIL_2);
            writer.write(TRAIL_3);
        }
        CsvMapper csvMapper = new CsvMapper();

        trailDaoCsvFile = new TrailDaoCsvFileImpl(tempCsvFile, csvMapper);
    }

    @AfterEach
    public void tearDown() {
        if (tempCsvFile.exists()) {
            tempCsvFile.delete();
        }
    }

    @Test
    public void testGetTrails_FilterByRestrooms() {
        TrailSearchContext searchContext = TrailSearchContext.builder().restrooms(YES).pageNumber(1).pageSize(10).build();

        TrailSearchResponse response = trailDaoCsvFile.getTrails(searchContext);
        List<TrailInfo> filteredTrails = response.getTrailInfos();

        assertEquals(2, filteredTrails.size());
        assertEquals(1, response.getTotalPage());
        assertTrue(filteredTrails.stream().allMatch(trailInfo -> trailInfo.getRestrooms().equals(YES)));
    }

    @Test
    public void testGetTrails_FilterByPicnic() {
        TrailSearchContext searchContext = TrailSearchContext.builder().picnic(YES).pageNumber(1).pageSize(10).build();

        TrailSearchResponse response = trailDaoCsvFile.getTrails(searchContext);
        List<TrailInfo> filteredTrails = response.getTrailInfos();

        assertEquals(2, filteredTrails.size());
        assertTrue(filteredTrails.stream().allMatch(trailInfo -> trailInfo.getPicnic().equals(YES)));
    }

    @Test
    public void testGetTrails_FilterByTrailClass() {
        TrailSearchContext searchContext = TrailSearchContext.builder().trailClass(T1_TRAIL_CLASS).pageNumber(1).pageSize(10).build();

        TrailSearchResponse response = trailDaoCsvFile.getTrails(searchContext);
        List<TrailInfo> filteredTrails = response.getTrailInfos();

        assertEquals(1, filteredTrails.size());
        assertTrue(filteredTrails.stream().allMatch(trail -> T1_TRAIL_CLASS.equals(trail.getTrailClass())));
    }

    @Test
    public void testGetTrails_Pagination() {
        TrailSearchContext searchContext = TrailSearchContext.builder().pageSize(2).pageNumber(1).build();

        TrailSearchResponse response = trailDaoCsvFile.getTrails(searchContext);
        List<TrailInfo> filteredTrails = response.getTrailInfos();

        assertEquals(2, filteredTrails.size());

        searchContext.setPageNumber(2);
        response = trailDaoCsvFile.getTrails(searchContext);
        filteredTrails = response.getTrailInfos();

        assertEquals(1, filteredTrails.size());
    }

}