package spring.controller;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by debasish paul on 07-05-2018.
 */
public class AppControllerTest {
    String url = "http://localhost:8080/";

    @Test
    public void hello() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url + "hello");
        HttpResponse response = client.execute(request);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        assertEquals(result.toString(), "hello world");
    }

    @Test
    public void getAllCacheData() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url + "cache?cache=my-cache");
        HttpResponse response = client.execute(request);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Test
    public void putCacheData() throws Exception {


        LocalDateTime start = LocalDateTime.now();

        for (int i = 0; i < 300000; i++) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPut request = new HttpPut(url + "putString");
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("cache", "my-cache"));
            urlParameters.add(new BasicNameValuePair("key", String.valueOf(i)));
            urlParameters.add(new BasicNameValuePair("value", String.valueOf(i)));
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            client.execute(request);
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Time taken: " + Duration.between(start, end));
    }

}