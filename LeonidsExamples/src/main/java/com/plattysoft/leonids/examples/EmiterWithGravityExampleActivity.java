package com.plattysoft.leonids.examples;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;

public class EmiterWithGravityExampleActivity extends BaseDetailActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_particle_system_example);
		findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		new ParticleSystem(this, 100, R.drawable.star_pink, 3000)
		.setAcceleration(0.00013f, 90)
		.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
		.setFadeOut(200, new AccelerateInterpolator())
		.emitWithGravity(arg0, Gravity.BOTTOM, 30);
	}
}
