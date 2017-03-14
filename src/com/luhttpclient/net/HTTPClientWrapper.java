package com.luhttpclient.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Lukas on 14.03.2017.
 */

public class HTTPClientWrapper {
    private boolean processing;

    public HTTPClientWrapper() {

    }

    public void start(String url) {

    }

    public String process(String url) throws IOException, IllegalStateException {
        processing = true;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);

        getRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
        getRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        getRequest.addHeader("Accept-Language", "de,en-US;q=0.7,en;q=0.3");
        getRequest.addHeader("DNT", "1");
        getRequest.addHeader("Connection", "keep-alive");

        HttpResponse response = httpClient.execute(getRequest);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        String output;
        StringBuilder data = new StringBuilder();
        while ((output = br.readLine()) != null) {
            data.append(output);
        }

        processing = false;

        return data.toString();
    }

    public void cancel() {
        processing = false;
    }

    // getters & setters

    public boolean isProcessing() {
        return processing;
    }
}
