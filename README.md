# Java Zeus Client

![Alt text](/docs/images/zeus-logo.png?raw=true "Zeus Logo")

Java client for [Cisco Zeus](http://www.ciscozeus.io/). This allows us to send and receive data to and from Zeus.

## Installation

TODO

## Usage

```java
import com.cisco.zeus.*;
ZeusAPIClient zeusClient = new ZeusAPIClient("your_token_here");
```

List All Metrics

```java
Parameters params = new Parameters();
#  params.add("metric_name", "regular expression to filtering result");
result = zeusClient.retrieveMetricNames(params);
System.out.println(result);  # => ""
```

Get Metric

```java
Parameters params = new Parameters();
#  params.add("metric_name", "regular expression to filtering result");
result = zeusClient.retrieveMetricValues(params);
System.out.println(result);  # => ""
```

Send Metrics

```java
Metric metric = new Metric()
metric.setColumns("col1", "col2", ...)
      .setValues(3, 3, ...)
      .setValues(4, 4, ...)  # you can send multiple metrics
      .build();
String result = zeusClient.sendMetrics("your_metric_name_here", metric);
System.out.println(result);  # => ""
```

Delete Metric

```java
result = zeusClient.deleteMetrics("metric_name_here");
System.out.println(result);  # => ""
```


## Contributing

1. Fork it ( https://github.com/CiscoZeus/java-zeusclient/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request
