package com.plattysoft.leonids.examples;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.initializers.ScaleInitializer;
import com.plattysoft.leonids.modifiers.AlphaModifier;
import com.plattysoft.leonids.modifiers.ExtraSpeedModifier;
import com.plattysoft.leonids.modifiers.ScaleModifier;

public class SnowExampleActivity extends BaseDetailActivity  {
	private ViewGroup parentView;
    private ExtraSpeedModifier extraSpeedModifier;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snow_example);

		parentView = findViewById(R.id.effect_layer);

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

		isSnowing = true;
//		startSnowing();
		//snowing();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isSnowing = false;
	}

	public void startSnowing() {
		new ParticleSystem(this, 4, R.drawable.christ_snow_piece_bg, 3000)
		.setSpeedByComponentsRange(-0.025f, 0.025f, -0.06f, -0.08f)		
		.setAcceleration(0.00001f, 30)
		.setInitialRotationRange(0, 360)
		.addModifier(new AlphaModifier(255, 0, 1000, 3000))
		.addModifier(new ScaleModifier(0.5f, 2f, 0, 1000))
		.oneShot(parentView, 4);
	}

	@Override
	public void onClick(View v) {
		if (null != flakingParticle) {
			return;
		}

		snowing();
	}

	private boolean isSnowing = false;
	private ParticleSystem flakingParticle;
	private void snowing() {
        Drawable flakingSnow = getResources().getDrawable(R.drawable.christ_snow_piece_bg);
        //int width = parentView.getWidth();
        int width = parentView.getWidth();
        int height = parentView.getHeight();
//            int height = CommonUtils.getScreenHeight(parentView.getContext());
//            int x = 0;
//            int y = - toolbarHeightOffset;
        int x = (int)(-0.2f * width);
        int snowWidth = width - 2*x;

        int[] parentLocation = new int[2];
        parentView.getLocationInWindow(parentLocation);
        int y = parentLocation[1];
//            int y = 0;
//            y -= 50;

        int maxParticles = 90;
        int particlesPerSecond = 10;
        long timeToLive = maxParticles*1000/particlesPerSecond;
        flakingParticle = new ParticleSystem(parentView, maxParticles, flakingSnow, timeToLive);
        int[] speedAngleArray = new int[] { 90, 100 };
//            flakingParticle.setAcceleration(0.00003f, 90)
        flakingParticle
                //.setSpeedByComponentsRange(-0.01f, 0.01f, 0.02f, 0.02f)
//                    .setSpeedModuleAndAngleRange(0f, 0.04f, speedAngleArray)
                .setSpeedModuleAndAngleRange(0.03f, 0.07f, 60, 120)
//                    .setSpeedModuleAndAngleRange(0.03f, 0.07f, 60, 120)
                //.setSpeedModuleAndAngleRange(0.00005f, 0.00009f, 0, 10)
//                    .setFadeOut(200, new AccelerateInterpolator())
//                    .addModifier(new AlphaModifier(255, 20, 0, timeToLive))
                .setAlphaRange(50, 255)
                .addModifier(new ScaleModifier(1f, 1.2f, 0, timeToLive))
                .addModifier(extraSpeedModifier)
                .addInitializer(new ScaleInitializer(0.8f, 1.5f))
                .emitWithGravity(new int[]{x, y}, snowWidth, 0, Gravity.BOTTOM, 10);
    }
}
