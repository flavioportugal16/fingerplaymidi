package com.flat20.fingerplay.config;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import android.os.Environment;

import com.flat20.fingerplay.config.dto.ConfigItem;
import com.flat20.fingerplay.config.dto.ConfigLayout;
import com.flat20.fingerplay.config.dto.ConfigScreen;
import com.flat20.fingerplay.settings.SettingsModel;

/**
 * Will parse the config and notify listeners (view, MidiControllerManager)
 * when it's ready
 * 
 *  
 * @author andreas.reuterberg
 *
 */
public class ConfigManager {

	private SettingsModel mSettingsModel;

    private ArrayList<IConfigUpdateListener> mListeners;

    //private MidiWidgetContainer mMidiWidgetsContainer;
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
			// TODO Move this
 
	        for (ConfigScreen screen : layout.screens) {
	        	for (ConfigItem configItem : screen.items) {
					Class<?> ControllerClass = Class.forName( configItem.controllerClassName );
					Class<?>[] classParams = new Class<?>[] {};
					Object[] objectParams = new Object[] {};
					Constructor<?> ctor = ControllerClass.getConstructor( classParams );
					IConfigurable controller = (IConfigurable) ctor.newInstance( objectParams );
					configItem.itemController = controller;

					configItem.itemController.setParameters(configItem.parameters);
	        	}
	        }

	        for (IConfigUpdateListener listener : mListeners) {
	        	listener.onConfigUpdated(layout);
	        }
	        /*
			// Add all MIDI controllers to the MidiControllerManager
        	mMidiControllerManager.setConfigItems( layout );

        	// Create Views for all items
        	mMidiWidgetsContainer.setConfigItems( layout );
*/

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
