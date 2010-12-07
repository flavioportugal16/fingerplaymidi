package com.flat20.fingerplay.socket.commands.misc;

import com.flat20.fingerplay.socket.commands.SocketCommand;
import com.flat20.fingerplay.socket.commands.SocketStringCommand;

public class SetMidiDevice extends SocketStringCommand {

	private String mType = "";
	private String mDevice = "";

	public SetMidiDevice() {
		super(SocketCommand.COMMAND_SET_MIDI_DEVICE);
	}

	public SetMidiDevice(int type, String device) {
		super(SocketCommand.COMMAND_SET_MIDI_DEVICE, type + "%" + device);

		if (type == DeviceList.TYPE_OUT)
			mType = DeviceList.TYPE_OUT_STRING;
		else
			mType = DeviceList.TYPE_IN_STRING;
		mDevice = device;
		
		System.out.println("SetMidiDevice ctor " + type + ", " + device + " this = " + this);
	}

	public void setMessage(String message) {
		super.setMessage(message);
		int firstBreak = message.indexOf("%");
		mType = message.substring(0, firstBreak);
		mDevice = message.substring(firstBreak+1);

		System.out.println("SetMidiDevice setMessage " + message + ", this = " + this);
	}


	public int getType() {
		if (DeviceList.TYPE_OUT_STRING.equals(mType))
			return DeviceList.TYPE_OUT;
		else if (DeviceList.TYPE_IN_STRING.equals(mType))
			return DeviceList.TYPE_IN;
		else
			return -1;
	}
	
	public String getDevice() {
		return mDevice;
	}

	public String toString() {
		return "SetMidiDevice mType: " + mType + ", mDevice: \"" + mDevice + "\"";
	}
}
