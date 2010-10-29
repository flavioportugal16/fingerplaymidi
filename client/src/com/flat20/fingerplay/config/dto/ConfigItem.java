package com.flat20.fingerplay.config.dto;

import com.flat20.fingerplay.config.IConfigItemView;
import com.flat20.fingerplay.config.IConfigurable;

public class ConfigItem {

	public IConfigurable itemController;
	public IConfigItemView itemView;
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
	 * Only works properly as long as IConfigurable and IView items are still null.
	 * Otherwise it won't be a complete copy since they're passed by reference.
	 */
	public ConfigItem clone() {

		if (itemController != null && itemView != null)
			return null;

		ConfigItem copy = new ConfigItem();

		copy.itemController = itemController;
		copy.itemView = itemView;
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

	public String toString() {
		return "ConfigWidget item: " + itemController + ", view: " + itemView + " tagName: " + tagName + ", controllerClassName: " + controllerClassName + ", viewClassName: " + viewClassName + ", displayName: " + displayName + ", x: " + x + ", y: " + y + ", width: " + width + ", height: " + height + ", params: " + parameters;
	}

}
