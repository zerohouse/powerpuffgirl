package com.mylikenews.go;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class SelectStage extends ActionBarActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectstage);
		
		ArrayList<Stage> stages = new ArrayList<Stage>();
		stages.add(new Stage("Stage 1  ", 0, "2,3,2,3 0,0,3,0 0,3,2,0 4,0,2,1 3,2,3,0 0,4,2,1 3,3,2,1 4,3,2,0"));
		stages.add(new Stage("Stage 2  ", 0, "2,4,2,3 1,1,3,1 0,3,3,1 2,3,3,0 3,0,3,0 4,1,2,0 3,4,2,0 5,3,3,1"));
		stages.add(new Stage("Stage 3  ", 0, "2,3,2,3 1,0,3,0 1,2,3,0 0,3,3,1 4,1,2,1 5,1,2,1 3,3,2,0 2,5,3,0"));
		stages.add(new Stage("Stage 4  ", 0, "2,3,2,3 1,0,3,0 0,1,2,0 1,3,3,1 2,2,3,0 3,3,3,0 5,0,2,1 2,5,2,0 4,5,2,0"));
		stages.add(new Stage("Stage 5  ", 1, "2,4,2,3 0,0,3,0 1,1,2,1 2,1,2,0 0,3,3,0 0,5,2,0 4,1,3,1 5,1,3,1 3,4,2,0"));
		stages.add(new Stage("Stage 6  ", 1, "2,4,2,3 0,0,2,0 1,1,3,0 2,2,3,0 4,0,2,1 0,3,3,0 0,4,2,1 3,4,2,0"));
		
		stages.add(new Stage("Stage 7  ", 1, "2,4,2,3 0,1,3,1 1,0,3,0 1,1,3,0 1,2,3,0 1,3,2,0 3,3,2,0 4,0,2,1 5,1,2,1, 4,5,2,0"));
		stages.add(new Stage("Stage 8  ", 2, "2,4,2,3 2,0,3,0 0,1,3,1 1,2,3,0 1,3,2,0 3,1,2,0 4,2,2,1 5,2,2,1 4,4,2,0 4,5,2,0"));
		stages.add(new Stage("Stage 9  ", 2, "2,3,2,3 1,0,2,0 1,1,3,0 2,2,3,0 0,3,2,1 1,5,3,0 3,3,2,0 3,4,2,0 4,0,2,1 5,1,3,1"));
		stages.add(new Stage("Stage 10  ", 3, "2,3,2,3 0,0,2,0 0,1,3,0 1,2,2,0 2,0,3,0 4,1,2,1 5,1,2,1 3,3,2,0 3,4,2,1 4,4,2,1 5,4,2,1 1,5,2,0 0,3,2,1"));
		
		
		
		StageListAdapter stageadapter = new StageListAdapter(SelectStage.this, R.layout.stage,
				stages);
		
		ListView stagelist = (ListView) findViewById(R.id.stages);
		stagelist.setAdapter(stageadapter);


	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_stage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

