package com.stockrock.analysis.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiCall {
    Builder builder;

    private ApiCall() {
    }

    public ApiCall(Builder builder) {
        this.builder = builder;
    }

    public static Builder Builder() {
        return new Builder();
    }

    public void connect1() {
        if (builder.url == null) {
            if (builder.listener != null) {
                builder.listener.onFailure(new NullPointerException("No URL"));
                return;
            }
        }

        StringBuilder inline = new StringBuilder();

        try {
            assert builder.url != null;
            URL url = new URL(builder.url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(builder.requestMethod);
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setConnectTimeout(3000);
            conn.connect();
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                if (builder.listener != null)
                    builder.listener.onFailure(new RuntimeException("HttpResponseCode: " + responsecode));
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline.append(sc.nextLine());
                }
                if (builder.listener != null) builder.listener.onSuccess(inline.toString());
                sc.close();
            }
        } catch (Exception e) {
            if (builder.listener != null) builder.listener.onFailure(e);
        }

    }

    public void connect(){
        if (builder.url == null) {
            if (builder.listener != null) {
                builder.listener.onFailure(new NullPointerException("No URL"));
                return;
            }
        }
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(builder.url);

        HttpResponse response = null;
        try {
            response = client.execute(request);
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(
                            response.getEntity().getContent()));

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200){
                StringBuilder responseString = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    responseString.append(line);
                }
                if (builder.listener != null) builder.listener.onSuccess(responseString.toString());
            }else{
                if (builder.listener != null) {
                    builder.listener.onFailure(new RuntimeException("HttpResponseCode: " + statusCode));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (builder.listener != null) {
                builder.listener.onFailure(e);
            }
        }
    }

    public interface ApiListener {
        void onSuccess(String responseString);

        void onFailure(Exception e);
    }

    public static class Builder {

        String url;
        String requestMethod = "GET";
        ApiListener listener;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setRequestId(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder setListener(ApiListener listener) {
            this.listener = listener;
            return this;
        }
    }

}
