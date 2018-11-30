package com.plattysoft.leonids.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

abstract public class BaseDetailActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (null != intent) {
            String title = intent.getStringExtra("title");
            if (null != title) {
                setTitle(title);
            }
        }
    }
}
