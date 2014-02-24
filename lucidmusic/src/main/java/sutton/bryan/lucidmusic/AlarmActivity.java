package sutton.bryan.lucidmusic;

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

    private LucidPlayer lucidplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences), 0);
        int duration = sharedPref.getInt(getString(R.string.preference_duration),0);
        float max_volume = sharedPref.getFloat(getString(R.string.preference_max_volume), 1);

        android.util.Log.w("read max_volume " , "" + max_volume);

        lucidplayer = new LucidPlayer();
        lucidplayer.save(duration,max_volume);
        lucidplayer.start();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
        lucidplayer.start();
    }

    public void pause(View view){
        lucidplayer.pause();
    }

    public void stop(View view){
        android.util.Log.w("LucidPlayer","Main Stop");
        lucidplayer.stop();

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
