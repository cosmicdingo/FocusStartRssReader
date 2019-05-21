package com.example.focusstartrssreader.network;

import android.util.Log;

import com.example.focusstartrssreader.helper.Contract;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkConnection {

    public HttpURLConnection getHttpConnection(String urlString) {

        if(!urlString.startsWith("http://") && !urlString.startsWith("https://"))
            urlString = "http://" + urlString;

        HttpURLConnection httpConn = null;

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
        }
        catch (MalformedURLException ex) {
            Log.d(Contract.MALFORMED_URL_EXCEPTION, ex.getMessage());
        }
        catch (IOException ex) {
            Log.d(Contract.IO_EXCEPTION, ex.getMessage());
        }
        return httpConn;
    }
}
