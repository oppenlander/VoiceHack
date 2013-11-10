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
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String URL_SEND_TASK = "http://ec2-204-236-209-134.compute-1.amazonaws.com:5000/put_task";
    private Context context;
    private VoiceHackFragment voiceHackFragment;
    private String taskString;
    private final String TASK_FAILURE = "failure";
    private final String AMBIGUOUS_FAILURE = "ambiguous command";

    public SendCommandTask(Context context, VoiceHackFragment voiceHackFragment, String taskString) {
        super();
        this.context = context;
        this.voiceHackFragment = voiceHackFragment;
        this.taskString = taskString;
    }

    @Override
    protected String doInBackground(Void... nothing) {
        StringBuilder response = new StringBuilder();

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL_SEND_TASK);

        //TODO: do fancy processing on the taskString
        //begin interpreting
        String noun = "non", verb = "vrb", adjective = "adj";
        int doorVerbCounter = 0, lightVerbCounter = 0;
        int nounCounter = 0;
        int doorAdjectiveCounter = 0, lightAdjectiveCounter = 0;

        String[] tokens = taskString.split("\\s+"); //assuming first result is best

        for (String token: tokens) {
            Log.e("VoiceHack", token);
        }

        for (String token: tokens) {
            if (token.equals("open") || token.equals("close") || token.equals("lock") || token.equals("unlock")) {
                verb = token;
                doorVerbCounter++;
            }

            if (token.equals("on") || token.equals("off")) {
                verb = token;
                lightVerbCounter++;
            }

            if (token.equals("door") || token.equals("doors")) {
                noun = "door";
                nounCounter++;
            }

            if (token.equals("light") || token.equals("lights")) {
                noun = "light";
                nounCounter++;
            }

            if (token.equals("garage") || token.equals("front") || token.equals("all")) {
                adjective = token;
                doorAdjectiveCounter++;
            }

            if (token.equals("1") || token.equals("one") || token.equals("2") || token.equals("two") || token.equals("3") || token.equals("all")) {
                adjective = token;
                lightAdjectiveCounter++;
            }
        }

        String outbox = "";

        if (doorVerbCounter == 1 && doorAdjectiveCounter == 1 && noun.equals("door")) {
            outbox = verb + " " + noun + " " + adjective;
        }

        else if (lightVerbCounter == 1 && lightAdjectiveCounter == 1 && noun.equals("light")) {
            outbox = verb + " " + noun + " " + adjective;
        }

        else {
            outbox = verb + " " + noun + " " + adjective;
            Log.e("VoiceHack", "The closest outbox is " + outbox);
            Log.e("VoiceHack", "doorVerbCounter is " + doorVerbCounter + ", doorAjectiveCounter is " + doorAdjectiveCounter + ", and noun is " + noun);
            Log.e("VoiceHack", "lightVerbCounter is " + lightVerbCounter + ", lightAdjectiveCounter is " + lightAdjectiveCounter + ", and noun is " + noun);
            return AMBIGUOUS_FAILURE;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("command", taskString);
        } catch (JSONException e) {
            Log.e("VoiceHack", "Something went wrong while encoding jsonObject as string: " + e.getMessage());
            return TASK_FAILURE;
        }
        HttpEntity taskEntity;
        try {
            taskEntity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            Log.e("VoiceHack", "Something went wrong while encoding the jsonObject: " + e.getMessage());
            return TASK_FAILURE;
        }
        httpPost.setEntity(taskEntity);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");

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
            Log.e("VoiceHack", "Could not get InputStream from HttpResponse: " + e.getMessage());
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
            return TASK_FAILURE;
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals(AMBIGUOUS_FAILURE)) {
            Toast.makeText(context, R.string.task_ambiguous_error, Toast.LENGTH_LONG).show();
        }

        else if(result.equals(TASK_FAILURE)) {
            //Log.e("VoiceHack", "The outbox you sent is " + taskString);
            Toast.makeText(context, R.string.task_command_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.task_command_success, Toast.LENGTH_SHORT).show();

            SharedPreferences historyPreferences = context.getSharedPreferences("history", Context.MODE_PRIVATE);
            SharedPreferences.Editor historyEditor = historyPreferences.edit();
            historyEditor.putLong(taskString, new Date().getTime());
            if(!historyEditor.commit()) {
                Toast.makeText(context, R.string.history_error_add, Toast.LENGTH_LONG).show();
            }

            voiceHackFragment.updateHistory();
        }
    }
}