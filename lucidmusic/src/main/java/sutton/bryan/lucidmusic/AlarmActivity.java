package sutton.bryan.lucidmusic;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.os.Build;

public class AlarmActivity extends ActionBarActivity {

    private PlayerFragment playerfragment;
    private AlarmTimeManager alarmtimemanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        FragmentManager fm = getFragmentManager();
        playerfragment = (PlayerFragment) fm.findFragmentByTag("alarmtimemanager");

        if(playerfragment == null){
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences), 0);
            int duration = sharedPref.getInt(getString(R.string.preference_duration),0);
            float max_volume = sharedPref.getFloat(getString(R.string.preference_max_volume), 1);

            alarmtimemanager = new AlarmTimeManager();
            alarmtimemanager.getLucidPlayer().save(duration,max_volume);

            playerfragment = new PlayerFragment();
            fm.beginTransaction().add(playerfragment, "alarmtimemanager").commit();
            playerfragment.setAlarmTimeManager(alarmtimemanager);
        }

        alarmtimemanager = playerfragment.getAlarmTimeManager();
        alarmtimemanager.setActivity(this);
        alarmtimemanager.getLucidPlayer().start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm, menu);
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
        alarmtimemanager.getLucidPlayer().start();
    }

    public void pause(View view){
        alarmtimemanager.getLucidPlayer().pause();
    }

    public void stop(View view){
        android.util.Log.w("LucidPlayer","Main Stop");
        alarmtimemanager.getLucidPlayer().stop();

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
            View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
            return rootView;
        }
    }

}
