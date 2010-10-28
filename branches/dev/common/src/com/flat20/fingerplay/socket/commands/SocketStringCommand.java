package com.flat20.fingerplay.socket.commands;

import com.flat20.fingerplay.socket.commands.SocketCommand;

public class SocketStringCommand extends SocketCommand {

	protected String message = null;

	public SocketStringCommand() {
		super();
	}

	public SocketStringCommand(byte command) {
		super(command);
	}

	public SocketStringCommand(byte command, String message) {
		super(command);
		setMessage(message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
