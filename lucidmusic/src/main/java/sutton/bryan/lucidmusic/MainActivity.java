package sutton.bryan.lucidmusic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.*;
import android.widget.*;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity {

    private LucidPlayer lucidplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lucidplayer = new LucidPlayer();
        lucidplayer.setTimeToMaxVolume(21600);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void play(View view){
        android.util.Log.w("LucidPlayer","Main Play");
        updateCurrentPlayer(view);
        lucidplayer.start();
    }

    public void pause(View view){
        lucidplayer.pause();
    }

    public void stop(View view){
        android.util.Log.w("LucidPlayer","Main Stop");
        lucidplayer.stop();

    }

    public void updateCurrentPlayer(View view){
        int duration = getDuration(view);
        float max_volume = getMaxVolume(view);
        lucidplayer.save(duration,max_volume);
    }

    public void save(View view){
        // Get the start time
        TimePicker time = (TimePicker) findViewById(R.id.timepicker_startime);
        int hour        = time.getCurrentHour();
        int minute     = time.getCurrentMinute();

        int duration = getDuration(view);

        float max_volume = getMaxVolume(view);

        // Schedule the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent alarmIntent = PendingIntent.getActivity(this, 0, intent, 0);

        int alarmType = AlarmManager.RTC_WAKEUP;
        AlarmManager alarmmanager = (AlarmManager) getSystemService(this.ALARM_SERVICE);
        alarmmanager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        // Save the settings
        SharedPreferences pref = getSharedPreferences(getString(R.string.preferences), 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(getString(R.string.preference_duration), duration);
        editor.putFloat(getString(R.string.preference_max_volume), max_volume);
        editor.commit();

        // Save to the current lucid player
        lucidplayer.save(duration, max_volume);



        android.util.Log.w("LucidPlayer","saved");

    }

    public void test(View view){
        Intent intent = new Intent(this,AlarmActivity.class);
        startActivity(intent);
        finish();
    }

    private int getDuration(View view){
        // Get the duration
        NumberPicker hours_duration      = (NumberPicker) findViewById(R.id.hours);
        NumberPicker minutes_duration    = (NumberPicker) findViewById(R.id.minutes);
        return (hours_duration.getValue() * 60 * 60) + (minutes_duration.getValue() * 60);
    }

    private float getMaxVolume(View view){
        // Get the max volume
        SeekBar max_volume_bar   = (SeekBar) findViewById(R.id.maxvol_seekbar);
        android.util.Log.w("set volume is " , "" + max_volume_bar.getProgress());
        float max_volume = max_volume_bar.getProgress() / (float)100;
        android.util.Log.w("progress is  " , "" + max_volume_bar.getProgress());
        android.util.Log.w("passing volume " , "" + max_volume);
        return max_volume;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            NumberPicker hours      = (NumberPicker) rootView.findViewById(R.id.hours);
            NumberPicker minutes    = (NumberPicker) rootView.findViewById(R.id.minutes);

            hours.setMaxValue(24);
            minutes.setMaxValue(60);
            return rootView;
        }
    }

}
