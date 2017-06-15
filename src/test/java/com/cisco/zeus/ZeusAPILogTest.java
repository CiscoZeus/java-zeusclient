package com.cisco.zeus;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.URLEncoder;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * ZeusAPILogTest
 * <p>
 * Test For the methods related to the Logs service.
 */
public class ZeusAPILogTest {
    private ZeusAPIClient client;
    private static String fakeToken;
    private static String fakeAuthHeader;
    private static HttpResponseFactory responseFactory;
    private ArgumentCaptor<HttpRequestBase> requestArguments;

    @BeforeClass
    public static void beforeAll() {
        fakeToken = "fake-token";
        fakeAuthHeader = String.format("Bearer %s", fakeToken);
        responseFactory = new DefaultHttpResponseFactory();
    }

    @Before
    public void beforeEach() {
        client = spy(new ZeusAPIClient(fakeToken));
        requestArguments = ArgumentCaptor.forClass(HttpRequestBase.class);
    }

    @Test
    public void testSendLogs() throws IOException {
        String fakeLogName = "fake-logs";
        String expectedURI = String.format(
                "http://api.ciscozeus.io/logs/%s/%s/",
                fakeToken, fakeLogName
        );

        JSONObject fakeResponse = new JSONObject();
        fakeResponse.put("successful", 1);
        mockResponse(SC_OK, fakeResponse);

        Log log = new Log()
                .setKeyValues("key1", "value1")
                .setKeyValues("key2", "value2")
                .build();

        LogList loglist = new LogList(fakeLogName).add(log).build();

        client.sendLogs(loglist);
        verify(client, times(1)).execute(requestArguments.capture());

        HttpRequestBase fakeRequest = requestArguments.getValue();

        assertEquals(HttpPost.METHOD_NAME, fakeRequest.getMethod());
        assertEquals(expectedURI, fakeRequest.getURI().toString());

        checkAuthorizationHeader(fakeRequest);
        HttpEntity entity = ((HttpPost) fakeRequest).getEntity();
        String formData = EntityUtils.toString(entity);

        String expected = new JSONArray()
                .put(new JSONObject().put("key1", "value1").put("key2", "value2"))
                .toString();

        expected = "logs=".concat(URLEncoder.encode(expected, "UTF-8"));
        assertEquals(expected, formData);
    }

    @Ignore // TODO Fix this test case
    @Test
    public void testSendLogsWithMultipleLogs() throws IOException {
        String fakeLogName = "fake-logs";
        String expectedURI = String.format(
                "http://api.ciscozeus.io/logs/%s/%s/",
                fakeToken, fakeLogName
        );

        JSONObject fakeResponse = new JSONObject();
        fakeResponse.put("successful", 2);
        mockResponse(SC_OK, fakeResponse);

        Log log1 = new Log()
                .setKeyValues("key1", "value1")
                .setKeyValues("key2", "value2")
                .build();

        Log log2 = new Log()
                .setKeyValues("key3", "value3")
                .setKeyValues("key4", "value4")
                .build();

        LogList loglist = new LogList(fakeLogName).add(log1).add(log2).build();

        client.sendLogs(loglist);
        verify(client, times(1)).execute(requestArguments.capture());

        HttpRequestBase fakeRequest = requestArguments.getValue();

        assertEquals(HttpPost.METHOD_NAME, fakeRequest.getMethod());
        assertEquals(expectedURI, fakeRequest.getURI().toString());

        checkAuthorizationHeader(fakeRequest);
        HttpEntity entity = ((HttpPost) fakeRequest).getEntity();
        String formData = EntityUtils.toString(entity);

        String expected = new JSONArray()
                .put(new JSONObject().put("key1", "value1").put("key2", "value2"))
                .put(new JSONObject().put("key3", "value3").put("key4", "value4"))
                .toString();

        expected = "logs=".concat(URLEncoder.encode(expected, "UTF-8"));

        assertEquals(expected, formData);
    }

    @Test
    public void testRetrieveLogs() throws IOException {
        String fakeLogName = "fake-logs";
        String expectedURI = String.format(
                "http://api.ciscozeus.io/logs/%s?log_name=%s",
                fakeToken, fakeLogName
        );

        Parameters params = new Parameters();
        params.add("log_name", fakeLogName);

        mockResponse(SC_OK);
        client.retrieveLogs(params);
        verify(client, times(1)).execute(requestArguments.capture());

        HttpRequestBase fakeRequest = requestArguments.getValue();
        assertEquals(HttpGet.METHOD_NAME, fakeRequest.getMethod());
        assertEquals(expectedURI, fakeRequest.getURI().toString());
    }

    private void checkAuthorizationHeader(HttpRequestBase request) {
        Header[] fakeHeader = request.getHeaders("Authorization");
        assertEquals(1, fakeHeader.length);
        assertEquals(fakeAuthHeader, fakeHeader[0].getValue());
    }


    private void mockResponse(int statusCode) throws IOException {
        BasicStatusLine statusLine = new BasicStatusLine(HTTP_1_1, statusCode, null);
        HttpResponse fakeResponse = responseFactory.newHttpResponse(statusLine, null);

        doReturn(fakeResponse).when(client).execute((HttpRequestBase) any());
    }

    private void mockResponse(int statusCode, JSONObject body) throws IOException {
        BasicStatusLine statusLine = new BasicStatusLine(HTTP_1_1, statusCode, null);
        HttpResponse fakeResponse = responseFactory.newHttpResponse(statusLine, null);
        StringEntity jsonBody = new StringEntity(body.toString(), ContentType.APPLICATION_JSON);
        fakeResponse.setEntity(jsonBody);

        doReturn(fakeResponse).when(client).execute((HttpRequestBase) any());
    }
}
