package com.example.focusstartrssreader.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkConnection {

    private static final String MALFORMED_URL_EXCEPTION = "MalformedUrlException";
    private static final String IO_EXCEPTION = "IOException";

    public static InputStream getInputStream(String urlString) {

        HttpURLConnection httpConn = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect(); // попытка соединения с сервером
            int response = httpConn.getResponseCode(); // код ответа от сервера
            if (response == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        }
        catch (MalformedURLException ex) {
            Log.d(MALFORMED_URL_EXCEPTION, ex.getMessage());
        }
        catch (IOException ex) {
            Log.d(IO_EXCEPTION, ex.getMessage());
        }
        finally {
            httpConn.disconnect();
        }
        return inputStream;
    }
}
