package sutton.bryan.lucidmusic;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    private PlayerFragment playerfragment;
    private AlarmTimeManager alarmtimemanager;

    static final int PICK_FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();

        }

        // Create the fragment for containing the Alarm Manager
        FragmentManager fm = getFragmentManager();
        playerfragment = (PlayerFragment) fm.findFragmentByTag("alarmtimemanager");

        if(playerfragment == null){
            alarmtimemanager = new AlarmTimeManager();
            playerfragment = new PlayerFragment();
            fm.beginTransaction().add(playerfragment, "alarmtimemanager").commit();
            playerfragment.setAlarmTimeManager(alarmtimemanager);
        }

        // Set the Alarm Time Manager to contain this activity
        alarmtimemanager = playerfragment.getAlarmTimeManager();
        alarmtimemanager.setActivity(this);
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

    public void fileBrowser(View view){
        Intent fileBrowserIntent = new Intent(this,ListFileActivity.class);
        startActivityForResult(fileBrowserIntent, PICK_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_FILE) {
            if (resultCode == RESULT_OK) {
                android.util.Log.w("LucidPlayer" , "path is " + data.getStringExtra("result"));
                alarmtimemanager.getLucidPlayer().setLocation(data.getStringExtra("result"));
            }
        }
    }

    public void play(View view){
        android.util.Log.w("LucidPlayer","Main Play");
        updateCurrentPlayer(view);
        alarmtimemanager.getLucidPlayer().start();
    }

    public void pause(View view){
        alarmtimemanager.getLucidPlayer().pause();
    }

    public void stop(View view){
        android.util.Log.w("LucidPlayer","Main Stop");
        alarmtimemanager.getLucidPlayer().stop();

    }

    public void updateCurrentPlayer(View view){
        alarmtimemanager.updateCurrentPlayer(view);
    }

    public void save(View view){
        alarmtimemanager.save(view);



        android.util.Log.w("LucidPlayer","saved");

    }

    public void openTimePickerDialog(View view){
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        int hour   = calendar.get(Calendar.HOUR_OF_DAY);

        TimePickerDialog dialog = new TimePickerDialog(this, alarmtimemanager, hour, minute, false);
        dialog.show();
    }

    public void openDurationDialog(View view){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View durationDialogView = inflater.inflate(R.layout.dialog_duration, null);
        build.setView(durationDialogView)
                .setPositiveButton(R.string.ok, alarmtimemanager);
        AlertDialog dialog = build.create();
        dialog.show();
        alarmtimemanager.setDurationView(durationDialogView);
    }


    public void test(View view){
        Intent intent = new Intent(this,AlarmActivity.class);
        startActivity(intent);
        finish();
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
            return rootView;
        }
    }

}
