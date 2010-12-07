package com.flat20.fingerplay.socket.commands.misc;

import com.flat20.fingerplay.socket.commands.SocketCommand;
import com.flat20.fingerplay.socket.commands.SocketStringCommand;

public class RequestMidiDeviceList extends SocketStringCommand {

	public RequestMidiDeviceList() {
		super(SocketCommand.COMMAND_REQUEST_MIDI_DEVICE_LIST);
	}

	public RequestMidiDeviceList(int deviceType) {
		super(SocketCommand.COMMAND_REQUEST_MIDI_DEVICE_LIST, (deviceType==DeviceList.TYPE_OUT) ? DeviceList.TYPE_OUT_STRING : DeviceList.TYPE_IN_STRING);
		System.out.println("RequestMidiDeviceList ctor type = " + deviceType);
	}

	public int getType() {
		if (DeviceList.TYPE_OUT_STRING.equals(message))
			return DeviceList.TYPE_OUT;
		else if (DeviceList.TYPE_IN_STRING.equals(message))
			return DeviceList.TYPE_IN;
		else
			return -1;
	}
}
