package com.flat20.fingerplay.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OutputPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private final int COLUMN_SMARTFOX_USER_ID = 0;
	private final int COLUMN_SOURCE = 0;
	//private final int COLUMN_MESSAGE = 1;

	protected JProgressBar mProgressBar;
	DefaultTableModel mTableModel;
	//HashMap<MessageHandler, Integer> mMessageHandlers = new HashMap<MessageHandler, Integer>();

	public OutputPanel() {
		setLayout(new BorderLayout());

		mProgressBar = new JProgressBar();
		add(mProgressBar, BorderLayout.NORTH);
		createTable();
	}

	public void print(String text) {

		Object[] rowData = {"", text};
		mTableModel.addRow(rowData);

	}

/*
	public void addMessageHandler(MessageHandler messageHandler) {

		try {
			BasicLobbyHandler blh = (BasicLobbyHandler) messageHandler;
			blh.addListener(mMessageHandlerListener);
		} catch (ClassCastException e) {
			messageHandler.addListener(mMessageHandlerListener);
		}
		Object[] rowData = {"", "", "Client created"};
		mTableModel.addRow(rowData);
		mMessageHandlers.put(messageHandler, mTableModel.getRowCount()-1);

		mProgressBar.setMaximum(mMessageHandlers.size());
	}

	public boolean clearMessageHandlers() {
		for (MessageHandler messageHandler : mMessageHandlers.keySet()) {
			if (!messageHandler.isFinished()) {
				System.out.println("Want to clear but message handler wasn't finished?!");
				return false;
			}
		}
		mMessageHandlers.clear();
		while (mTableModel.getRowCount() > 0)
			mTableModel.removeRow(0);
		return true;
	}
*/
	private void createTable() {
		mTableModel = new DefaultTableModel();
		//mTableModel.addColumn("SF ID");
		mTableModel.addColumn("Source");
		mTableModel.addColumn("Message");
		JTable table = new JTable(mTableModel);

		//table.getColumnModel().getColumn(COLUMN_SMARTFOX_USER_ID).setMaxWidth(50);
		//table.getColumnModel().getColumn(COLUMN_SMARTFOX_USER_ID).setWidth(50);
		table.getColumnModel().getColumn(COLUMN_SOURCE).setMaxWidth(100);
		table.getColumnModel().getColumn(COLUMN_SOURCE).setWidth(100);

		table.setPreferredScrollableViewportSize(new Dimension(getWidth(), getHeight()));
		table.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}

/*
	protected BasicLobbyHandlerListener mMessageHandlerListener = new BasicLobbyHandlerListener() {

		public void onShutdown(MessageHandler messageHandler, String message) {
			Integer rowIndex = mMessageHandlers.get(messageHandler);
			mTableModel.setValueAt("Shutdown: " + message, rowIndex, COLUMN_MESSAGE);
			
		}

		public void onDisconnect(MessageHandler messageHandler) {
			Integer rowIndex = mMessageHandlers.get(messageHandler);
			mTableModel.setValueAt("Disconnected", rowIndex, COLUMN_MESSAGE);
		}

		public void onError(MessageHandler messageHandler, int errorCode, String errorCommand) {
			Integer rowIndex = mMessageHandlers.get(messageHandler);
			mTableModel.setValueAt("*** " + errorCode + ": " + errorCommand, rowIndex, COLUMN_MESSAGE);
		}

		// MessageHandler wants to print something
		public void onOutput(MessageHandler messageHandler, Object message) {
			Integer rowIndex = mMessageHandlers.get(messageHandler);
			mTableModel.setValueAt(message, rowIndex, COLUMN_MESSAGE);
		}

		@Override
		public void onLogin(BasicLobbyHandler messageHandler, int smartfoxUserId, String username) {
			Integer rowIndex = mMessageHandlers.get(messageHandler);
			mTableModel.setValueAt(smartfoxUserId, rowIndex, COLUMN_SMARTFOX_USER_ID);
			mTableModel.setValueAt(username, rowIndex, COLUMN_USERNAME);
			mProgressBar.setValue(mProgressBar.getValue()+1);
		}
		
		
	};
	
*/
}
