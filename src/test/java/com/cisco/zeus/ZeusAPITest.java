package com.cisco.zeus;

//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Iterator;

public class ZeusAPITest 
{
    String token = "28edde36";
    String testSeriesName = "testing";
    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code   
        System.out.println("OnetimeSetup: Setting up ZeusAPI Client for testing");
    }
 
    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        System.out.println("oneTimeTearDown");
    }
    
    public void sleep(int sec) {
        System.out.println("Sleeping for "+sec+" seconds");
        try {
            Thread.sleep(sec*1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public int countDataPointsInMetric(String metricName, String result) throws ParseException {
        int length = -1;
        JSONParser parser=new JSONParser();
        Object obj=parser.parse(result);
        JSONArray array=(JSONArray)obj;
        Iterator<JSONObject> iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject json = (JSONObject) (iterator.next());
            if((json.get("name")).equals(metricName)) {
                length = ((JSONArray)json.get("points")).size();
                break;
            }
        }
        System.out.println("Total datapoints"+length);
        return length;         
    }

    public int countTotalMetricNames(String result) throws ParseException {
        JSONParser parser=new JSONParser();
        Object obj=parser.parse(result);
        JSONArray array=(JSONArray)obj;
        System.out.println("Total metrics"+array.size());
        return array.size();         
    }

    public boolean isMetricNamePresent(String metricName, String result) throws ParseException {
        JSONParser parser=new JSONParser();
        boolean isPresent = false;
        Object obj=parser.parse(result);
        JSONArray array=(JSONArray)obj;
        Iterator<JSONObject> iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject json = (JSONObject) (iterator.next());
            if((json.get("name")).equals(metricName)) {
                isPresent = true;
                break;
            }
        }
        System.out.println("isPresent"+isPresent); 
        return isPresent;         
    }

    @Test
    public void testSingleMetric() throws IOException, ParseException
    {
        ZeusAPIClient zeusClient = new ZeusAPIClient(token);
        //send a single Metric to Zeus with only value field 

        //Each Metric is (timestamp, <list of columns>) pair in the system
        //If timestamp is omitted, system generated timestamp will be used
        System.out.println("Sending Metric");
        Metric metric = new Metric()
                            .setColumns("col1","col2")
                            .setValues(3,3)
                            .build();
        //It creates a series named testing with value 1001
        String result = zeusClient.sendMetrics(testSeriesName, metric);
        System.out.println("Metric Sent: "+result);

        sleep(3);

        System.out.println("Retrieving Metric");
        Parameters params = new Parameters();
        params.add("metric_name",testSeriesName);
        result = zeusClient.retrieveMetricValues(params);

        System.out.println("Metric Got: "+result);
        assertTrue(isMetricNamePresent(testSeriesName,result));        
        assertTrue(countDataPointsInMetric(testSeriesName, result) == 1);
        assertTrue(countTotalMetricNames(result) == 1);
    
        System.out.println("Delete Metric"); 
        result = zeusClient.deleteMetrics(testSeriesName);
        System.out.println("Metric Delete: "+result);
    }
   
    @Test
    public void testMultipleMetrics() throws IOException, ParseException
    {
        ZeusAPIClient zeusClient = new ZeusAPIClient(token);
        //send mulitple Metrics to Zeus

        //Each Metric is (timestamp, <list of columns>) pair in the system
        //If timestamp is omitted, system generated timestamp will be used
        System.out.println("Sending Metrics");
        Metric metric = new Metric()
                            .setColumns("col1","col2")
                            .setValues(3,3)
                            .setValues(4,4)
                            .build();
        //It creates a series named testing with value 1001
        String result = zeusClient.sendMetrics(testSeriesName, metric);
        System.out.println("Metric Sent: "+result);

        sleep(3);

        System.out.println("Retrieving Metrics");
        Parameters params = new Parameters();
        params.add("metric_name",testSeriesName);
        params.add("limit",10);
        result = zeusClient.retrieveMetricValues(params);

        System.out.println("Metric Got: "+result);
        assertTrue(isMetricNamePresent(testSeriesName,result));        
        assertTrue(countDataPointsInMetric(testSeriesName, result) == 2);
        assertTrue(countTotalMetricNames(result) == 1);

       
        System.out.println("Delete Metric"); 
        result = zeusClient.deleteMetrics(testSeriesName);
        System.out.println("Metric Delete: "+result);
        
    }

    @Test
    public void testMultipleMetricsWithTimestamps() throws IOException, ParseException
    {
        ZeusAPIClient zeusClient = new ZeusAPIClient(token);
        //send mulitple Metrics to Zeus

        //Each Metric is (timestamp, <list of columns>) pair in the system
        //If timestamp is omitted, system generated timestamp will be used
        System.out.println("Sending Metrics");
        Metric metric = new Metric()
                            .setColumns("timestamp","col1","col2")
                            .setValues(1423034086.343,3,3)
                            .setValues(1423034089,4,4)
                            .build();
        //It creates a series named testing with value 1001
        String result = zeusClient.sendMetrics(testSeriesName, metric);
        System.out.println("Metric Sent: "+result);

        sleep(3);

        System.out.println("Retrieving Metrics");
        Parameters params = new Parameters();
        params.add("metric_name",testSeriesName);
        params.add("limit",10);
        result = zeusClient.retrieveMetricValues(params);

        System.out.println("Metric Got: "+result);
        assertTrue(isMetricNamePresent(testSeriesName,result));        
        assertTrue(countDataPointsInMetric(testSeriesName, result) == 2);
        assertTrue(countTotalMetricNames(result) == 1);

       
        System.out.println("Delete Metric"); 
        result = zeusClient.deleteMetrics(testSeriesName);
        System.out.println("Metric Delete: "+result);
        
    }

}
