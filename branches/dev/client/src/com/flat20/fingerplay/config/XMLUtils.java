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

	/**
	 * Returns all child Elements of another Element.
	 * 
	 * @param element
	 * @return
	 */
	/*
	public static ArrayList<Element> getChildElements(Element element) {
		return getChildElements(element, null);
	}*/

	/**
	 * Returns all child Elements with the specified name.
	 * 
	 * @param element
	 * @param childElementName
	 * @return
	 */
	/*
	public static ArrayList<Element> getChildElements(Element element, String childElementName) {

		ArrayList<Element> children = new ArrayList<Element>();


		NodeList childNodes = element.getChildNodes();
		final int length = childNodes.getLength();
		for (int e = 0; e < length; e++) {

			if (childNodes.item(e).getNodeType() == Node.ELEMENT_NODE) {

				Element childElement = (Element) childNodes.item(e);
				String name = childElement.getNodeName();

				if (childElementName != null && name.equals( childElementName )) {
					children.add(childElement);
				}
			}
		}
		
		return children;

	}*/
}
