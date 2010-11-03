package com.flat20.fingerplay.socket.commands.misc;

import com.flat20.fingerplay.socket.commands.SocketCommand;
import com.flat20.fingerplay.socket.commands.SocketStringCommand;

public class DeviceList extends SocketStringCommand {

	public final static int TYPE_IN = 1; // In from DAW and OUT to FP client
	public final static int TYPE_OUT = 2; // Out from FP client.
	public final static String TYPE_IN_STRING = "in";
	public final static String TYPE_OUT_STRING = "out";

	private String mType = "";
	private String mDeviceList = "";

	public DeviceList() {
		super(SocketCommand.COMMAND_MIDI_DEVICE_LIST);
	}

	public DeviceList(int type, String deviceList) {
		super(SocketCommand.COMMAND_MIDI_DEVICE_LIST, (type==TYPE_IN) ? TYPE_IN_STRING  + "%" + deviceList : TYPE_OUT_STRING + "%" + deviceList);
	}

	public void setMessage(String message) {
		super.setMessage(message);
		int firstBreak = message.indexOf("%");
		mType = message.substring(0, firstBreak);
		mDeviceList = message.substring(firstBreak+1);
	}

	public int getType() {
		if (TYPE_IN_STRING.equals(mType))
			return DeviceList.TYPE_OUT;
		else if (TYPE_OUT_STRING.equals(mType))
			return DeviceList.TYPE_IN;
		else
			return -1;
	}

	public String getDeviceList() {
		return mDeviceList;
	}

}
