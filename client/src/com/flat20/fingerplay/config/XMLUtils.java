package com.flat20.fingerplay.config;

import org.w3c.dom.Element;

// Should this be a static Util class?
public class XMLUtils {

	public static Integer getIntegerAttribute(Element element, String attributeName) {
		return Integer.parseInt( element.getAttribute(attributeName) );
	}

	// Gets attribute integer value if the attribute is set and is a valid integer. Otherwise returns defaultValue.
	public static Integer getIntegerAttribute(Element element, String attributeName, int defaultValue) {
		int result = defaultValue;

		try {
			if (element.hasAttribute(attributeName))
				result = Integer.parseInt( element.getAttribute(attributeName) );
		} catch (Exception e) {
		}

		return result;
	}

	public static String getStringAttribute(Element element, String attributeName, String defaultValue) {
		String result = defaultValue;

		if (element.hasAttribute(attributeName))
			result = element.getAttribute(attributeName);
		if (result == null)
			result = defaultValue;

		return result;
	}


}
