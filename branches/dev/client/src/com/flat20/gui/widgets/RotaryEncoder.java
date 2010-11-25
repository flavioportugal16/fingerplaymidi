package com.flat20.gui.widgets;

import com.flat20.gui.Materials;
import com.flat20.gui.sprites.MaterialSprite;
import com.flat20.gui.textures.CircleMesh;

// This is an absolute rotary encoder.
public class RotaryEncoder extends DefaultMidiWidget {

	final protected static int CC_TOUCH = 0;
	final protected static int CC_VALUE = 1;

	final protected MaterialSprite mCircle;
	final protected MaterialSprite mCircleOff;
	final protected MaterialSprite mKnobOverlay;

	private float mAmount = 1.0f;
	private float mPressedAmount = 1.0f;

	private int mPressedY;
	
	private int lastValue = -1;

	public RotaryEncoder() {

		addSprite(mBackground);

		mCircle = new MaterialSprite( Materials.MC_ROTARY );
		mCircle.rotation = 180;
		addSprite(mCircle);

		mCircleOff = new MaterialSprite( Materials.MC_ROTARY_OFF );
		mCircleOff.rotation = 180;
		addSprite(mCircleOff);

		mKnobOverlay = new MaterialSprite( Materials.MC_ROTARY_OVERLAY );
		addSprite(mKnobOverlay);

		addSprite(mOutline);
		//addSprite(mOutlineSelected);
		addSprite(mTvScanlines);

	}

	private void setAmount(float amount) {

		mAmount = amount;
		mAmount = (mAmount > 1.0f) ? 1.0f : (mAmount < 0.0f) ? 0.0f : mAmount;

		// Only bother to redraw if amount*0x7f has changed
		int value = (int) Math.max(0, Math.min(mAmount * 0x7F, 0x7F));
		if (value != lastValue) {
			getMidiController().sendParameter(CC_VALUE, value);

			// First and last segments are hidden under the black knob overlay.
			final CircleMesh mesh = (CircleMesh)mCircle.getGrid();
			int visible = Math.round(mAmount * (mesh.getNumSegments()-3)) + 1;
			mesh.setVisibleSegments( visible );

			final CircleMesh mesh2 = (CircleMesh)mCircleOff.getGrid();
			mesh2.setVisibleSegments( visible );

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

		//getMidiController().sendParameter(CC_TOUCH, 0x7F);
		mCircle.visible = true;
		mCircleOff.visible = false;
	}

	@Override
	protected void release(float pressure) {

		//getMidiController().sendParameter(CC_TOUCH, 0x00);
		mCircle.visible = false;
		mCircleOff.visible = true;
	}

	@Override
	public void setSize(int w, int h) {

		mCircle.setSize(w, h);
		mCircle.x += w/2 + 1;
		mCircle.y += h/2;

		mCircleOff.setSize(w, h);
		mCircleOff.x = mCircle.x;
		mCircleOff.y = mCircle.y;

		mKnobOverlay.setSize(w, h);

		super.setSize(w, h);

	}

}
