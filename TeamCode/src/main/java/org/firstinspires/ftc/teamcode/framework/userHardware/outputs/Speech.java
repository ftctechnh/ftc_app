package org.firstinspires.ftc.teamcode.framework.userHardware.outputs;

import android.speech.tts.TextToSpeech;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Locale;

public class Speech {

    private TextToSpeech mTTS;

    public Speech(HardwareMap hwMap){
        mTTS = new TextToSpeech(hwMap.appContext,null);
        mTTS.setLanguage(Locale.ENGLISH);
    }

    public void speak(String text){
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    public boolean isSpeaking(){
        return mTTS.isSpeaking();
    }
}
