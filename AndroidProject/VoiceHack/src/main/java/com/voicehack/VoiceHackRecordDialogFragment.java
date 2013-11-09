package com.voicehack;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by andrew on 11/9/13.
 */
public class VoiceHackRecordDialogFragment extends DialogFragment {

    public static VoiceHackRecordDialogFragment newInstance() {
        VoiceHackRecordDialogFragment frag = new VoiceHackRecordDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record_dialog, container, false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO: start recording here
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        //TODO: dismissing, might want to do something
    }
}
