package org.sap.wenjun.config;

import org.sap.wenjun.daos.TrailDaoCsvFileImpl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.sap.wenjun.config.Constant.PAGE_SIZE;

/**
 * This class mocks any external variable.
 * Usually these values can get from environment variables, app config server.
 */
public class ExternalVariableMockImpl {

    private static final String TRAIL_DATA_FILE_NAME = "BoulderTrailHeads.csv";
    private final Map<String, String> configs;

    public ExternalVariableMockImpl() {
        configs = new HashMap<>();
        configs.put(PAGE_SIZE, "10");
        URL csvFileResource = TrailDaoCsvFileImpl.class.getClassLoader().getResource(TRAIL_DATA_FILE_NAME);
        configs.put(TRAIL_DATA_FILE_NAME, csvFileResource.getFile());

    }

    public int getInt(String key) {
        return Integer.parseInt(configs.get(key));
    }

}
