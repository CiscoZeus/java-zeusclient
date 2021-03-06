package com.cisco.zeus;

import org.json.JSONObject;

import java.io.IOException;

public class ZeusSampleClient {

    public static void main(String[] args) throws IOException {
        System.out.println("Zeus Sample Client");

        /////////////////////////////////////// 
        //Edit the below line to add your token
        //////////////////////////////////////
        String token = "Your_token_here";

        System.out.println("******Zeus Access token is " + token + "******");
        ZeusAPIClient zeusClient = new ZeusAPIClient(token);

        System.out.println("Posting Metric with metric name: zeus-test");
        MetricList metric = new MetricList("zeus-test");

        metric.addColumns("col1", "col2", "col3")
                .addValues(3, 3, 5.4)
                .addValues(4, 4, 43.242)  // you can send multiple metrics
                .build();

        JSONObject response;
        response = zeusClient.sendMetrics(metric);
        System.out.println("Metrics Post Result " + response.toString());

        System.out.println("Posting more metric points to the same metric: zeus-test");
        metric.addValues(5, 5, 100.31)
                .addValues(6, 6, 50.31)
                .build();
        response = zeusClient.sendMetrics(metric);
        System.out.println("Metrics Post Result " + response.toString());

        sleep(2);

        System.out.println("Retrieving all metrics that matches pattern: zeus");
        //For list of available parameters, refer API doc http://api.ciscozeus.io 
        //metric_name can be any regex pattern
        Parameters metric_params = new Parameters();
        metric_params.add("metric_name", "zeus");//Here zeus matches our original metric: zeus-test
        response = zeusClient.retrieveMetricValues(metric_params);
        System.out.println("Metrics Get Result " + response.toString());

        System.out.println("Deleting metric with metric name: zeus-test");
        response = zeusClient.deleteMetrics("zeus-test");
        System.out.println("Metrics Delete Result " + response.toString());


        System.out.println("Sending a log with log name: zeus-test");
        Log log = new Log()
                .setKeyValues("key1", "value1")
                .setKeyValues("key2", "value2")
                .setKeyValues("key3", 100.01)
                .build();

        LogList loglist = new LogList("zeus-test")
                .add(log)
                .build();

        response = zeusClient.sendLogs(loglist);
        System.out.println("Logs Post Result " + response.toString());

        System.out.println("Sending more logs with same log name: zeus-test");

        Log log1 = new Log()
                .setKeyValues("key3", "value33")
                .setKeyValues("key4", "value34")
                .setKeyValues("key5", 34)
                .build();

        Log log2 = new Log()
                .setKeyValues("key1", "value11")
                .setKeyValues("key4", "value14")
                .build();

        loglist.add(log1)
                .add(log2)
                .build();
        response = zeusClient.sendLogs(loglist);
        System.out.println("Logs Post Result " + response.toString());

        sleep(2);
        System.out.println("Retrieving all logs with log name: zeus-test");
        //For list of available parameters, refer API doc http://api.ciscozeus.io 
        //log_name must be the log name to be retrieved
        Parameters log_params = new Parameters();
        log_params.add("log_name", "zeus-test");//Here zeus matches our original metric: zeus-test
        response = zeusClient.retrieveLogs(log_params);
        System.out.println("Logs Get Result " + response.toString());

    }

    public static void sleep(int sec) {
        System.out.println("Sleeping for " + sec + " seconds");
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
