package org.sap.wenjun.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.sap.wenjun.config.ExternalVariableMockImpl;
import org.sap.wenjun.daos.TrailDao;
import org.sap.wenjun.daos.TrailDaoCsvFileImpl;
import org.sap.wenjun.models.TrailInfo;
import org.sap.wenjun.models.TrailSearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.sap.wenjun.config.Constant.PAGE_SIZE;

public class GetTrailHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String PAGE_SIZE_QUERY = "pageSize";
    private static final String PAGE_QUERY = "page";
    private static final String PICNIC_QUERY = "picnic";
    private static final String RESTROOMS_QUERY = "restrooms";
    private static final String TRAIL_CLASS_QUERY = "trailClass";

    private static final Set<String> STRING_BOOLEAN_VALUES = Set.of("Yes", "No");

    private static final String WRONG_QUERY_VALUE = "Wrong %s query value. %s query parameter value should be Yes or No.";

    private static final String TRAIL_DATA_FILE_NAME = "BoulderTrailHeads.csv";


    private static final Logger logger = LoggerFactory.getLogger(GetTrailHandler.class);

    private ExternalVariableMockImpl externalVariableMock;
    private TrailDao trailDao;

    private ObjectMapper objectMapper;

    public GetTrailHandler() throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        externalVariableMock = new ExternalVariableMockImpl();
        URL resource = TrailDaoCsvFileImpl.class.getClassLoader().getResource(TRAIL_DATA_FILE_NAME);
        trailDao = new TrailDaoCsvFileImpl(new File(resource.getFile()), csvMapper);
        objectMapper = new ObjectMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {

        int pageSize = externalVariableMock.getInt(PAGE_SIZE);
        int page = 1;
        String trailClass = null;
        String picnic = null;
        String restrooms = null;
        try {
            Map<String, String> queryParams = apiGatewayProxyRequestEvent.getQueryStringParameters();
            if (queryParams != null && !queryParams.isEmpty()) {
                if (queryParams.containsKey(PICNIC_QUERY) && !STRING_BOOLEAN_VALUES.contains(queryParams.get(PICNIC_QUERY))) {
                    return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody(String.format(WRONG_QUERY_VALUE, PICNIC_QUERY, PICNIC_QUERY));
                }

                if (queryParams.containsKey(RESTROOMS_QUERY) && !STRING_BOOLEAN_VALUES.contains(queryParams.get(RESTROOMS_QUERY))) {
                    return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody(String.format(WRONG_QUERY_VALUE, RESTROOMS_QUERY, RESTROOMS_QUERY));
                }
                pageSize = queryParams.containsKey(PAGE_SIZE_QUERY) ? Integer.parseInt(queryParams.get(PAGE_SIZE_QUERY)) : externalVariableMock.getInt(PAGE_SIZE);
                page = queryParams.containsKey(PAGE_QUERY) ? Integer.parseInt(queryParams.get(PAGE_QUERY)) : 1;
                if (queryParams.containsKey(PICNIC_QUERY)) {
                    picnic = queryParams.get(PICNIC_QUERY);
                }
                if (queryParams.containsKey(RESTROOMS_QUERY)) {
                    restrooms = queryParams.get(RESTROOMS_QUERY);
                }

                if (queryParams.containsKey(TRAIL_CLASS_QUERY)) {
                    trailClass = queryParams.get(TRAIL_CLASS_QUERY);
                }

            }

            TrailSearchContext searchContext = TrailSearchContext.builder()
                    .pageNumber(page)
                    .pageSize(pageSize)
                    .trailClass(trailClass)
                    .picnic(picnic)
                    .restrooms(restrooms).build();



            List<TrailInfo> trailInfos = trailDao.getTrails(searchContext);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(trailInfos));
        } catch (Exception e) {
            logger.error("fail to handle the request", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }

    }
}
