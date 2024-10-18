package org.sap.wenjun.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpPrincipal;
import org.sap.wenjun.config.ExternalVariableMockImpl;
import org.sap.wenjun.daos.TrailDao;
import org.sap.wenjun.daos.TrailDaoCsvFileImpl;
import org.sap.wenjun.models.TrailInfo;
import org.sap.wenjun.models.TrailSearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.sap.wenjun.config.Constant.PAGE_SIZE;

public class GetTrailHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String PAGE_SIZE_QUERY = "pageSize";
    private static final String PAGE_QUERY = "pageSize";
    private static final String PICNIC_QUERY = "picnic";
    private static final String RESTROOMS_QUERY = "restrooms";
    private static final String TRAIL_CLASS_QUERY = "trailClass";

    private static final Set<String> STRING_BOOLEAN_VALUES = Set.of("Yes", "No");

    private static final String WRONG_QUERY_VALUE = "Wrong %s query value. %s query parameter value should be Yes or No.";

    private static final Logger logger = LoggerFactory.getLogger(GetTrailHandler.class);

    private final ExternalVariableMockImpl externalVariableMock;
    private final TrailDao trailDao;

    private final ObjectMapper objectMapper;

    public GetTrailHandler() throws IOException {
        externalVariableMock = new ExternalVariableMockImpl();
        trailDao = new TrailDaoCsvFileImpl();
        objectMapper = new ObjectMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {

        try {
            Map<String, String> queryParams = apiGatewayProxyRequestEvent.getQueryStringParameters();
            if (queryParams.containsKey(PAGE_QUERY) && !STRING_BOOLEAN_VALUES.contains(queryParams.get(PICNIC_QUERY))) {
                return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody(String.format(WRONG_QUERY_VALUE, PICNIC_QUERY, PICNIC_QUERY));
            }

            if (queryParams.containsKey(RESTROOMS_QUERY) && !STRING_BOOLEAN_VALUES.contains(queryParams.get(RESTROOMS_QUERY))) {
                return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody(String.format(WRONG_QUERY_VALUE, RESTROOMS_QUERY, RESTROOMS_QUERY));
            }

            int pageSize = queryParams.containsKey(PAGE_SIZE_QUERY) ? Integer.parseInt(queryParams.get(PAGE_SIZE_QUERY)) : externalVariableMock.getInt(PAGE_SIZE);
            int page = queryParams.containsKey(PAGE_QUERY) ? Integer.parseInt(queryParams.get(PAGE_QUERY)) : 1;
            TrailSearchContext searchContext = TrailSearchContext.builder()
                    .pageNumber(page)
                    .pageSize(pageSize)
                    .trailClass(queryParams.get(TRAIL_CLASS_QUERY))
                    .picnic(queryParams.get(PICNIC_QUERY))
                    .restrooms(queryParams.get(TRAIL_CLASS_QUERY)).build();

            List<TrailInfo> trailInfos = trailDao.getTrails(searchContext);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(trailInfos));
        } catch (Exception e) {
            logger.error("fail to handle the request", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }

    }
}
