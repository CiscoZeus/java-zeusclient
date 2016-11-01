package com.cisco.zeus;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class ZeusAPITrigalertTest
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
    public void testTrgalert() throws IOException
    {
        System.out.println("Retrieving all trigalerts");
        String result = zeusClient.retrieveTrigalerts();
        System.out.println("Alerts Get Result "+result);

        System.out.println("Retrieving all trigalertLast24s");
        result = zeusClient.retrieveTrigalertsLast24();
        System.out.println("Alert Get Result "+result);

    }
}
