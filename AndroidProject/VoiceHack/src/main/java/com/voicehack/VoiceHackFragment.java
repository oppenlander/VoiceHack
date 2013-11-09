package com.voicehack;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class VoiceHackFragment extends Fragment {

    private Button voiceButton;

    public VoiceHackFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voicehack, container, false);
        if(rootView == null) {
            return container;
        }

        voiceButton = (Button) rootView.findViewById(R.id.voice_button);
        voiceButton.setOnClickListener(voiceButtonClickListener);

        return rootView;
    }

    private View.OnClickListener voiceButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogFragment dialogFragment = VoiceHackRecordDialogFragment.newInstance();
            dialogFragment.show(getFragmentManager(), "record_dialog");
        }
    };
}
