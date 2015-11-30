package dev.mirror.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class HalfHeightLinearLayout extends LinearLayout
{
	public HalfHeightLinearLayout(Context context)
	{
		super(context);
	}

	public HalfHeightLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@SuppressLint("NewApi") 
	public HalfHeightLinearLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		int height = width / 2;
		int w_mode = View.MeasureSpec.getMode(heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, 
				View.MeasureSpec.makeMeasureSpec(height, w_mode));
		setMeasuredDimension(width, height);
	}
}