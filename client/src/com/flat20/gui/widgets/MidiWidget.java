package com.flat20.gui.widgets;

import com.flat20.fingerplay.config.IConfigItemView;
import com.flat20.fingerplay.config.IConfigurable;
import com.flat20.fingerplay.midicontrollers.IMidiController;
import com.flat20.fingerplay.midicontrollers.Parameter;

/**
 * IMidiController and AbstractMidiController deals with sending
 * the data now. Plan is to make MidiWidgets only deal mostly with
 * UI stuff.
 * 
 * @author andreas.reuterberg
 *
 */
public abstract class MidiWidget extends Widget implements IConfigItemView { //implements IMidiController {

	private IMidiController mMidiController;
	
	protected boolean mHold = false;

	public MidiWidget() {
		super();

		//mMidiController = midiController;
		//mMidiController.setView(this);

		//setName(name);
		//setControllerNumber(controllerNumber);
	}

	@Override
	public void setController(IConfigurable controller) throws Exception {
		if (controller instanceof IMidiController) {
			mMidiController = (IMidiController) controller;
			mMidiController.setView(this);
		} else
			throw new Exception("Illegal controller assigned to MidiWidget; Must be of class IMidiController.");
	}

	public void onParameterUpdated(Parameter parameter, int value) {
		System.out.println("MidiWidget.onParameterUpdated");
	}

	public IMidiController getMidiController() {
		return mMidiController;
	}


	// Subclasses decide what to do with these.
	protected void press(float pressure) {
	}

	protected void release(float pressure) {
		
	}

	// Redraw widget. Sort of belongs to Widget because it
	// it needs to be done after a setSize for example.
	protected void redraw() {
		
	}
}
