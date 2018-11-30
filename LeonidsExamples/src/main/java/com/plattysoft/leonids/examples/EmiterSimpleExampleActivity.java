package com.plattysoft.leonids.examples;

import android.os.Bundle;
import android.view.View;

import com.plattysoft.leonids.ParticleSystem;

public class EmiterSimpleExampleActivity extends BaseDetailActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_particle_system_example);
		findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		new ParticleSystem(this, 50, R.drawable.star_pink, 1000)
		.setSpeedRange(0.1f, 0.25f)
		.emit(arg0, 100);
	}

}
