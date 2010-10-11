package com.flat20.fingerplay.config.dto;

import com.flat20.fingerplay.config.IConfigurable;
import java.lang.Exception;


// config.WidgetItem? ConfigWidgetItem doesn't really specify if it is a MIDIController
// what do we for labels?
public class ConfigItem {

	public IConfigurable item;
	public ConfigItemParameters parameters;
	public String tagName;
	public String controllerClassName;
	public String viewClassName;
	public String displayName;
	public int x;
	public int y;
	public int width;
	public int height;

	/**
	 * Only works as long as IConfigurable item is still null.
	 */
	public ConfigItem clone() {

		if (item != null)
			return null;

		ConfigItem copy = new ConfigItem();

		copy.item = item;
		if (parameters != null)
			copy.parameters = parameters.clone();
		copy.tagName = tagName;
		copy.controllerClassName = controllerClassName;
		copy.viewClassName = viewClassName;
		copy.displayName = displayName;
		copy.x = x;
		copy.y = y;
		copy.width = width;
		copy.height = height;
		return copy;
	}

	public void hej() throws Exception {
		throw new Exception("adasd");
	}

	public String toString() {
		return "ConfigWidget item: " + item + ", tagName: " + tagName + ", controllerClassName: " + controllerClassName + ", viewClassName: " + viewClassName + ", displayName: " + displayName + ", x: " + x + ", y: " + y + ", width: " + width + ", height: " + height + ", params: " + parameters;
	}

}
