package com.plattysoft.leonids.examples;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.plattysoft.leonids.ParticleSurface;
import com.plattysoft.leonids.initializers.ScaleInitializer;
import com.plattysoft.leonids.modifiers.ExtraSpeedModifier;
import com.plattysoft.leonids.modifiers.ScaleModifier;
import com.plattysoft.leonids.surface.Playground;

public class SnowExampleActivity extends BaseDetailActivity  {
	//private ViewGroup parentView;
    private ExtraSpeedModifier extraSpeedModifier;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snow_example);

		View parentView = findViewById(R.id.effect_layer);

		float widthRange = parentView.getWidth() / 3;
		float randomStart = widthRange;
		float randomRange = widthRange;
		extraSpeedModifier = new ExtraSpeedModifier(randomStart, randomRange, 0, 0);
		parentView.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null == flakingParticle) {
			return;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if (null != flakingParticle) {
			return;
		}

		snowing();
	}

	private ParticleSurface flakingParticle;
	private void snowing() {
        Playground playground = findViewById(R.id.playground_surfaceview);

        Drawable flakingSnow = getResources().getDrawable(R.drawable.christ_snow_piece_bg);
        int width = playground.getWidth();
        int x = (int)(-0.2f * width);
        int snowWidth = width - 2*x;

        int[] parentLocation = new int[2];
        playground.getLocationInWindow(parentLocation);
        int y = parentLocation[1];
        int maxParticles = 90;
        int particlesPerSecond = 10;
        long timeToLive = maxParticles*1000/particlesPerSecond;
        flakingParticle = new ParticleSurface(playground, maxParticles, flakingSnow, timeToLive);

        flakingParticle
                .setSpeedModuleAndAngleRange(0.03f, 0.07f, 60, 120)
                .setAlphaRange(50, 255)
                .addModifier(new ScaleModifier(1f, 1.2f, 0, timeToLive))
                .addModifier(extraSpeedModifier)
                .addInitializer(new ScaleInitializer(0.8f, 1.5f))
                .emitWithGravity(new int[]{x, y}, snowWidth, 0, Gravity.BOTTOM, 10);
    }
}
