package com.stonewar.appname.activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.stonewar.appname.common.AbstractBaseActivity;
import com.stonewar.appname.R;
import com.stonewar.appname.fragment.PlayStopTimeIntervalFragment;
import com.stonewar.appname.manager.FragmentManager;
import com.stonewar.appname.util.Constant;

public class PlayStopTimeIntervalActivity extends AbstractBaseActivity
        implements PlayStopTimeIntervalFragment.PlayStopTimeIntervalListener{

    private static final String TAG = PlayStopTimeIntervalActivity.class.getName();

    private Button nextAcceptButton;
    private boolean isPlayingTimeSet, isStopTimeSet;
    private int playTimeInterval, stopTimeInterval;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlayStopTimeIntervalFragment playTimeIntervalFragment =
                PlayStopTimeIntervalFragment.createInstance("Select playing time interval", "Playing Interval");
        FragmentManager.replaceFragment(R.id.layout_play_stop_interval, playTimeIntervalFragment, getFragmentManager(), 0 , 0);

        nextAcceptButton = (Button) findViewById(R.id.button_nextAccept);
        nextAcceptButton.setText("Next");
        nextAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlayingTimeSet && isStopTimeSet){
                    Log.d(TAG, "Starting the activity");
                    Intent intent = new Intent(PlayStopTimeIntervalActivity.this, Main2Activity.class);
                    intent.putExtra(Constant.PLAYING_TIME_INTERVAL, playTimeInterval);
                    intent.putExtra(Constant.STOPPING_TIME_INTERVAL, stopTimeInterval);
                    startActivity(intent);
                }
                else if(isPlayingTimeSet){
                    Log.d(TAG, "Starting stopTimeIntervalFragment");
                    PlayStopTimeIntervalFragment  stopTimeIntervalFragment =
                            PlayStopTimeIntervalFragment.createInstance("Select stopping time interval", "Stopping Interval");
                    FragmentManager.replaceFragment(R.id.layout_play_stop_interval, stopTimeIntervalFragment, getFragmentManager(), R.animator.slide_in_from_bottom,R.animator.slide_out_to_bottom);
                    nextAcceptButton.setText("Accept");
                }
            }
        });
    }

    @Override
    public int contentView() {
        return R.layout.activity_play_stop_time_interval;
    }

    public boolean isDisplayHomeAsUpEnabled(){
        return false;
    }

    public boolean isHomeButtonEnabled(){
        return false;
    }

    @Override
    public void onTimerValueChanged(int i) {
        if(!isPlayingTimeSet){
            playTimeInterval = i;
            isPlayingTimeSet = true; //TODO see this bug
            Log.d(TAG, "playing time interval set: "+playTimeInterval);
        }
        else {
            stopTimeInterval = i;
            isStopTimeSet = true;
            Log.d(TAG, "stop time interval set: "+stopTimeInterval);
        }
    }
}
