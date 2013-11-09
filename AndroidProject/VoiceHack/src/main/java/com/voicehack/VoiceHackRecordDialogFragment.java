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

import java.util.ArrayList;

/**
 * Created by andrew on 11/9/13.
 */
public class VoiceHackRecordDialogFragment extends DialogFragment implements RecognitionListener {

    SpeechRecognizer sr;

    public static VoiceHackRecordDialogFragment newInstance() {
        VoiceHackRecordDialogFragment frag = new VoiceHackRecordDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record_dialog, container, false);

        getDialog().setTitle(R.string.recording_command);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());

        sr = SpeechRecognizer.createSpeechRecognizer(getActivity());
        sr.setRecognitionListener(this);
        sr.startListening(intent);

        Log.i("VoiceHack", "isRecognitionAvailable returns " + sr.isRecognitionAvailable(getActivity()));

       //TODO: start recording here
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        sr.stopListening();

        Log.i("VoiceHack", "onDismissed has been called.");


        //TODO: dismissing, might want to do something
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e("VoiceHack", "Speech has begun.");
    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.e("VoiceHack", "Speech has ended.");
    }

    @Override
    public void onError(int i) {
        Log.e("VoiceHack", "Ohse noseee it's an errorrr");
        switch(i){
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
                Log.e("VoiceHack", "ERROR_SPEECH_TIMEOUT");
                break;

        }

    }

    @Override
    public void onResults(Bundle bundle) {
        Log.e("VoiceHack", "onResults was called");
        ArrayList<String> command = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        for (String derp: command) {
            Log.e("VoiceHack", derp);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
