package com.cisco.zeus;

import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ZeusSampleClient
{ 

    public static void main(String [] args) throws IOException
    {
        System.out.println("Zeus Sample Client");
       
        /////////////////////////////////////// 
        //Edit the below line to add your token
        //////////////////////////////////////
        String zeusapi = "Zeus_API_URL";
        String token = "Your_token_here";
        System.out.println("******Zeus Access token is "+token+"******");
        ZeusAPIClient zeusClient = new ZeusAPIClient(zeusapi, token);


        Integer val = 100;
        System.out.println("Sending a alert");
        Parameters params = new Parameters()
            .add("alert_name", "value"+(val+1))
            .add("username", "value"+(val+2))
            .add("alert_expression", "value"+(val+3));

        String result = zeusClient.sendAlert(params);
        System.out.println("Alert Post Result "+result);
        Integer alertID = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(result);
            Long lng = (Long)obj.get("id");
            alertID = lng != null ? lng.intValue() : null;
        } catch(ParseException e) {
            System.out.println("ParseERROR: "+e);
        }

        sleep(2);
        System.out.println("Retrieving all alerts");
        result = zeusClient.retrieveAlerts();
        System.out.println("Alerts Get Result "+result);

        System.out.println("Updating a alert with id: "+alertID);
        params = new Parameters()
            .add("alert_name", "value"+(val+4))
            .add("username", "value"+(val+5))
            .add("alert_expression", "value"+(val+6));
        result = zeusClient.updateAlert(alertID, params);
        System.out.println("Alert Put Result "+result);
        sleep(2);
        System.out.println("Retrieving a alert with id: "+alertID);
        result = zeusClient.retrieveAlert(alertID);
        System.out.println("Alert Get Result "+result);

        System.out.println("Deleting alert with id: "+alertID);
        result = zeusClient.deleteAlert(alertID);
        System.out.println("Alert Delete Result "+result);
        System.out.println("If status code == 204, then delete is succeeded");


        System.out.println("Posting Metric with metric name: zeus-test");
        MetricList metric = new MetricList("zeus-test");
        
        metric.addColumns("col1", "col2", "col3")
                  .addValues(3, 3, 5.4)
                  .addValues(4, 4, 43.242)  // you can send multiple metrics
                  .build();

        result = zeusClient.sendMetrics(metric);
        System.out.println("Metrics Post Result "+result);
      
        System.out.println("Posting more metric points to the same metric: zeus-test");
        metric.addValues(5,5,100.31)
              .addValues(6,6,50.31)
              .build();
        result = zeusClient.sendMetrics(metric);
        System.out.println("Metrics Post Result "+result);
 
        sleep(2);
 
        System.out.println("Retrieving all metrics that matches pattern: zeus");
        //For list of available parameters, refer API doc http://api.ciscozeus.io 
        //metric_name can be any regex pattern
        Parameters metric_params = new Parameters();
        metric_params.add("metric_name", "zeus");//Here zeus matches our original metric: zeus-test
        result = zeusClient.retrieveMetricValues(metric_params);
        System.out.println("Metrics Get Result "+result);

        System.out.println("Deleting metric with metric name: zeus-test");
        result = zeusClient.deleteMetrics("zeus-test");
        System.out.println("Metrics Delete Result "+result); 


        System.out.println("Sending a log with log name: zeus-test");
        Log log = new Log()
            .setKeyValues("key1","value1")
            .setKeyValues("key2","value2")
            .setKeyValues("key3",100.01)
            .build();

        LogList loglist = new LogList("zeus-test")
                  .addLog(log)
                  .build();

        result = zeusClient.sendLogs(loglist);
        System.out.println("Logs Post Result "+result);

        System.out.println("Sending more logs with same log name: zeus-test");

        Log log1 = new Log()
            .setKeyValues("key3","value33")
            .setKeyValues("key4","value34")
            .setKeyValues("key5",34)
            .build();
        
        Log log2 = new Log()
            .setKeyValues("key1","value11")
            .setKeyValues("key4","value14")
            .build();
        
        loglist.addLog(log1)
               .addLog(log2)        
               .build();
        result = zeusClient.sendLogs(loglist);
        System.out.println("Logs Post Result "+result);

        sleep(2);
        System.out.println("Retrieving all logs with log name: zeus-test");
        //For list of available parameters, refer API doc http://api.ciscozeus.io 
        //log_name must be the log name to be retrieved
        Parameters log_params = new Parameters();
        log_params.add("log_name", "zeus-test");//Here zeus matches our original metric: zeus-test
        result = zeusClient.retrieveLogs(log_params);
        System.out.println("Logs Get Result "+result);


        System.out.println("Retrieving all trigalerts");
        result = zeusClient.retrieveTrigalerts();
        System.out.println("Alerts Get Result "+result);

        System.out.println("Retrieving all trigalertLast24s");
        result = zeusClient.retrieveTrigalertsLast24();
        System.out.println("Alert Get Result "+result);
    }

    public static void sleep(int sec) {
        System.out.println("Sleeping for "+sec+" seconds");
        try {
            Thread.sleep(sec*1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
