package com.voicehack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by andrew on 11/9/13.
 */
public class SendCommandTask extends AsyncTask<Void, Void, String> {
    public static final String URL_SEND_TASK = "amazonserver/send_task_request";
    private Context context;
    private VoiceHackFragment voiceHackFragment;
    private String task;
    private final String TASK_FAILURE = "Failure";

    public SendCommandTask(Context context, VoiceHackFragment voiceHackFragment, String task) {
        super();
        this.context = context;
        this.voiceHackFragment = voiceHackFragment;
        this.task = task;
    }

    @Override
    protected String doInBackground(Void... nothing) {
        StringBuilder response = new StringBuilder();

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL_SEND_TASK);
        httpPost.setHeader("Content-Type", "application/json");

        //TODO: do fancy processing on the task

        HttpEntity taskEntity;
        try {
            taskEntity = new StringEntity("{'task':" + task + "}");
        } catch (UnsupportedEncodingException e) {
            Log.e("VoiceHack", "Something went wrong while encoding the task: " + e.getMessage());
            return TASK_FAILURE;
        }
        httpPost.setEntity(taskEntity);

        HttpResponse execute;
        try {
            execute = client.execute(httpPost);
        } catch (ClientProtocolException e) {
            Log.e("VoiceHack", "Something went wrong while executing HttpPost: " + e.getMessage());
            return TASK_FAILURE;
        } catch (IOException e) {
            Log.e("VoiceHack", "Something went wrong while executing HttpPost: " + e.getMessage());
            return TASK_FAILURE;
        }

        InputStream content;
        try {
            content = execute.getEntity().getContent();
        } catch (IOException e) {
            Log.e("VoiceHack", "Could not get InputStream from HttpResponse: " + e.getMessage());;
            return TASK_FAILURE;
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
        String s;

        try {
            while ((s = buffer.readLine()) != null) {
                response.append(s);
            }
        } catch (IOException e) {
            Log.e("VoiceHack", "Could not readLine from HttpResponse buffer: " + e.getMessage());
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals(TASK_FAILURE)) {
            Toast.makeText(context, R.string.task_command_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.task_command_success, Toast.LENGTH_SHORT).show();

            SharedPreferences historyPreferences = context.getSharedPreferences("history", Context.MODE_PRIVATE);
            SharedPreferences.Editor historyEditor = historyPreferences.edit();
            historyEditor.putLong(task, new Date().getTime());
            if(!historyEditor.commit()) {
                Toast.makeText(context, R.string.history_error_add, Toast.LENGTH_LONG).show();
            }

            voiceHackFragment.updateHistory();
        }
    }
}