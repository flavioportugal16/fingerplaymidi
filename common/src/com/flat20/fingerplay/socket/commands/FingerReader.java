package com.flat20.fingerplay.socket.commands;

import java.io.DataInputStream;

import java.io.IOException;
import java.net.SocketException;

import com.flat20.fingerplay.socket.commands.midi.MidiSocketCommand;
import com.flat20.fingerplay.socket.commands.misc.DeviceList;
import com.flat20.fingerplay.socket.commands.misc.RequestMidiDeviceList;
import com.flat20.fingerplay.socket.commands.misc.SetMidiDevice;
import com.flat20.fingerplay.socket.commands.misc.Version;

public class FingerReader {

	final private DataInputStream mIn;
	final private IReceiver mReceiver;

	final private static byte[] sData = new byte[ 0xFFFF ];

	final private MidiSocketCommand sMss = new MidiSocketCommand();

	//final private DeviceList sDl = new DeviceList();
	//final private RequestMidiDeviceList sRmdl = new RequestMidiDeviceList();
	//final private SetMidiDevice sMd = new SetMidiDevice();
	final private Version sV = new Version();

	public FingerReader(DataInputStream in, IReceiver receiver) {
		mIn = in;
		mReceiver = receiver;
	}

	public byte readCommand() throws Exception {

		final SocketCommand sm;
		byte command = mIn.readByte();

		switch (command) {
			case SocketCommand.COMMAND_MIDI_SHORT_MESSAGE:
				decode(sMss, command);
				mReceiver.onMidiSocketCommand( sMss );
				return command;
			
			case SocketCommand.COMMAND_REQUEST_MIDI_DEVICE_LIST:
				sm = new RequestMidiDeviceList();
				decode((RequestMidiDeviceList)sm, command);
				mReceiver.onRequestMidiDeviceList( (RequestMidiDeviceList) sm );
				return command;

			case SocketCommand.COMMAND_MIDI_DEVICE_LIST:
				sm = new DeviceList();
				decode((DeviceList)sm, command);
				mReceiver.onDeviceList( (DeviceList) sm );
				return command;

			case SocketCommand.COMMAND_SET_MIDI_DEVICE:
				SetMidiDevice temp = new SetMidiDevice();
				System.out.println("FingerReader 1 temp = " + temp);
				decode(temp, command);
				System.out.println("FingerReader 2 temp = " + temp);
				mReceiver.onSetMidiDevice( temp );
				return command;

			case SocketCommand.COMMAND_VERSION:
				decode(sV, command);
				mReceiver.onVersion( sV );
				return command;
			default:
				System.out.println("Unknown command: " + command + String.valueOf(command));
		}
		return -1;
	}
/*
	private SocketCommand decode(SocketCommand socketCommand, byte command) {
		socketCommand.command = command;
		return socketCommand;
	}
*/
	private SocketStringCommand decode(SocketStringCommand socketCommand, byte command, int length, byte[] message) {
		socketCommand.command = command;
		socketCommand.setMessage( new String(message, 0, length) );
		return socketCommand;
	}

	private void decode(SocketStringCommand socketCommand, byte command) throws Exception {
		try {
			final DataInputStream in = mIn;
			// Read until we have an int.
			final int textLength = in.readInt(); // wait forever?
			if (textLength == -1)
				throw new SocketException("Disconnected");

			//byte[] data = new byte[textLength];
			final int numRead = in.read(sData, 0, textLength); // wait forever?
			if (numRead == -1)
				throw new SocketException("Disconnected");

			if (numRead != textLength)
				throw new Exception("Incorrect length for SocketStringCommand " + socketCommand.command);

			decode(socketCommand, command, textLength, sData);

			//return socketCommand;

		} catch (IOException e) {
			throw new Exception("Couldn't parse SocketStringCommand " + socketCommand.command);
		}
	}

	private void decode(Version version, byte command) throws Exception {
		decode((SocketStringCommand) version, command);
		//return version;
	}

	private void decode(MidiSocketCommand socketCommand, byte command) throws Exception {
		try {
			final DataInputStream in = mIn;
			socketCommand.command = command;
			socketCommand.set(in.readByte(), in.readByte(), in.readByte(), in.readByte());
			//return socketCommand;
		} catch (IOException e) {
			throw new Exception("Couldn't parse MidiSocketCommand " + socketCommand.command);
		}
	}


	public interface IReceiver {
		public void onMidiSocketCommand(MidiSocketCommand socketCommand) throws Exception;
		public void onRequestMidiDeviceList(RequestMidiDeviceList socketCommand) throws Exception;
		public void onDeviceList(DeviceList socketCommand) throws Exception;
		public void onSetMidiDevice(SetMidiDevice socketCommand) throws Exception;
		public void onVersion(Version socketCommand) throws Exception;
	}

}
