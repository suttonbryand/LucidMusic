package sutton.bryan.lucidmusic;

/**
 * Created by Bryan on 3/2/14.
 */

import android.os.Bundle;
import android.app.Fragment;

public class PlayerFragment extends Fragment {

    private AlarmTimeManager alarmtimemanager;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    public void setAlarmTimeManager(AlarmTimeManager alarmtimemanager){
        this.alarmtimemanager = alarmtimemanager;
    }

    public AlarmTimeManager getAlarmTimeManager(){
        return this.alarmtimemanager;
    }

}
