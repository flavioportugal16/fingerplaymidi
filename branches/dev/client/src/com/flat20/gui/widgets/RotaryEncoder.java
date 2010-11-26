package com.flat20.gui.widgets;

public abstract class RotaryEncoder extends DefaultMidiWidget {

	final protected static int CC_TOUCH = 0;
	final protected static int CC_VALUE = 1;

	protected float mAmount = 1.0f;
	private float mPressedAmount = 1.0f;

	private int mPressedY;
	
	private int lastValue = -1;

	public RotaryEncoder() {

	}
	
	protected void redraw() {
		
	}

	private void setAmount(float amount) {

		mAmount = amount;
		mAmount = (mAmount > 1.0f) ? 1.0f : (mAmount < 0.0f) ? 0.0f : mAmount;

		// Only bother to redraw if amount*0x7f has changed
		int value = (int) Math.max(0, Math.min(mAmount * 0x7F, 0x7F));
		if (value != lastValue) {
			getMidiController().sendParameter(CC_VALUE, value);

			redraw();

			lastValue = value;
		}

	}

	@Override
	public boolean onTouchDown(int touchX, int touchY, float pressure, int pointerId) {
		press(1.0f);
		mPressedAmount = mAmount;

		mPressedY = touchY;
		return true;
	}

	@Override
	public boolean onTouchMove(int touchX, int touchY, float pressure, int pointerId) {

		float acc = (height/32*64); // How far do we need to drag
		float delta = (touchY - mPressedY) / acc; 
		setAmount( mPressedAmount + delta );
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
	}

	@Override
	protected void release(float pressure) {
		getMidiController().sendParameter(CC_TOUCH, 0x00);
	}

}
