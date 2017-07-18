package com.cisco.zeus;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.junit.*;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * ZeusAPIMetricTest
 * <p>
 * Test For the methods related to the Metrics service.
 */
public class ZeusAPIMetricTest {
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
    public void testSendMetrics() throws IOException, ParseException {
        String metricsName = "fake-metrics";
        String expectedURI = String.format(
                "http://api.ciscozeus.io/metrics/%s/%s/",
                fakeToken, metricsName
        );

        Parameters params = new Parameters();
        params.add("metric_name", metricsName);


        MetricList metric = new MetricList(metricsName)
                .addColumns("col1", "col2")
                .addValues(3, 3)
                .build();

        mockResponse(SC_OK);
        client.sendMetrics(metric);
        verify(client, times(1)).execute(requestArguments.capture());

        HttpRequestBase fakeRequest = requestArguments.getValue();
        assertEquals(HttpPost.METHOD_NAME, fakeRequest.getMethod());
        assertEquals(expectedURI, fakeRequest.getURI().toString());
        checkAuthorizationHeader(fakeRequest);

        // TODO Check the request body.
    }

    @Test
    public void testRetrieveMetricNames() throws IOException {
        String expectedURI = String.format(
                "http://api.ciscozeus.io/metrics/%s/_names", fakeToken
        );

        Parameters params = new Parameters();
        params.add("metric_name", "fake-metrics");

        mockResponse(SC_OK);
        client.retrieveMetricNames(params);
        verify(client, times(1)).execute(requestArguments.capture());

        HttpRequestBase fakeRequest = requestArguments.getValue();
        assertEquals(HttpGet.METHOD_NAME, fakeRequest.getMethod());
        // TODO  Check the request message reflects Parameter object correctly to URL
        assertTrue(fakeRequest.getURI().toString().contains(expectedURI));
        checkAuthorizationHeader(fakeRequest);
    }

    @Test
    public void testRetrieveMetricValues() throws IOException {
        String expectedURI = String.format("http://api.ciscozeus.io/metrics/%s/_values", fakeToken);

        Parameters params = new Parameters();
        params.add("metric_name", "fake-metrics");
        params.add("offset", 1);
        params.add("limit", 3);
        params.add("from", 1433198121);
        params.add("to", 1433198361);
        params.add("aggregator_function", "count");
        params.add("aggregator_column", "col1");
        params.add("group_interval", "5m");

        mockResponse(SC_OK);
        client.retrieveMetricValues(params);
        verify(client, times(1)).execute(requestArguments.capture());

        HttpRequestBase fakeRequest = requestArguments.getValue();
        assertEquals(HttpGet.METHOD_NAME, fakeRequest.getMethod());
        // TODO  Check the request message reflects Parameter object correctly to URL
        assertTrue(fakeRequest.getURI().toString().contains(expectedURI));
        checkAuthorizationHeader(fakeRequest);

        // TODO Check the request message reflects Parameter object correctly.
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
