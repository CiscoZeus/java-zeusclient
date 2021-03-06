package com.cisco.zeus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


/**
 * ZeusAPIClient
 */
public class ZeusAPIClient {
    private HttpClient client;
    private String accessToken;
    private String bucketName;

    // TODO Configurable Endpoint
    private final static String ZEUS_API = "http://api.ciscozeus.io";

    /**
     * @param token is either user token or bucket (external) token.
     */
    public ZeusAPIClient(String token) {
        bucketName = null;
        accessToken = token;
        client = HttpClientBuilder.create().build();
    }

    public ZeusAPIClient bucket(String bucketFullName) {
        bucketName = bucketFullName;
        return this;
    }

    /**
     * Send Metrics to the API endpoint
     *
     * @param metricsList is List of Metric object to be sent
     * @return Response message
     * @throws IOException is risen when the server is not available or the response could not be parsed
     */
    public JSONObject sendMetrics(MetricList metricsList) throws IOException {
        String tag = metricsList.metricName;
        String path = String.format("/metrics/%s/%s/", accessToken, tag);

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("metrics", metricsList.toString()));

        HttpResponse response = request("POST", path, params);
        return deserialize(response);
    }

    /**
     * Send Logs to the API endpoint
     *
     * @param logList is list of log object to be sent
     * @return Response message
     * @throws IOException is risen when the server is not available or the response could not be parsed
     */
    public JSONObject sendLogs(LogList logList) throws IOException {
        String path = String.format("/logs/%s/%s/", accessToken, logList.logName);

        ArrayList<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("logs", logList.toString()));

        HttpResponse response = request("POST", path, body);

        return deserialize(response);
    }

    /**
     * @param params is Parameter object for building request message
     * @return JSON Response object
     * @throws IOException is risen when the server is not available or the response could not be parsed
     */
    public JSONObject retrieveMetricValues(Parameters params) throws IOException {
        String queryString = URLEncodedUtils.format(params.toNameValuePairList(), "utf-8");
        String path = String.format("/metrics/%s/_values?%s", accessToken, queryString);

        HttpResponse response = request("GET", path);
        return deserialize(response);
    }

    /**
     * @param params is Parameter object for building request message
     * @return JSON Response object
     * @throws IOException is risen when the server is not available or the response could not be parsed
     */
    public JSONObject retrieveMetricNames(Parameters params) throws IOException {
        String queryString = URLEncodedUtils.format(params.toNameValuePairList(), "utf-8");
        String path = String.format("/metrics/%s/_names?%s", accessToken, queryString);
        HttpResponse response = request("GET", path);
        return deserialize(response);
    }

    /**
     * @param tag is target tag to be deleted
     * @return JSON Response object
     * @throws IOException is risen when the server is not available or the response could not be parsed
     */
    public JSONObject deleteMetrics(String tag) throws IOException {
        String path = String.format("/metrics/%s/%s/", accessToken, tag);
        HttpResponse response = request("DELETE", path);
        return deserialize(response);
    }

    /**
     * @param params will be converted into URL query parameter
     * @return JSON Response object
     * @throws IOException is risen when the server is not available or the response could not be parsed
     */
    public JSONObject retrieveLogs(Parameters params) throws IOException {
        String queryString = URLEncodedUtils.format(params.toNameValuePairList(), "utf-8");
        String path = String.format("/logs/%s?%s", accessToken, queryString);
        HttpResponse response = request("GET", path, params.toNameValuePairList());
        return deserialize(response);
    }

    private HttpResponse request(
            String method, String path) throws IOException {
        return request(method, path, Collections.<String, String>emptyMap(), new LinkedList<NameValuePair>());
    }

    private HttpResponse request(
            String method, String path, List<NameValuePair> body) throws IOException {
        return request(method, path, Collections.<String, String>emptyMap(), body);
    }

    /**
     * request do HTTP Call against the endpoint.
     * <p>
     * TODO body should be JSON instead of List (Form Data).
     * This requires API endpoints to support for JSON message.
     *
     * @param method HTTP Method such as GET, POST, PUT or DELETE.
     * @param path   URL Path starting with '/'
     * @param header HTTP Header
     * @param body   HTTP Form data
     * @return HTTP Response
     * @throws IOException
     */
    private HttpResponse request(
            String method, String path,
            Map<String, String> header, List<NameValuePair> body) throws IOException {

        String url = ZEUS_API.concat(path);
        HttpRequestBase request;
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(body);

        switch (method.toUpperCase()) {
            case "GET":
                request = new HttpGet(url);
                break;
            case "POST":
                HttpPost postRequest = new HttpPost(url);
                postRequest.setEntity(entity);
                request = postRequest;
                break;
            case "PUT":
                HttpPut putRequest = new HttpPut(url);
                putRequest.setEntity(entity);
                request = putRequest;
                break;
            case "DELETE":
                request = new HttpDelete(url);
                break;
            default:
                throw new IOException(String.format("Unknown Method %s", method));
        }

        request.setHeader("Authorization", String.format("Bearer %s", accessToken));

        if (bucketName != null) {
            request.setHeader("Bucket-Name", bucketName);
            bucketName = null;
        }

        for (Map.Entry<String, String> entry : header.entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());
        }

        return execute(request);
    }

    /**
     * Deserialize the response message
     * <p>
     * NOTE Not sure it's proper implementation to convert response object to just String.
     * It should be JSON object instead of string?? (this implementation design comes from
     * old implementation, please feel free to refactor these code).
     *
     * @param response is HttpResponse Object from zeus_api
     * @return JSON JSONObject
     * @throws IOException is risen when could not parse the response body
     */
    private JSONObject deserialize(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            // Empty response body
            return new JSONObject();
        }

        String body = EntityUtils.toString(entity);
        return new JSONObject(body);
    }

    /**
     * This method is for mocking the request/response of API call.
     * <p>
     * TODO Mock HttpClient.execute(HttpRequestBase) instead, and remove this function.
     *
     * @param request to be used by HttpClient
     * @return HttpResponse object
     * @throws IOException is risen when API server is not available
     */
    protected HttpResponse execute(HttpRequestBase request) throws IOException {
        return client.execute(request);
    }
}
