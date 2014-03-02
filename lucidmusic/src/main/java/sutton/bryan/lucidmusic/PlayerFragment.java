package sutton.bryan.lucidmusic;

/**
 * Created by Bryan on 3/2/14.
 */

import android.os.Bundle;
import android.app.Fragment;

public class PlayerFragment extends Fragment {

    private LucidPlayer lucidplayer;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    public void setLucidPlayer(LucidPlayer player){
        this.lucidplayer = player;
    }

    public LucidPlayer getLucidPlayer(){
        return this.lucidplayer;
    }

}
