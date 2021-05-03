package com.my.categories.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonConverter.class);
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonConverter() {
    }

    /**
     * <p>
     * getJsonString.
     * </p>
     *
     * @param obj a {@link Object} object.
     * @return a {@link String} object.
     */
    public static String getJsonString(Object obj) {
        String json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException ioe) {
            LOGGER.error("An I/O exception occurred while serializing to json", ioe);
        }

        return json;
    }
}
