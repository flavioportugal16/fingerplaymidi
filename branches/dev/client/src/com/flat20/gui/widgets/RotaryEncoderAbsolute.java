package com.flat20.gui.widgets;

import com.flat20.gui.Materials;
import com.flat20.gui.sprites.MaterialSprite;
import com.flat20.gui.textures.CircleMesh;

public class RotaryEncoderAbsolute extends RotaryEncoder {

	final protected MaterialSprite mCircle;
	final protected MaterialSprite mCircleOff;
	final protected MaterialSprite mKnobOverlay;

	public RotaryEncoderAbsolute() {

		addSprite(mBackground);

		mCircle = new MaterialSprite( Materials.MC_ROTARY );
		mCircle.rotation = 180;
		addSprite(mCircle);

		mCircleOff = new MaterialSprite( Materials.MC_ROTARY_OFF );
		mCircleOff.rotation = 180;
		addSprite(mCircleOff);

		mKnobOverlay = new MaterialSprite( Materials.MC_ROTARY_OVERLAY );
		addSprite(mKnobOverlay);

		//addSprite(mOutline);
		//addSprite(mOutlineSelected);
		//addSprite(mTvScanlines);

	}

	@Override
	protected void redraw() {

		// First and last segments are hidden under the black knob overlay.
		final CircleMesh mesh = (CircleMesh)mCircle.getGrid();
		int visible = Math.round(mValue * (mesh.getNumSegments()-3)) + 1;
		mesh.setVisibleSegments( visible );

		final CircleMesh mesh2 = (CircleMesh)mCircleOff.getGrid();
		mesh2.setVisibleSegments( visible );

	}

	@Override
	protected void press(float pressure) {

		super.press(pressure);

		mCircle.visible = true;
		mCircleOff.visible = false;
	}

	@Override
	protected void release(float pressure) {

		super.release(pressure);

		mCircle.visible = false;
		mCircleOff.visible = true;
	}

	@Override
	public void setSize(int w, int h) {

		super.setSize(w, h);

		mCircle.setSize(w, h);
		mCircle.x += w/2+1;
		mCircle.y += h/2-1;

		mCircleOff.setSize(w, h);
		mCircleOff.x = mCircle.x;
		mCircleOff.y = mCircle.y;

		mKnobOverlay.setSize(w, h);

		redraw();
	}

}
