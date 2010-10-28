package com.flat20.fingerplay.socket.commands;

public class SocketCommand {

	// Standard MIDI 4 byte message.
	public final static byte COMMAND_MIDI_SHORT_MESSAGE = 0x01;

	// Client tells the server which channel he'll be using.
	public final static byte COMMAND_SET_CONTROL_CHANGE_CHANNEL = 0x02;

	// Client asks for a list of MIDI devices.
	public final static byte COMMAND_REQUEST_MIDI_DEVICE_LIST = 0x03; 

	// Server sends the MIDI device list to the client.
	public final static byte COMMAND_MIDI_DEVICE_LIST = 0x04; 

	// Client sets the MIDI device used by the server.
	public final static byte COMMAND_SET_MIDI_DEVICE = 0x05; 

	// Client sets the MIDI device used by the server.
	public final static byte COMMAND_SET_MIDI_DEVICE_IN = 0x06; 

	public final static byte COMMAND_VERSION = 0x07; 

	public byte command;

	public SocketCommand() {
		
	}

	public SocketCommand(byte command) {
		this.command = command;
	}
}
