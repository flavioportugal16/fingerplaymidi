package com.flat20.fingerplay.socket.commands.misc;

import com.flat20.fingerplay.socket.commands.SocketCommand;
import com.flat20.fingerplay.socket.commands.SocketStringCommand;

public class RequestMidiDeviceList extends SocketStringCommand {
	
	public RequestMidiDeviceList() {
		super(SocketCommand.COMMAND_REQUEST_MIDI_DEVICE_LIST);
	}

	public RequestMidiDeviceList(String deviceType) {
		super(SocketCommand.COMMAND_REQUEST_MIDI_DEVICE_LIST, deviceType);
	}

	public int getType() {
		if ("out".equals(message))
			return DeviceList.TYPE_OUT;
		else if ("in".equals(message))
			return DeviceList.TYPE_IN;
		else
			return DeviceList.TYPE_IN;
	}
}
