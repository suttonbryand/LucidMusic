package sutton.bryan.lucidmusic;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Bryan on 3/2/14.
 */
public class AlarmTimeManager implements TimePickerDialog.OnTimeSetListener {

    private long triggerTime;
    private LucidPlayer lucidplayer;
    private Activity activity;
    private Context context;

    public AlarmTimeManager(Activity a){
        lucidplayer = new LucidPlayer();
        lucidplayer.setTimeToMaxVolume(21600);
        activity = a;
        context = a.getApplicationContext();
    }

    public LucidPlayer getLucidPlayer(){
        return this.lucidplayer;
    }

    public void save(View view){

        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preferences), 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(context.getString(R.string.preference_duration), getDuration());
        editor.putFloat(context.getString(R.string.preference_max_volume), getMaxVolume());
        editor.commit();

        Intent intent = new Intent(context.getApplicationContext(), AlarmActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent alarmIntent = PendingIntent.getActivity(context, 0, intent, 0);

        int alarmType = AlarmManager.RTC_WAKEUP;
        AlarmManager alarmmanager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmmanager.setExact(AlarmManager.RTC_WAKEUP, this.triggerTime, alarmIntent);
    }

    public void updateCurrentPlayer(View view){
        int duration = getDuration();
        float max_volume = getMaxVolume();
        lucidplayer.save(duration, max_volume);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, i2);
        calendar.set(Calendar.SECOND, 0);

        this.triggerTime = calendar.getTimeInMillis();

    }

    private int getDuration(){
        // Get the duration
        NumberPicker hours_duration      = (NumberPicker) activity.findViewById(R.id.hours);
        NumberPicker minutes_duration    = (NumberPicker) activity.findViewById(R.id.minutes);
        return (hours_duration.getValue() * 60 * 60) + (minutes_duration.getValue() * 60);
    }

    private float getMaxVolume(){
        // Get the max volume
        SeekBar max_volume_bar   = (SeekBar) activity.findViewById(R.id.maxvol_seekbar);
        android.util.Log.w("set volume is " , "" + max_volume_bar.getProgress());
        float max_volume = max_volume_bar.getProgress() / (float)100;
        android.util.Log.w("progress is  " , "" + max_volume_bar.getProgress());
        android.util.Log.w("passing volume " , "" + max_volume);
        return max_volume;
    }
}
