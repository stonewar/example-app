package com.stonewar.appname.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stonewar.appname.R;

/**
 * Created by yandypiedra on 18.11.15.
 */
public abstract class AbstractBaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(contentView());
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled());
        getSupportActionBar().setHomeButtonEnabled(isHomeButtonEnabled());
    }

    public abstract int contentView();

    public boolean isDisplayHomeAsUpEnabled(){
        return true;
    }

    public boolean isHomeButtonEnabled(){
        return true;
    }

}
