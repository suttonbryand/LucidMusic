package sutton.bryan.lucidmusic;

import android.media.*;
import java.util.Calendar;
import android.app.AlarmManager;
import android.content.Context;
import java.io.IOException;


/**
 * Created by Bryan on 2/22/14.
 */
public class LucidPlayer extends MediaPlayer{

    private int status;
    private double volume_increment;
    private double current_volume;
    private float  max_volume;
    private Thread t;

    public final static int STATUS_PLAYING = 1;
    public final static int STATUS_PAUSED  = 2;
    public final static int STATUS_STOPPED = 3;

    private final String location    = "/storage/sdcard0/Music/fires.mp3";
    private int          delay       = 0;

    public LucidPlayer(){

        status = STATUS_STOPPED;
        max_volume = (float) .5;
        resetValues();
        super.setLooping(true);
        try {
            super.setAudioStreamType(AudioManager.STREAM_MUSIC);
            super.setDataSource(location);
        }
        catch (IOException e) {
            android.util.Log.w("LucidPlayer",e.toString());
        }
    }

    public void start(){
        switch(status){
            case STATUS_PLAYING:
                return;
            case STATUS_PAUSED:
                status = STATUS_PLAYING;
                this.play();
                break;
            case STATUS_STOPPED:
                status = STATUS_PLAYING;
                try {
                    super.prepare();
                    super.seekTo(0);
                    this.play();
                } catch (IOException e) {

                }
        }
    }

    public void pause(){
        switch(status){
            case STATUS_PLAYING:
                status = STATUS_PAUSED;
                super.pause();
                break;
            case STATUS_PAUSED:
                return;
            case STATUS_STOPPED:
                return;
        }
    }

    public void stop(){
        switch(status){
            case STATUS_PLAYING:
                android.util.Log.w("LucidPlayer","Stopping");
                status = STATUS_STOPPED;
                super.stop();
                break;
            case STATUS_PAUSED:
                status = STATUS_STOPPED;
                super.stop();
                break;
            case STATUS_STOPPED:
                android.util.Log.w("LucidPlayer","Not Stopping");
                return;
        }
    }

    public void save(double seconds_to_max, float max_volume_arg){
        // TODO: set time

        max_volume = max_volume_arg;
        setTimeToMaxVolume(seconds_to_max);


    }

    public void setTimeToMaxVolume(double seconds){
        if(seconds < 1){
            volume_increment = max_volume;
        }
        else{
            volume_increment = 1 / seconds;
        }
    }

    private void play(){
        super.start();
        resetValues();
        t.start();
    }

    private void resetValues(){
        current_volume = 0;
        final LucidPlayer player = this;
        super.setVolume(0,0);
        t = new Thread(){

            public void run(){
                float max_vol = max_volume;
                double vol_inc = volume_increment;
                android.util.Log.w("LucidPlayer","volume is " + current_volume + "out of " + max_vol);
                while(current_volume < max_vol && status != STATUS_STOPPED){
                    android.util.Log.w("LucidPlayer","volume is " + current_volume + "out of " + max_vol);
                    if(status != STATUS_PAUSED && delay == 0){
                        current_volume += vol_inc;
                        player.setVolume((float)current_volume, (float)current_volume);
                    }
                    else if(delay > 0){
                        delay--;
                    }
                    try {
                        sleep(1000);
                    }
                    catch (InterruptedException e) {
                    }
                }
                android.util.Log.w("LucidPlayer","volume is " + current_volume + "out of " + max_vol);
            }
        };
    }



}
