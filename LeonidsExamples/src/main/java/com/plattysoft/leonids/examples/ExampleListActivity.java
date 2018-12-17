package com.plattysoft.leonids.examples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExampleListActivity extends ListActivity {
	private final String[] sampleList = new String [] {
			"One Shot Simple"
			,"Snowing"
			,"One Shot Advanced"
			, "Emiter Simple"
			, "Emiting on background [NEW]"
			, "Emiter Intermediate"
			, "Emiter Time Limited"
			, "Emit with Gravity [NEW]"
			, "Follow touch [NEW]"
			, "Animated particles"
			, "Fireworks"
			, "Confetti [Rabbit and Eggs]"
			, "Dust [Rabbit and Eggs]"
			, "Stars [Rabbit and Eggs]"
//				, "Animated Particles"
	};
	private final Class[] activityList = new Class[] {
			OneShotSimpleExampleActivity.class,
			SnowExampleActivity.class,
			OneShotAdvancedExampleActivity.class,
			EmiterSimpleExampleActivity.class,
			EmiterBackgroundSimpleExampleActivity.class,
			EmiterIntermediateExampleActivity.class,
			EmiterTimeLimitedExampleActivity.class,
			EmiterWithGravityExampleActivity.class,
			FollowCursorExampleActivity.class,
			AnimatedParticlesExampleActivity.class,
			FireworksExampleActivity.class,
			ConfettiExampleActivity.class,
			DustExampleActivity.class,
			StarsExampleActivity.class
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		assert (sampleList.length == activityList.length);

		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sampleList));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (position < activityList.length && position >= 0) {
			Intent intent = new Intent(this, activityList[position]);
			intent.putExtra("title", sampleList[position]);
			startActivity(intent);
		}
	}

}
