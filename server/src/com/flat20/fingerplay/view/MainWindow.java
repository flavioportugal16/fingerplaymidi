package com.flat20.fingerplay.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;


public class MainWindow extends JFrame implements IView, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected OutputPanel mOutput;
/*
	protected SettingsPanel mSettingsPanel;
	protected Output mOutput;
*/	
	protected WindowListener mWindowListener = null;

	public MainWindow() {

		setLayout(new BorderLayout());
/*
		mSettingsPanel = new SettingsPanel();
		mSettingsPanel.getConnectButton().addActionListener(this);
		add( mSettingsPanel, BorderLayout.NORTH );
*/
		mOutput = new OutputPanel();
		add( mOutput, BorderLayout.CENTER );

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLocation(300, 300);
		setSize(500, 400);
		setVisible(true);
	}
/*
	public void addMessageHandler(MessageHandler messageHandler) {
		mOutput.addMessageHandler(messageHandler);
	}

	public boolean clearMessageHandlers() {
		return mOutput.clearMessageHandlers();
	}
*/
	public void setWindowListener(WindowListener windowListener) {
		mWindowListener = windowListener;
	}
/*
	public SettingsPanel getSettingsPanel() {
		return mSettingsPanel;
	}
*/

	public void actionPerformed(ActionEvent arg0) {
		/*
		// TODO Auto-generated method stub
		if (mWindowListener != null) {
			if (mSettingsPanel.getConnectButton().getLabel().equals("  Connect  ")) {
				mWindowListener.onConnect();
				mSettingsPanel.getConnectButton().setLabel("Disconnect");
			} else {
				mWindowListener.onDisconnect();
				mSettingsPanel.getConnectButton().setLabel("  Connect  ");
			}
		}*/

	}

	// IView
	public void print(String text) {
		mOutput.print(text);
	}
}
