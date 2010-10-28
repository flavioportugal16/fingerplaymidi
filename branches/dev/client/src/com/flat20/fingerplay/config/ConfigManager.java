package com.flat20.fingerplay.config;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import android.os.Environment;

import com.flat20.fingerplay.config.dto.ConfigItem;
import com.flat20.fingerplay.config.dto.ConfigLayout;
import com.flat20.fingerplay.config.dto.ConfigScreen;
import com.flat20.fingerplay.midicontrollers.IMidiController;
import com.flat20.fingerplay.settings.SettingsModel;
import com.flat20.gui.widgets.Widget;

/**
 * Will parse the config and notify listeners (view, MidiControllerManager).
 *  
 * @author andreas.reuterberg
 *
 */
public class ConfigManager {

	private SettingsModel mSettingsModel;

    private ArrayList<IConfigUpdateListener> mListeners;

    private InputStream mDefaultConfigXml = null;
    private int mWidth;
    private int mHeight;


	// Singleton
	private static ConfigManager mInstance = null;
	public static ConfigManager getInstance() {
		if (mInstance == null)
			mInstance = new ConfigManager();
		return mInstance;
	}

	private ConfigManager() {
		//mConnectionManager.addConnectionListener(mConnectionListener);
		mSettingsModel = SettingsModel.getInstance();
		mListeners = new ArrayList<IConfigUpdateListener>();
	}

	public void addListener(IConfigUpdateListener listener) {
		mListeners.add(listener);
	}

	public void setDefaultConfigXml(InputStream defaultConfigXml) {
		mDefaultConfigXml = defaultConfigXml; 
	}
	
	public void setScreenSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	private ConfigReader getReader() {
        // TODO Move config parsing somewhere better
        File xmlFile = new File(Environment.getExternalStorageDirectory() + "/FingerPlayMIDI/" + mSettingsModel.layoutFile);

        ConfigReader reader = null;
        try {
        	if (mSettingsModel.layoutFile != null && xmlFile != null) {
    			reader = new ConfigReader( xmlFile );
        	}
		} catch (Exception e) {
			// Tried loading and parsing file but failed. Most likely the file wasn't there.
			System.out.println(e);
		}

		try {
			if (reader == null && mDefaultConfigXml != null) {
				mDefaultConfigXml.reset();
				reader = new ConfigReader( mDefaultConfigXml );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reader;
	}

	public void updateConfig() throws Exception {

		ConfigReader reader = getReader();
		if (reader == null)
			throw new Exception("No config file to parse!");

		try {
			ConfigLayout layout = reader.selectLayout(mWidth, mHeight);
			reader.parseLayout(layout); // Fills layout with info from the config file.

			// Instantiate controller classes.
			// MidiWidgetContainer instantiates the View class but the controllers
			// are created here. Maybe this class should do both..?

	        for (ConfigScreen screen : layout.screens) {
	        	for (ConfigItem configItem : screen.items) {
					Class<?> ControllerClass = Class.forName( configItem.controllerClassName );
					Class<?>[] classParams = new Class<?>[] {};
					Object[] objectParams = new Object[] {};
					Constructor<?> ctor = ControllerClass.getConstructor( classParams );
					IConfigurable controller = (IConfigurable) ctor.newInstance( objectParams );
					configItem.itemController = controller;

					configItem.itemController.setParameters(configItem.parameters);


					// TODO Instantiating the View class
					// Make a parent class for Widgets which doesn't rely on IMidiController as param
					/*
					if (configItem.viewClassName != null) {
						Class<?> WidgetClass = Class.forName( configItem.viewClassName );
						Class<?>[] viewClassParams = new Class<?>[] {IMidiController.class};
						Object[] viewObjectParams = new Object[] { configItem.itemController };
						Constructor<?> ctor = WidgetClass.getConstructor( classParams );
	
						Widget widget = (Widget) ctor.newInstance(objectParams);
					}*/

	        	}
	        }

	        for (IConfigUpdateListener listener : mListeners) {
	        	listener.onConfigUpdated(layout);
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
