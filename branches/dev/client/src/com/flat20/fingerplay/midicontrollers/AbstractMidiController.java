package com.flat20.fingerplay.midicontrollers;

import java.util.HashMap;

import com.flat20.fingerplay.config.ConfigItemParameters;
import com.flat20.fingerplay.config.IConfigurable;
import com.flat20.gui.widgets.MidiWidget;

public abstract class AbstractMidiController implements IMidiController, IConfigurable {

	//private int mControllerNumber = CONTROLLER_NUMBER_UNASSIGNED;
	private String mName = null;
	private Parameter[] mParameters = null;

	private IOnControlChangeListener mListener = null;
	
	// The MidiWidget this controller belongs to.
	private MidiWidget mView = null;
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
/*
	@Override
	public void setParameters(Parameter[] parameters) {
		mParameters = parameters;
	}*/
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

	@Override
    public void setOnControlChangeListener(IOnControlChangeListener l) {
    	mListener = l;
    }

	@Override
	public void setView(MidiWidget widget) {
		mView = widget;
	}

	@Override
	public MidiWidget getView() {
		return mView;
	}


	// IConfigurable

	@Override
	public void setParameters( ConfigItemParameters parameters) {

		mParameters = new Parameter[parameters.data.size()];

		for (HashMap<String, Object> map : parameters.data) {
			System.out.println("AbstractMidiController::setParameters - map = " + map);

			int id = Integer.parseInt((String)map.get("id"));
			int channel = Integer.parseInt((String)map.get("channel"));
			int controllerNumber = Integer.parseInt( (String)map.get("controllerNumber") );
			String name = (String)map.get("name");
			boolean visible = Boolean.parseBoolean( (String)map.get("visible") );
			String stringType = (String)map.get("type");
			int type = Parameter.TYPE_CONTROL_CHANGE;

			if ("controlChange".equals(stringType)) {
				type = Parameter.TYPE_CONTROL_CHANGE;
			} else if ("note".equals(stringType)) {
				type = Parameter.TYPE_NOTE;
			}

			Parameter param = new Parameter(id, channel, controllerNumber, name, type, visible);
			mParameters[id] = param;
		}

	}

	public String toString() {
		String result = this.mName + " Parameters:\n";
		if (mParameters != null) {
			for (int i=0; i<mParameters.length; i++) {
				result += mParameters[i] + "\n";
			}
		}
		return result;
	}
}
