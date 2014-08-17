package com.mylikenews.go;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

class StageListAdapter extends BaseAdapter {
	Context maincon;
	LayoutInflater Inflater;
	ArrayList<Stage> stages;
	int layout;

	public StageListAdapter(Context context, int alayout, ArrayList<Stage> stage) {
		maincon = context;
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		stages = stage;
		layout = alayout;
	}

	public int getCount() {
		return stages.size();
	}

	public String getItem(int position) {
		return stages.get(position).name;
	}

	public long getItemId(int position) {
		return position;
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		TextView name = (TextView) convertView.findViewById(R.id.name);
		name.setText(stages.get(position).name);

		TextView difficulty = (TextView) convertView
				.findViewById(R.id.difficulty);

		if (stages.get(position).difficulty == 3) {
			difficulty.setBackgroundResource(R.drawable.diff3);
		} else if (stages.get(position).difficulty == 2) {
			difficulty.setBackgroundResource(R.drawable.diff2);
		} else if (stages.get(position).difficulty == 1) {
			difficulty.setBackgroundResource(R.drawable.diff1);
		} else {
			difficulty.setBackgroundResource(R.drawable.diff0);
		}

		LinearLayout btn = (LinearLayout) convertView
				.findViewById(R.id.eachstage);

		btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent game = new Intent(maincon, Game.class);
				game.putExtra("stage", stages.get(pos).stagestring);
				game.putExtra("stagename", (pos + 1) + "");
				maincon.startActivity(game);
			}
		});

		return convertView;
	}

}