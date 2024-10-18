package org.sap.wenjun.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sap.wenjun.daos.TrailDao;
import org.sap.wenjun.models.TrailInfo;
import org.sap.wenjun.models.TrailSearchContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class GetTrailHandlerTest {

    @InjectMocks
    private GetTrailHandler getTrailHandler;
    @Mock
    private TrailDao trailDaoMock;
    private Context contextMock;

    @BeforeEach
    void setUp() {
        contextMock = Mockito.mock(Context.class);
    }

    @Test
    void testHandleRequest_Success() throws Exception {
        List<TrailInfo> mockTrails = new ArrayList<>();
        mockTrails.add(new TrailInfo(1, "Yes", "Yes", "No", "Trail A", "TH", "001", "T1", "Address 1", "Yes", "Yes", "No", "1", "2", 3, 4, "Asphalt", "Yes", "No", "No", "Yes", "Moderate", "Yes", "Yes", "Facility A", "Not Recommended", "2005-12-31", "2099-12-31", "Yes", "No", "Trail Name A", "Yes"));
        Mockito.when(trailDaoMock.getTrails(any(TrailSearchContext.class))).thenReturn(mockTrails);

        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setQueryStringParameters(Map.of("pageSize", "10", "page", "1", "picnic", "Yes", "restrooms", "Yes"));

        APIGatewayProxyResponseEvent responseEvent = getTrailHandler.handleRequest(requestEvent, contextMock);

        assertEquals(200, responseEvent.getStatusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponseBody = objectMapper.writeValueAsString(mockTrails);
        assertEquals(expectedResponseBody, responseEvent.getBody());
    }

    @Test
    void testHandleRequest_InvalidQueryParameter() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setQueryStringParameters(Map.of("picnic", "InvalidValue"));

        // Execute the handler
        APIGatewayProxyResponseEvent responseEvent = getTrailHandler.handleRequest(requestEvent, contextMock);

        // Verify the response
        assertEquals(404, responseEvent.getStatusCode());
        assertEquals("Wrong picnic query value. picnic query parameter value should be Yes or No.", responseEvent.getBody());
    }

    @Test
    void testHandleRequest_ExceptionHandling() {
        Mockito.when(trailDaoMock.getTrails(any(TrailSearchContext.class))).thenThrow(new RuntimeException("Database error"));

        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setQueryStringParameters(Map.of("pageSize", "10", "page", "1"));

        APIGatewayProxyResponseEvent responseEvent = getTrailHandler.handleRequest(requestEvent, contextMock);

        assertEquals(500, responseEvent.getStatusCode());
    }
}