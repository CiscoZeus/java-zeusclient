# Java Zeus Client

![Alt text](/docs/images/zeus-logo.png?raw=true "Zeus Logo")

[![Build Status](https://travis-ci.org/CiscoZeus/java-zeusclient.svg)](https://travis-ci.org/CiscoZeus/java-zeusclient) 


Java client for [Cisco Zeus](http://www.ciscozeus.io/). This allows us to send and receive data to and from Zeus.


## How to get started
Refer src/examples/ZeusSampleClient.java for a quick introduction. 
Edit the file to add your assigned Zeus access token and execute the following commands to post/get sample logs & metrics.

```
git clone https://github.com/CiscoZeus/java-zeusclient.git
mvn compile
mvn -q exec:java -Dexec.mainClass="com.cisco.zeus.ZeusSampleClient"
```
For more advanced options and examples, refer files in the src/test directory.

## Usage
###Metrics
```java
import com.cisco.zeus.*;
ZeusAPIClient zeusClient = new ZeusAPIClient("your_token_here");
```
#####Post Metrics
Metric name has to start with an alphanumerical variable and it can contain hyphen(-), dot(.) and underscore(_).

```java
MetricList metric = new MetricList("your_metric_name_here");
metric.addColumns("col1", "col2", ...)
      .addValues(3, 3, ...)
      .addValues(4, 4, ...)  // you can send multiple metrics
      .build();
String result = zeusClient.sendMetrics(metric);
System.out.println(result);
```

#####List All Metrics
For more information about parameters, please refer to [Cisco Zeus API Doc](http://www.ciscozeus.io/)
```java
Parameters params = new Parameters();
// params.add("metric_name", "metric_name_regex");
String result = zeusClient.retrieveMetricNames(params);
System.out.println(result);
```

#####Get Metric Values
For more information about parameters, please refer to [Cisco Zeus API Doc](http://www.ciscozeus.io/)
```java
Parameters params = new Parameters();
// params.add("metric_name", "metric_name_regex");
String result = zeusClient.retrieveMetricValues(params);
System.out.println(result); 
```


#####Delete Metric

```java
String result = zeusClient.deleteMetrics("your_metric_name_here");
System.out.println(result); 
```
###Logs

#####Post Logs
Log name must contain only alphanumerical characters.

```java
Log log = new Log()
            .setKeyValues("key1","value1")
            .setKeyValues("key2","value2");

Log log1 = new Log()
            .setKeyValues("key3","value3")
            .setKeyValues("key4","value4");

LogList loglist = new LogList("your_log_name_here")
                  .addLog(log)
                  .addLog(log1);
String result = zeusClient.sendLogs(loglist);
System.out.println(result); 
```

#####Get Logs
For more information about parameters, please refer to [Cisco Zeus API Doc](http://www.ciscozeus.io/)
```java
Parameters params = new Parameters();
// params.add("log_name", "regular expression to filtering result");
String result = zeusClient.retrieveLogs(params);
System.out.println(result);
```

## Contributing

1. Fork it ( https://github.com/CiscoZeus/java-zeusclient/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request
