package com.flat20.fingerplay.midicontrollers;

import com.flat20.fingerplay.config.IConfigurable;

public interface IMidiController extends IConfigurable {

	final public static int CONTROLLER_NUMBER_UNASSIGNED = -1;

	public void setName(String name);
	public String getName();

	// Names (and indices) of all parameters belonging to this controller
	public Parameter[] getParameters();

	public void sendParameter(int parameterId, int value);
	public void setOnControlChangeListener(IOnControlChangeListener l);
}
