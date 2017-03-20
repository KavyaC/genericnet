package com.temenos.dshubhamrajput.genericnet;

/**
 * Created by Administrator on 20-02-2017.
 */

import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class HttpHandler {

    HttpHandler() {
    }

    String makeServiceCall(String reqUrl) {
        String response = null;
        String basicAuth;
        try {
            URL e = new URL(reqUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)e.openConnection();
            String userPass = "CREDITMGR" + ":" + "123456";
            basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertStreamToString(in);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return response;
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            try {
                while((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        return sb.toString();
    }

    String postfunc(String reurl, String jsonstring)
    {
       String response = "";
        try {

            URL e = new URL(reurl);
            HttpURLConnection urlConnectio = (HttpURLConnection) e.openConnection();
            urlConnectio.setDoOutput(true);
            String basicAuth;
            String userPass;

            userPass = "CREDITMGR" + ":" + "123456";
            basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
            urlConnectio.setRequestProperty("Authorization", basicAuth);
            urlConnectio.setRequestProperty("Accept", "application/json");
            urlConnectio.setRequestProperty("Content-Type", "application/json");
            urlConnectio.setRequestMethod("POST");
            urlConnectio.connect();
            OutputStreamWriter out = new OutputStreamWriter(urlConnectio.getOutputStream());
            out.write(jsonstring);// here i sent the parameter
            out.flush();
            out.close();


            int responseCode=urlConnectio.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
               response="YES";
            }
            else {
                response="NO";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // 11. return result
        return response;
    }

    String posCommit(String reurl, String jsonstring) {
        String response="";
        try {
            URL commit = new URL(reurl);
            HttpURLConnection urlcommit = (HttpURLConnection) commit.openConnection();
            urlcommit.setDoOutput(true);
            String basicAuth;
            String userPass;
            userPass = "CREDITMGR" + ":" + "123456";
            basicAuth = "Basic " + new String((new Base64()).encode(userPass.getBytes()));
            urlcommit.setRequestProperty("Authorization", basicAuth);
            urlcommit.setRequestProperty("Accept", "application/json");
            urlcommit.setRequestProperty("Content-Type", "application/json");
            urlcommit.setRequestMethod("POST");
            urlcommit.connect();
            OutputStreamWriter commitout = new OutputStreamWriter(urlcommit.getOutputStream());
            commitout.write(jsonstring);// here i sent the parameter
            commitout.flush();
            commitout.close();

            int commitresponse = urlcommit.getResponseCode();
            if (commitresponse == HttpsURLConnection.HTTP_CREATED) {
                response="YES";
            }
            else {
                response="NO";
            }
        }
        catch (Exception commit){
            commit.printStackTrace();
        }
        return response;
    }
}
