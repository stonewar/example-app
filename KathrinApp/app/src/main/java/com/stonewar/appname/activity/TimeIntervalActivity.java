package com.stonewar.appname.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractBaseActivity;
import com.stonewar.appname.util.Constant;

public class TimeIntervalActivity extends AbstractBaseActivity {

    private static final String TAG = TimeIntervalActivity.class.getName();
    private String stoppingTimeInterval, playingTimeInterval;
    private NumberPicker npPlayInterval, npStopInterval;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView convertedPlayedTime = (TextView) findViewById(R.id.text_converted_playing_time);
        final TextView convertedStopTime = (TextView) findViewById(R.id.text_converted_stopping_time);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
              boolean isPlayingData = msg.getData().getBoolean(Constant.IS_TIME_INTERVAL_PLAYING_DATA);
              String dataValue = msg.getData().getString(Constant.DATA_VALUE);
                if (isPlayingData){
                    convertedPlayedTime.setText(dataValue);
                }
                else {
                    convertedStopTime.setText(dataValue);
                }
            }
        };

        npPlayInterval = (NumberPicker) findViewById(R.id.numberPicker_play_interval);
        npStopInterval = (NumberPicker) findViewById(R.id.numberPicker_stop_interval);

        //Range for accessing the array in the method setDisplayedValues
        // setMinValue(3) setMaxValue(4) (4 - 3 = 1 so access to index 0, 1)
        // setMinValue(4) setMaxValue(10) (10 - 4 = 6 so access to index 0,1,2,3,4,5,6,7)
        npPlayInterval.setMinValue(1);
        npPlayInterval.setMaxValue(360);
        final String[] playValues = calculateValuesToDisplay(360, 10, 10);

        npPlayInterval.setDisplayedValues(playValues);
        npPlayInterval.setWrapSelectorWheel(true);

        npPlayInterval.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, "Old index :" + oldVal);
                Log.d(TAG, "New index :" + newVal);
                playingTimeInterval = playValues[newVal - 1];
                Log.d(TAG, "Selected value :" + playingTimeInterval);
                updatedMinutes(playingTimeInterval, true);
            }
        });

        npStopInterval.setMinValue(1);
        npStopInterval.setMaxValue(180);
        final String[] stopValues = calculateValuesToDisplay(180, 5, 5);

        npStopInterval.setDisplayedValues(stopValues);
        npStopInterval.setWrapSelectorWheel(true);
        npStopInterval.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, "Old index :" + oldVal);
                Log.d(TAG, "New index :" + newVal);
                stoppingTimeInterval = stopValues[newVal - 1];
                Log.d(TAG, "Selected value :" + stoppingTimeInterval);
                updatedMinutes(stoppingTimeInterval, false);
            }
        });

        playingTimeInterval = playValues[npPlayInterval.getValue()- 1];
        stoppingTimeInterval = stopValues[npStopInterval.getValue()- 1];
        updatedMinutes(playingTimeInterval, true);
        updatedMinutes(stoppingTimeInterval, false);
    }

    @Override
    public int contentView() {
        return R.layout.activity_time_interval;
    }

    public void accept(View view){
        Intent intent = new Intent(this, MediaPlayerActivity.class);
        intent.putExtra(Constant.SELECTED_SONGS, getIntent().getParcelableArrayListExtra(Constant.SELECTED_SONGS));
        intent.putExtra(Constant.PLAYING_TIME_INTERVAL, playingTimeInterval);
        intent.putExtra(Constant.STOPPING_TIME_INTERVAL, stoppingTimeInterval);
        startActivity(intent);
    }

    private void updatedMinutes(String selectedValue, boolean isPlayingData){
        double convertedTimeInMinutes = Double.valueOf(selectedValue) / 60;
        double convertedTimeInMinutesRound = Math.floor(convertedTimeInMinutes * 100) / 100;
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_TIME_INTERVAL_PLAYING_DATA, isPlayingData);
        bundle.putString(Constant.DATA_VALUE, String.valueOf(convertedTimeInMinutesRound));
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private String[] calculateValuesToDisplay(int numberOfValues, int firstValue , int jumpStep){
        String[] values = new String[numberOfValues];
        for(int i = 0; i < values.length; i++){
            if(i != 0)
                firstValue += jumpStep;
            values[i] = String.valueOf(firstValue);
        }
        return values;
    }

}
