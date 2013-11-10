package com.voicehack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VoiceHackFragment extends Fragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private List<String> voiceHistory;
    private ArrayAdapter<String> voiceHistoryArrayAdapter;

    public VoiceHackFragment() {
        voiceHistory = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voicehack, container, false);
        if(rootView == null) {
            return container;
        }

        ListView voiceHistoryList = (ListView) rootView.findViewById(R.id.voice_history_list);
        voiceHistoryArrayAdapter = new VoiceHistoryArrayAdapter(
                getActivity(), R.layout.voice_history_list_item,
                R.id.voice_history_item_text, voiceHistory
        );
        voiceHistoryList.setAdapter(voiceHistoryArrayAdapter);
        voiceHistoryList.setOnItemClickListener(this);
        voiceHistoryList.setOnItemLongClickListener(this);


        updateHistory();

        return rootView;
    }

    public void updateHistory() {
        if(getActivity() == null) {
            return;
        }
        voiceHistory.clear();
        SharedPreferences historyPreferences = getActivity().getSharedPreferences("history", Context.MODE_PRIVATE);

        Map<String, ?> history = historyPreferences.getAll();
        if(history != null) {
            for(String task : history.keySet()) {
                int i = 0;
                for(; i < voiceHistory.size(); ++i) {
                    if(((Long)history.get(voiceHistory.get(i))).compareTo((Long)history.get(task)) < 0) {
                        break;
                    }
                }
                voiceHistory.add(i, task);
            }
        }
        voiceHistoryArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i("VoiceHack", "Item clicked: " + position);

        String inbox = adapterView.getItemAtPosition(position).toString();

        //need to get the command string from the voicehackrecordialogfragment homie
        SendCommandTask sendCommandTask = new SendCommandTask(getActivity(), this, inbox);
        sendCommandTask.execute();




    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i("VoiceHack", "Item long clicked: " + position);
        return false;
    }

    private class VoiceHistoryArrayAdapter extends ArrayAdapter<String> {

        public VoiceHistoryArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                v = layoutInflater.inflate(R.layout.voice_history_list_item, parent, false);
            }
            if(v == null) {
                Log.e("VoiceHack", "Could not create a list view for history item.");
                return parent;
            }

            TextView itemText = (TextView) v.findViewById(R.id.voice_history_item_text);
            itemText.setText(getItem(position));

            return v;
        }
    }
}
