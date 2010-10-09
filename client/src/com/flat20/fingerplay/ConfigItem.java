package com.flat20.fingerplay;

// config.WidgetItem? ConfigWidgetItem doesn't really specify if it is a MIDIController
// what do we for labels?
public class ConfigItem {

	public IConfigurable item;
	public String tagName;
	public String className;
	public String displayName;
	public int x;
	public int y;
	public int width;
	public int height;
	
	public String toString() {
		return "ConfigWidget item: " + item + ", name: " + displayName + ", x: " + x + ", y: " + y + ", width: " + width + ", height: " + height;
	}
}
