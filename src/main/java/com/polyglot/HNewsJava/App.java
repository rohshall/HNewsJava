package com.polyglot.HNewsJava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.polyglot.HNewsJava.RssReader;

/**
 * Main App
 *
 */
public class App
{
    public static void main(String[] args)
    {
        try {
            String feed = readUrl("https://news.ycombinator.com/rss");
            ArrayList<HashMap<String, String>> articles = RssReader.read(new StringReader(feed));
            for(HashMap<String, String> article : articles) {
                System.out.println(article.get("title"));
                System.out.println(article.get("link"));
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String readUrl(String url) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } else {
            throw new IOException("Server sent error response: " + statusCode);
        }
    }
}

