package com.flat20.fingerplay.socket.commands.misc;

import com.flat20.fingerplay.socket.commands.SocketCommand;
import com.flat20.fingerplay.socket.commands.SocketStringCommand;

public class SetMidiDevice extends SocketStringCommand {

	private String mType = "";
	private String mDevice = "";

	public SetMidiDevice() {
		super(SocketCommand.COMMAND_SET_MIDI_DEVICE);
	}
/*
	public SetMidiDevice(String encoded) {
		super(SocketCommand.COMMAND_SET_MIDI_DEVICE, encoded);

		int firstBreak = this.message.indexOf("%");
		mType = this.message.substring(0, firstBreak);
		mDevice = this.message.substring(firstBreak);
		System.out.println(mType + ", " + mDevice);

	}
*/
	public SetMidiDevice(int type, String device) {
		super(SocketCommand.COMMAND_SET_MIDI_DEVICE, type + "%" + device);

		if (type == DeviceList.TYPE_OUT)
			mType = DeviceList.TYPE_OUT_STRING;
		else
			mType = DeviceList.TYPE_IN_STRING;
		mDevice = device;
	}

	public void setMessage(String message) {
		int firstBreak = this.message.indexOf("%");
		mType = this.message.substring(0, firstBreak);
		mDevice = this.message.substring(firstBreak);
		System.out.println(mType + ", " + mDevice);
	}


	public int getType() {
		if ("out".equals(message))
			return DeviceList.TYPE_OUT;
		else if ("in".equals(message))
			return DeviceList.TYPE_IN;
		else
			return DeviceList.TYPE_IN;
	}
	
	public String getDevice() {
		return mDevice;
	}

}
