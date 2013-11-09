package com.voicehack;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by andrew on 11/9/13.
 */
public class SendTaskRequest extends AsyncTask<String, Void, String> {
    public static final String URL_SEND_TASK = "amazonserver/send_task_request";

    @Override
    protected String doInBackground(String... tasks) {
        StringBuilder response = new StringBuilder();
        for (String task : tasks) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL_SEND_TASK);
            HttpParams params = httpGet.getParams();
            params.setParameter("task", task);
            httpGet.setParams(params);
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response.append(s);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        //TODO: possibly toast saying it was a success
    }
}