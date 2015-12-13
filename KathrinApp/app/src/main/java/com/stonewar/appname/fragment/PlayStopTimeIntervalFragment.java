package com.stonewar.appname.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stonewar.appname.R;
import com.stonewar.appname.custom.component.CircleTimerView;
import com.stonewar.appname.util.Constant;

/**
 * Created by yandypiedra on 13.12.15.
 */
public class PlayStopTimeIntervalFragment extends Fragment {

    public interface PlayStopTimeIntervalListener {
        void onTimerValueChanged(int i);
    }

    private PlayStopTimeIntervalListener playStopTimeIntervalListener;
    private String information;
    private String description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            information = arguments.getString(Constant.INFORMATION);
            description = arguments.getString(Constant.DESCRIPTION);
        }
    }

    public static PlayStopTimeIntervalFragment createInstance(String information, String description){
        Bundle arguments = new Bundle();
        arguments.putString(Constant.INFORMATION, information);
        arguments.putString(Constant.DESCRIPTION, description);
        PlayStopTimeIntervalFragment fragment = new PlayStopTimeIntervalFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_stop_time_interval,container, false);

        TextView textViewInfo = (TextView) view.findViewById(R.id.text_view_information);
        textViewInfo.setText(information);

        CircleTimerView circleTimerView = (CircleTimerView) view.findViewById(R.id.ctv_play);
        circleTimerView.setDescription(description);

        circleTimerView.setCircleTimerListener(new CircleTimerView.CircleTimerListener() {
            @Override
            public void onTimerStop() {}

            @Override
            public void onTimerStart(int i) {}

            @Override
            public void onTimerPause(int i) {}

            @Override
            public void onTimerValueChanged(int i) {
                Log.d("onTimerValueChanged", "" + i);
                playStopTimeIntervalListener.onTimerValueChanged(i);
            }

            @Override
            public void onTimerValueChange(int i) {}
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlayStopTimeIntervalListener) {
            playStopTimeIntervalListener = (PlayStopTimeIntervalListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PlayStopTimeIntervalListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        playStopTimeIntervalListener = null;
    }
}
