package com.mylikenews.go;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity {

	ArrayList<Character> board;
	boolean[][] occupy;
	Stack<int[]> history;

	RelativeLayout relativelayout;
	private Character selected_item = null;
	private int offset_x = 0;
	private int offset_y = 0;
	int marginx = 0;
	int marginy = 0;
	static int moveint = 0;
	int lastrow = 0;
	int lastcolumn = 0;
	String init = "";
	TextView move;
	TextView time;
	Intent getstage;
	boolean select = false;
	int blockx;
	int blocky;
	static int difficulty;
	Context context;
	boolean stop = false;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (stop == false) {
			onChanged();
		}
	}

	@Override
	public void onBackPressed() {
		moveint = 0;
		mainTime = 0;
		finish();
	}

	private void onChanged() {
		time = (TextView) findViewById(R.id.time);
		move = (TextView) findViewById(R.id.move);
		move.setText(moveint + "");
		relativelayout.setBackgroundResource(R.drawable.tile);
		blockx = relativelayout.getWidth() / 6;
		blocky = relativelayout.getHeight() / 6;
		String stagestring = getstage.getExtras().getString("stage");
		difficulty = Integer.parseInt(getstage.getExtras().getString(
				"difficulty"));
		setTitle("파워퍼프걸 - " + getstage.getExtras().getString("stagename")
				+ "단계");
		putElements(stagestring);
		mHandler.sendEmptyMessage(1);
	}

	private void putElements(String stagestring) {
		String[] stage = stagestring.split(" ");
		String[] elementattr;
		for (String element : stage) {
			elementattr = element.split(",");
			addCharacter(Integer.parseInt(elementattr[0]),
					Integer.parseInt(elementattr[1]),
					Integer.parseInt(elementattr[2]),
					Integer.parseInt(elementattr[3]));
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stop = true;
	}

	public void onResume() {
		super.onResume();
		stop = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {

		board = new ArrayList<Character>();
		history = new Stack<int[]>();
		occupy = new boolean[6][6];
		getstage = getIntent();

		Button back = (Button) findViewById(R.id.back);
		Button refresh = (Button) findViewById(R.id.refresh);
		back.setOnClickListener(lisetner);
		refresh.setOnClickListener(lisetner);

		relativelayout = (RelativeLayout) findViewById(R.id.gameplace);
		relativelayout.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				return dragElements(event);
			}
		});
	}

	OnClickListener lisetner = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.refresh:
				refreshGame();
				break;
			case R.id.back:
				unDo();
				break;
			}
		}
	};

	private boolean dragElements(MotionEvent event) {
		if (selected_item != null) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				lastrow = selected_item.row();
				lastcolumn = selected_item.column();

				withdraw(selected_item.row(), selected_item.column(),
						selected_item.size(), selected_item.horizontal());

				marginx = (int) event.getX() - offset_x;
				marginy = (int) event.getY() - offset_y;

				break;
			case MotionEvent.ACTION_MOVE:

				marginx = (int) event.getX() - offset_x;
				marginy = (int) event.getY() - offset_y;

				RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(
						new ViewGroup.MarginLayoutParams(
								RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT));

				layoutparams.width = selected_item.width();
				layoutparams.height = selected_item.height();
				layoutparams.setMargins(marginx, marginy, 0, 0);

				selected_item.setLayoutParams(layoutparams);

				break;

			case MotionEvent.ACTION_UP:

				if (selected_item.horizontal() == true) {
					if (movAble(selected_item.column(), checkColumn()) == true)
						selected_item.setColumn(checkColumn());
				} else {
					if (movAble(selected_item.row(), checkRow()) == true)
						selected_item.setRow(checkRow());
				}

				occupys(selected_item.row(), selected_item.column(),
						selected_item.size(), selected_item.horizontal());

				if (selected_item.row() != lastrow
						|| selected_item.column() != lastcolumn) {
					moveint++;
					move.setText(moveint + "");
					int[] position = new int[3];
					position[0] = selected_item.id();
					position[1] = lastrow;
					position[2] = lastcolumn;
					history.push(position);
				}

				draw();

				if (selected_item.id() == 0 && selected_item.column() == 0) {
					Done();
				}

				selected_item = null;

				break;

			default:
				break;

			}
		}
		return true;
	}

	private void unDo() {
		if (history.isEmpty() == true) {
			makeToast("움직인 캐릭터가 없어요!");
			return;
		}

		moveint--;
		move.setText(moveint + "");

		int[] position = history.pop();

		selected_item = board.get(position[0]);

		withdraw(selected_item.row(), selected_item.column(),
				selected_item.size(), selected_item.horizontal());

		// time.setText(position[0] + "," + position[1] + "," + position[2]);

		selected_item.setRow(position[1]);
		selected_item.setColumn(position[2]);

		occupys(selected_item.row(), selected_item.column(),
				selected_item.size(), selected_item.horizontal());
		draw();
		selected_item = null;
	}

	private void refreshGame() {
		makeToast("다시하기!");
		mainTime = 0;
		history.clear();
		moveint = 0;
		move.setText(moveint + "");

		String[] initrowcolumn = init.split("@");
		occupy = null;
		occupy = new boolean[6][6];

		String[] rowcol;

		for (int i = 0; i < initrowcolumn.length - 1; i++) {
			selected_item = board.get(i);
			rowcol = initrowcolumn[i].split("#");

			selected_item.setRow(Integer.parseInt(rowcol[0]));
			selected_item.setColumn(Integer.parseInt(rowcol[1]));

			occupys(selected_item.row(), selected_item.column(),
					selected_item.size(), selected_item.horizontal());
			draw();
		}
		selected_item = null;
	}

	public void draw() {
		if (selected_item == null)
			return;
		RelativeLayout.LayoutParams layoutparam = new RelativeLayout.LayoutParams(
				new ViewGroup.MarginLayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT));
		layoutparam.width = selected_item.width();
		layoutparam.height = selected_item.height();

		marginx = selected_item.column() * blockx;
		marginy = selected_item.row() * blocky;

		layoutparam.setMargins(marginx, marginy, 0, 0);
		selected_item.setLayoutParams(layoutparam);
	}

	private boolean movAble(int from, int to) {
		int start, end;
		if (from < to) {
			start = from;
			end = to + 1;
		} else {
			start = to;
			end = from + 1;
		}

		for (int i = start; i < end; i++) {
			if (selected_item.horizontal() == true) {
				if (checkOccupied(selected_item.row(), i, selected_item.size(),
						selected_item.horizontal()) == true)
					return false;
			} else {
				if (checkOccupied(i, selected_item.column(),
						selected_item.size(), selected_item.horizontal()) == true)
					return false;
			}
		}
		return true;
	}

	private int checkRow() {
		int value = selected_item.row();
		if (marginy < blocky * 0.5) {
			value = 0;
		} else if (marginy < blocky * 1.5) {
			value = 1;
		} else if (marginy < blocky * 2.5) {
			value = 2;
		} else if (marginy < blocky * 3.5) {
			value = 3;
		} else if (marginy < blocky * 4.5) {
			value = 4;
		} else if (marginy < blocky * 5.5) {
			value = 5;
		}
		return value;
	}

	private int checkColumn() {
		int value = selected_item.row();
		if (marginx < blockx * 0.5) {
			value = 0;
		} else if (marginx < blockx * 1.5) {
			value = 1;
		} else if (marginx < blockx * 2.5) {
			value = 2;
		} else if (marginx < blockx * 3.5) {
			value = 3;
		} else if (marginx < blockx * 4.5) {
			value = 4;
		} else if (marginx < blockx * 5.5) {
			value = 5;
		}
		return value;
	}

	private void addCharacter(int row, int column, int size, int horizontalint) {

		Random random = new Random();
		int rannum = random.nextInt(2);

		int resId = R.drawable.ic_launcher;
		boolean horizontal = false;
		if (horizontalint == 1 || horizontalint == 3)
			horizontal = true;

		if (horizontalint == 3) {
			if (rannum == 0)
				resId = R.drawable.puff1;
			else if (rannum == 1)
				resId = R.drawable.puff2;
			else if (rannum == 2)
				resId = R.drawable.puff3;

		} else if (horizontalint == 0 && size == 2) {
			if (rannum == 0)
				resId = R.drawable.char2hei1;
			else if (rannum == 1)
				resId = R.drawable.char2hei2;
			else if (rannum == 2)
				resId = R.drawable.char2hei3;

		} else if (horizontalint == 0 && size == 3) {
			if (rannum == 0)
				resId = R.drawable.char3hei1;
			else if (rannum == 1)
				resId = R.drawable.char3hei2;
			else if (rannum == 2)
				resId = R.drawable.char3hei3;

		} else if (horizontalint == 1 && size == 2) {
			if (rannum == 0)
				resId = R.drawable.char2wid1;
			else if (rannum == 1)
				resId = R.drawable.char2wid2;
			else if (rannum == 2)
				resId = R.drawable.char2wid3;

		} else if (horizontalint == 1 && size == 3) {
			if (rannum == 0)
				resId = R.drawable.char3wid1;
			else if (rannum == 1)
				resId = R.drawable.char3wid2;
			else if (rannum == 2)
				resId = R.drawable.char3wid3;

		}

		if (checkOccupied(row, column, size, horizontal) == true) {
			return;
		}

		Character character = new Character(this, resId);
		relativelayout.addView(character);

		dragAble(character);
		character.setRow(row);
		character.setColumn(column);

		init += row + "#" + column + "@";

		character.setSize(size);

		character.setBackgroundColor(100);

		if (horizontal == true) {
			character.setHorizontal();
			character.setWidth(blockx * size);
			character.setHeight(blocky);
		} else {
			character.setWidth(blockx);
			character.setHeight(blocky * size);
		}

		marginx = character.column() * blockx;
		marginy = character.row() * blocky;
		RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(
				new ViewGroup.MarginLayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT));
		layoutparams.width = character.width();
		layoutparams.height = character.height();
		layoutparams.setMargins(marginx, marginy, 0, 0);

		character.setLayoutParams(layoutparams);
		character.setId(board.size());
		board.add(character);
		dragAble(character);
		occupys(character.row(), character.column(), character.size(),
				character.horizontal());
		character.requestLayout();

	}

	private void withdraw(int row, int column, int size, boolean horizontal) {
		for (int i = 0; i < size; i++) {
			if (horizontal == true) {
				occupy[row][column + i] = false;
			} else {
				occupy[row + i][column] = false;
			}
		}
	}

	private void occupys(int row, int column, int size, boolean horizontal) {
		for (int i = 0; i < size; i++) {
			if (horizontal == true) {
				occupy[row][column + i] = true;
			} else {
				occupy[row + i][column] = true;
			}
		}
	}

	private boolean checkOccupied(int row, int column, int size,
			boolean horizontal) {
		if (horizontal == true) {
			if (column + size > 6)
				return true;
		} else {
			if (row + size > 6)
				return true;
		}

		for (int i = 0; i < size; i++) {
			if (horizontal == true) {
				if (occupy[row][column + i] == true)
					return true;
			} else {
				if (occupy[row + i][column] == true)
					return true;
			}
		}
		return false;
	}

	@SuppressLint("ClickableViewAccessibility")
	private void dragAble(Character Character) {
		Character.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					selected_item = (Character) v;
					offset_x = (int) event.getX();
					offset_y = (int) event.getY();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	// 게임 끝날때 다이얼로그
	MainDialog mMainDialog;
	Toast mToast;

	@SuppressLint("NewApi")
	void Done() {
		stop = true;
		mMainDialog = new MainDialog();
		mMainDialog.show(getFragmentManager(), "MYTAG");
	}

	@SuppressLint("NewApi")
	public static class MainDialog extends DialogFragment {

		@SuppressLint("InflateParams")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder mBuilder = new AlertDialog.Builder(
					getActivity());
			LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
			mBuilder.setView(mLayoutInflater
					.inflate(R.layout.dialog_main, null));
			mBuilder.setTitle("Stage Clear!!");

			int score = (10000 * ((difficulty + 1) * 10))
					/ (mainTime + 3 * moveint);
			mBuilder.setMessage(String.format(
					"Move : %d   Time : %s \n  Score : %d", moveint, strTime,
					score));
			return mBuilder.create();
		}

	}

	@SuppressLint("NewApi")
	public void ONCLICK_DIALOG(View v) {
		switch (v.getId()) {
		case R.id.refresh:
			refreshGame();
			stop = false;
			mMainDialog.dismiss();
			break;
		case R.id.other:
			mainTime = 0;
			finish();
			break;
		}

	}

	public void makeToast(String msg) {
		mToast = Toast.makeText(getApplicationContext(), msg,
				Toast.LENGTH_SHORT);
		mToast.show();
	}

	// 타이머

	static int mainTime = 0;
	static String strTime;

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@SuppressLint("DefaultLocale")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			/** 초시간을 잰다 */
			// int div = msg.what;
			int min = mainTime / 60;
			int sec = mainTime % 60;
			if (mainTime > 60) {
				strTime = String.format("%02d : %02d", min, sec);
			} else {
				strTime = mainTime + "";
			}

			if (stop == false) {
				this.sendEmptyMessageDelayed(0, 1000);
				time.setText(strTime);
				time.invalidate();
				mainTime++;
			}

		}
	};

}
