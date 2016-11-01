package com.cisco.zeus;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class ZeusAPIAlertTest
{

    /////////////////////////////////////// 
    //Edit the below line to add your token
    //////////////////////////////////////
    //String token = "Your_token_here";
    String token = System.getenv("ZEUS_TOKEN");
 
    String testLogName = UUID.randomUUID().toString();
    ZeusAPIClient zeusClient = new ZeusAPIClient(token);

    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code   
        System.out.println("OnetimeSetup: Setting up ZeusAPI Client for testing logs");
    }
 
    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        System.out.println("oneTimeTearDown: Zeus API Client log testing completed");
    }
   
    @After
    public void tearDown() {
        System.out.println("Testcase teardown"); 
    }
 
    public void sleep(int sec) {
        System.out.println("Sleeping for "+sec+" seconds");
        try {
            Thread.sleep(sec*1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testAlert() throws IOException
    {
        System.out.println("Sending a alert with log name: zeus-test");
        Parameters params = new Parameters()
                .add("alert_name", "value1")
                .add("username", "value2")
                .add("alert_expression", "value3");

        String result = zeusClient.sendAlert(params);
        System.out.println("Alert Post Result "+result);

        sleep(2);
        System.out.println("Retrieving all alerts");
        result = zeusClient.retrieveAlerts();
        System.out.println("Alerts Get Result "+result);

        System.out.println("Retrieving a alert with id: 1");
        result = zeusClient.retrieveAlert(1);
        System.out.println("Alert Get Result "+result);

        System.out.println("Updating a alert with id: 1");
        params = new Parameters()
                .add("alert_name", "value1")
                .add("username", "value2")
                .add("alert_expression", "value3");
        result = zeusClient.updateAlert(1, params);
        System.out.println("Alert Put Result "+result);
        sleep(2);
        System.out.println("Deleting alert with id: 1");
        result = zeusClient.deleteAlert(1);
        System.out.println("Alert Delete Result "+result);
    }
}
