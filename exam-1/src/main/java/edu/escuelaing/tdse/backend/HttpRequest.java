package edu.escuelaing.tdse.backend;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private Map<String, String> queryParams;

    public HttpRequest(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public HttpRequest(String queryString) {
        this.queryParams = parseQueryString(queryString);
    }

    public String getQueryParam(String key) {
        String value = queryParams.get(key);
        return value != null ? URLDecoder.decode(value) : null;
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