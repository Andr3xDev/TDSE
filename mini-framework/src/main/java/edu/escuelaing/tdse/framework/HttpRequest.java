package edu.escuelaing.tdse.framework;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request with support for parsing and retrieving query
 * parameters.
 * <p>
 * This class provides methods to parse a query string into key-value pairs and
 * retrieve
 * individual query parameters in a decoded form.
 * </p>
 */
public class HttpRequest {

    private Map<String, String> queryParams;

    // Constructors
    public HttpRequest(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public HttpRequest(String queryString) {
        this.queryParams = parseQueryString(queryString);
    }

    // auxiliar methods
    public String getQueryParam(String key) {
        String value = queryParams.get(key);
        return value != null ? URLDecoder.decode(value, StandardCharsets.UTF_8) : null;
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString != null && !queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";
                params.put(key, value);
            }
        }
        return params;
    }

}