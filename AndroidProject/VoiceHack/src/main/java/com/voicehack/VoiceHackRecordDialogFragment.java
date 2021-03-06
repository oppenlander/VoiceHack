package com.voicehack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by andrew on 11/9/13.
 */
public class VoiceHackRecordDialogFragment extends DialogFragment implements RecognitionListener {

    private SpeechRecognizer speechRecognizer;
    private VoiceHackFragment voiceHackFragment;
    private Button cancelButton, doneButton;

    public static VoiceHackRecordDialogFragment newInstance(VoiceHackFragment voiceHackFragment) {
        return new VoiceHackRecordDialogFragment(voiceHackFragment);
    }

    public VoiceHackRecordDialogFragment() {
        this.voiceHackFragment = null;
    }

    public VoiceHackRecordDialogFragment(VoiceHackFragment voiceHackFragment) {
        this.voiceHackFragment = voiceHackFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record_dialog, container, false);

        getDialog().setTitle(R.string.recording_command);

        View.OnClickListener cancelListener = new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        };

        View.OnClickListener doneListener = new View.OnClickListener() {
            public void onClick(View view) {
                speechRecognizer.stopListening();
                cancelButton.setEnabled(false);
                doneButton.setEnabled(false);

            }
        };

        cancelButton = (Button)v.findViewById(R.id.cancelButton);
        doneButton = (Button)v.findViewById(R.id.doneButton);

        cancelButton.setOnClickListener(cancelListener);
        doneButton.setOnClickListener(doneListener);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speechRecognizer.setRecognitionListener(this);
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        speechRecognizer.stopListening();
        speechRecognizer.cancel();
        speechRecognizer.destroy();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        //Do nothing
    }

    @Override
    public void onBeginningOfSpeech() {
        //Do nothing
    }

    @Override
    public void onRmsChanged(float v) {
        //Do nothing
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        //Do nothing
    }

    @Override
    public void onEndOfSpeech() {
        //Do nothing
    }

    @Override
    public void onError(int errorCode) {
        switch(errorCode){
            case SpeechRecognizer.ERROR_AUDIO:
                Log.e("VoiceHack", "Audio error.");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                Log.e("VoiceHack", "ERROR_CLIENT.");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                Log.e("VoiceHack", "ERROR_INSUFFICIENT_PERMISSIONS.");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                Log.e("VoiceHack", "ERROR_NETWORK.");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                Log.e("VoiceHack", "ERROR_NETWORK_TIMEOUT");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                Log.e("VoiceHack", "ERROR_NO_MATCH");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Log.e("VoiceHack", "ERROR_RECOGNIZER_BUSY");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                Log.e("VoiceHack", "ERROR_SERVER");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Toast.makeText(super.getActivity(), R.string.task_ambiguous_error, Toast.LENGTH_LONG).show();
                Log.e("VoiceHack", "ERROR_SPEECH_TIMEOUT");
                break;
        }
        dismiss();
    }

    @Override
    public void onResults(Bundle bundle) {
        Log.e("VoiceHack", "onResults was called");
        ArrayList<String> sentences = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);



/*
            for (String tokens: commands) {
            Log.e("VoiceHack", tokens);
        }
*/
        if(sentences != null && sentences.size() > 0 && voiceHackFragment != null) {
            //TODO: look at commands and choose the best one, instead of the first one
            SendCommandTask sendCommandTask = new SendCommandTask(getActivity(), voiceHackFragment, sentences.get(0));
            sendCommandTask.execute();
        }
        dismiss();
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        //Do nothing
        Log.e("VoiceHack", "onPartialResults has " + "");
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        //Do nothing
    }
}
