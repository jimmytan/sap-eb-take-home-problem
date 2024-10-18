package org.sap.wenjun.config;

import java.util.HashMap;
import java.util.Map;

import static org.sap.wenjun.config.Constant.PAGE_SIZE;

/**
 * This class mocks any external variable.
 * Usually these values can get from environment variables, app config server.
 */
public class ExternalVariableMockImpl {

    private final Map<String, String> configs;


    public ExternalVariableMockImpl() {
        configs = new HashMap<>();
        configs.put(PAGE_SIZE, "10");

    }

    public int getInt(String key) {
        return Integer.parseInt(configs.get(key));
    }

}
