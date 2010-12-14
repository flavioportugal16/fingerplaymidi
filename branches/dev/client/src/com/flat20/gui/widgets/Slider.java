package com.flat20.gui.widgets;

import com.flat20.fingerplay.midicontrollers.Parameter;
import com.flat20.gui.Materials;
import com.flat20.gui.sprites.MaterialSprite;

public class Slider extends DefaultMidiWidget {

	final protected MaterialSprite mMeter;
	final protected MaterialSprite mMeterOff;

	final protected static int CC_TOUCH = 0;
	final protected static int CC_VALUE = 1;

	// 0.0 -> 1.0
	private float mValue;
	int lastValue = -1;

	public Slider() {
		super();

		mMeter = new MaterialSprite(Materials.MC_INDICATOR);
		mMeterOff = new MaterialSprite(Materials.MC_INDICATOR_OFF);

		addSprite(mBackground);
		addSprite(mMeterOff);
		addSprite(mMeter);
		addSprite(mOutline);
		addSprite(mOutlineSelected);
		addSprite(mTvScanlines);
		
		//mBackground.x = 1;
		//mBackground.y = 1;

		mMeter.visible = false;

        mOutlineSelected.x = -3;
        mOutlineSelected.y = -3;

		mTvScanlines.x = 2;
		mTvScanlines.y = 2;

		setSize(32, 32);
	}

	public void setSize(int w, int h) {
		super.setSize(w, h);

		mMeter.setSize(w, h);
		mMeterOff.setSize(w, h);

		redraw();
	}

	// Redraw this view based on the Parameter values
	// in a future version we'll share Parameters between the
	// controller and this view.
	public void redraw() {
		setMeterHeight( Math.max(0, Math.min((int)(mValue*height), height)) );
	}

	@Override
	public boolean onTouchDown(int touchX, int touchY, float pressure, int pointerId) {
		press(1.0f);
		return true;
	}

	@Override
	public boolean onTouchMove(int touchX, int touchY, float pressure, int pointerId) {
		mValue = ((float)touchY / (float)height);
		int value = (int) Math.max(0, Math.min(mValue * 0x7F, 0x7F));
		if (value != lastValue) {
			getMidiController().sendParameter(CC_VALUE, value);
			lastValue = value;
			redraw();
		}
		return true;
	}

	@Override
	public boolean onTouchUp(int touchX, int touchY, float pressure, int pointerId) {
		release(1.0f);
		return true;
	}

	@Override
	public boolean onTouchUpOutside(int touchX, int touchY, float pressure, int pointerId) {
		release(1.0f);
		return true;
	}

	@Override
	protected void press(float pressure) {

		getMidiController().sendParameter(CC_TOUCH, 0x7F);

		mMeter.visible = true;
		mMeterOff.visible = false;
	}

	@Override
	protected void release(float pressure) {

		getMidiController().sendParameter(CC_TOUCH, 0x00);

		mMeter.visible = false;
		mMeterOff.visible = true;
	}

	// Message comes from the controller.
	// Maybe Parameter contains a float value and 
	// this function could be moved up when we start sharing parameters.
	public void onParameterUpdated(Parameter parameter, int value) {
		switch (parameter.id) {
			case CC_VALUE:
				mValue = (float)value/0x7F;
				redraw();
				break;
		}
	}

 
	protected void setMeterHeight(int meterHeight) {
		mMeter.setSize(mMeter.width, meterHeight);
		mMeterOff.setSize(mMeterOff.width, meterHeight);
		/*
		meter.getGrid().updateVertice(2*3+1, height); //*3+1 means y coordinate.
		meter.getGrid().updateVertice(3*3+1, height);
		meterOff.getGrid().updateVertice(2*3+1, height); //*3+1 means y coordinate.
		meterOff.getGrid().updateVertice(3*3+1, height);
		*/
	}

}
