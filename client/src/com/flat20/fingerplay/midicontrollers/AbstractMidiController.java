package com.flat20.fingerplay.midicontrollers;

public abstract class AbstractMidiController implements IMidiController {

	//private int mControllerNumber = CONTROLLER_NUMBER_UNASSIGNED;
	private String mName = null;
	private Parameter[] mParameters = null;

	private IOnControlChangeListener mListener = null;
/*
	@Override
	public int getControllerNumber() {
		return mControllerNumber;
	}
	
	@Override
	public void setControllerNumber(int number) {
		mControllerNumber = number;
	}
*/

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public void setName(String name) {
		mName = name;
	}

	@Override
	public Parameter[] getParameters() {
		return mParameters;
	}
	
	@Override
	public void setParameters(Parameter[] parameters) {
		mParameters = parameters;
	}
/*
	@Override
	public Parameter getParameterById(int parameterId) {
		return mParameters[parameterId];
		return null;
	}

	@Override
	public boolean isHolding() {
		// TODO Auto-generated method stub
		return false;
	}
*/
	/**
	 * Sends the parameter using the value (0x00-0x7FF)
	 * 
	 * @param parameterId
	 * @param value
	 */
	@Override
	public void sendParameter(int parameterId, int value) {
		if (mListener == null)
			return;
 
		final Parameter p = mParameters[parameterId];
		final int type = p.type;
		switch(type) {
			case Parameter.TYPE_CONTROL_CHANGE:
				mListener.onControlChange(this, p.channel, p.controllerNumber, value);
				break;
			case Parameter.TYPE_NOTE_ON:
				mListener.onNoteOn(this, p.channel, p.controllerNumber, value);
				break;
			case Parameter.TYPE_NOTE_OFF:
				mListener.onNoteOff(this, p.channel, p.controllerNumber, value);
				break;
		}
	}
/*
	@Override
	public void sendControlChange(int controllerNumber, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendNoteOff(int key, int velocity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendNoteOn(int key, int velocity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHold(boolean hold) {
		// TODO Auto-generated method stub

	}
*/

	@Override
    public void setOnControlChangeListener(IOnControlChangeListener l) {
    	mListener = l;
    }

}
