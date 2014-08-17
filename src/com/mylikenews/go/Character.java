package com.mylikenews.go;

import android.content.Context;
import android.widget.ImageView;

public class Character extends ImageView {

	private boolean horizontal;
	private int size, row, column, height, width, id;

	public Character(Context context, int resId) {
		super(context);

		setImageResource(resId);
		setScaleType(ScaleType.CENTER_CROP);
		horizontal = false;
		size = 1;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setHorizontal() {
		horizontal = true;
	}

	public void setVertical() {
		horizontal = false;
	}

	public int row() {
		return row;
	}
  
	public int column() {
		return column;
	}

	public boolean horizontal() {
		return horizontal;
	}

	public int size() {
		return size;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}
	
	public int id() {
		return id;
	}

}

