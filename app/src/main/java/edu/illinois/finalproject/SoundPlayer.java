package edu.illinois.finalproject;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundPlayer {
    private Context context;
    private SoundPool soundPool;
    int errorSoundId;
    int completedMoveId;
    int buttonPressId;
    int colorSelectId;

    private static final int LEFT_VOLUME = 1;
    private static final int RIGHT_VOLUME = 1;
    private static final int PRIORITY = 1;
    private static final int PLAYBACK_SPEED = 1;
    private static final int LOOP = 0;

    public SoundPlayer(Context context){
        this.context = context;

        SoundPool.Builder soundPoolBuilder =  new SoundPool.Builder();
        soundPoolBuilder.setMaxStreams(2);
        soundPoolBuilder.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());

        soundPool = soundPoolBuilder.build();
        errorSoundId = soundPool.load(context, R.raw.error_beep, PRIORITY);
        completedMoveId =  soundPool.load(context, R.raw.chess_move, PRIORITY);
        buttonPressId =  soundPool.load(context, R.raw.ping_sound, PRIORITY);
        colorSelectId = soundPool.load(context, R.raw.piece_click, PRIORITY);
    }

    public void playErrorSound(){
        soundPool.play(errorSoundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY,LOOP, PLAYBACK_SPEED);
    }

    public void playCompletedMoveSound(){
        soundPool.play(completedMoveId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY,LOOP, PLAYBACK_SPEED);
    }

    public void playButtonPressSound(){
        soundPool.play(buttonPressId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY,LOOP, PLAYBACK_SPEED);
    }

    public void playColorSelectSound(){
        soundPool.play(colorSelectId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY,LOOP, PLAYBACK_SPEED);
    }

}
