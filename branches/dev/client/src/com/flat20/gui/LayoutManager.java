package com.flat20.gui;

import java.io.File;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.flat20.fingerplay.midicontrollers.IMidiController;
import com.flat20.fingerplay.midicontrollers.MidiController;
import com.flat20.fingerplay.midicontrollers.Parameter;
import com.flat20.gui.widgets.Pad;
import com.flat20.gui.widgets.SensorSlider;
import com.flat20.gui.widgets.SensorXYPad;
import com.flat20.gui.widgets.Slider;
import com.flat20.gui.widgets.Widget;
import com.flat20.gui.widgets.WidgetContainer;
import com.flat20.gui.widgets.XYPad;

/**
 * TODO Move to GUI project.
 *
 * The resulting wc needs to go to NavigationOverlay but best would be to parse
 * the xml in to a data format and parse that into both MidiControllers
 * AND widgets. 
 * 
 * @author andreas
 *
 */
public class LayoutManager {

	final public static void loadXML(WidgetContainer mainContainer, InputStream xmlStream, int width, int height) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse( xmlStream );

			parseXML(mainContainer, doc, width, height);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	final public static void loadXML(WidgetContainer mainContainer, File xmlFile, int width, int height) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse( xmlFile );

			parseXML(mainContainer, doc, width, height);
		} catch (Exception e) {
			System.out.println(e);
		}


	}

	final public static void parseXML(WidgetContainer mainContainer, Document doc, int width, int height) {

		try {

			doc.getDocumentElement().normalize();

			// Figure out if it's new format or not based on the layout width compared to screen width.
			Element layout = doc.getDocumentElement();

			if (layout.getNodeName().equals("layouts")) {
				NodeList layouts = doc.getElementsByTagName("layout");
				int bestDiff = 10000;

				for (int l = 0; l < layouts.getLength(); l++) {
					if (layouts.item(l).getNodeType() == Node.ELEMENT_NODE) {
						Element tempLayout = (Element) layouts.item(l);
						int layoutWidth = getIntegerAttribute(tempLayout, "screenWidth");
						int diff = Math.abs(width - layoutWidth);
						if (diff < bestDiff) {
							bestDiff = diff;
							layout = tempLayout;
						}
					}
				}
			}

			parseLayout(mainContainer, layout, width, height);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	protected static void parseLayout(WidgetContainer mainContainer, Element layout, int androidWidth, int androidHeight) {

		int numTouchPads = 0;
		int numSliders = 0;
		int numButtons = 0;

		float deltaWidth = androidWidth / (float)getIntegerAttribute(layout, "screenWidth");
		float deltaHeight = androidHeight / (float)getIntegerAttribute(layout, "screenHeight");

		NodeList screens = layout.getElementsByTagName("screen");

		// Loop through all screens and add any widgets to the WidgetContainer.
		// A bit of a mix between view and data here.
		final int length = screens.getLength();
		for (int s = 0; s < length; s++) {
			
			if (screens.item(s).getNodeType() == Node.ELEMENT_NODE) {

				Log.i("LayoutManager", "TODO Not reading default values from XML. screen " + s);

				Element screenElement = (Element) screens.item(s);

				int screenX = (int) (getIntegerAttribute(screenElement, "x")*deltaWidth);
				int screenY = (int) (getIntegerAttribute(screenElement, "y")*deltaHeight);
				int screenWidth = (int) (getIntegerAttribute(screenElement, "width")*deltaWidth);
				int screenHeight = (int) (getIntegerAttribute(screenElement, "height")*deltaHeight);
				WidgetContainer wc = new WidgetContainer(screenWidth, screenHeight);
				wc.x = screenX;
				wc.y = screenY;

				NodeList widgets = screenElement.getChildNodes();
				final int wlength =  widgets.getLength();
				for (int e = 0; e < wlength; e++) {

					if (widgets.item(e).getNodeType() == Node.ELEMENT_NODE) {

						Element widgetElement = (Element) widgets.item(e);
						String name = widgetElement.getNodeName();

						int widgetX = (int)(getIntegerAttribute(widgetElement, "x")*deltaWidth);
						int widgetY = (int)(getIntegerAttribute(widgetElement, "y")*deltaHeight);
						int widgetWidth = (int)(getIntegerAttribute(widgetElement, "width")*deltaWidth);
						int widgetHeight = (int)(getIntegerAttribute(widgetElement, "height")*deltaHeight);
						int widgetControllerNumber = getIntegerAttribute(widgetElement, "controllerNumber", IMidiController.CONTROLLER_NUMBER_UNASSIGNED);

						MidiController mc = new MidiController();
						Widget widget = null;
						if (name.equals("button") || name.equals("pad")) {
							mc.setName( "Button " + (++numButtons) );
							Parameter parameters[] = {
									new Parameter(0, 0, widgetControllerNumber+0, "Press", Parameter.TYPE_NOTE, false)
									};
							mc.setParameters( parameters );
							widget = new Pad(mc);//"Button " + (++numButtons), widgetControllerNumber);

						} else if (name.equals("slider")) {
							mc.setName( "Slider " + (++numSliders) );

							Parameter parameters[] = {
									new Parameter(0, 0, widgetControllerNumber+0, "Press", Parameter.TYPE_NOTE, true),
									new Parameter(1, 0, widgetControllerNumber+1, "Vertical", Parameter.TYPE_CONTROL_CHANGE, true)
									};
							mc.setParameters(parameters);
							widget = new Slider(mc);//"Slider " + (++numSliders), widgetControllerNumber);

						} else if (name.equals("touchpad") || name.equals("xypad")) {
							mc.setName( "XY Pad " + (++numTouchPads) );
							Parameter parameters[] = {
									new Parameter(0, 0, widgetControllerNumber+0, "Press", Parameter.TYPE_CONTROL_CHANGE, true),
									new Parameter(1, 0, widgetControllerNumber+1, "Horizontal", Parameter.TYPE_CONTROL_CHANGE, true),
									new Parameter(2, 0, widgetControllerNumber+2, "Vertical", Parameter.TYPE_CONTROL_CHANGE, true)
									};
							mc.setParameters(parameters);
							widget = new XYPad(mc);//"XY Pad " + (++numTouchPads), widgetControllerNumber);
						}
						else if (name.equals("accelerometer") 
								|| name.equals("orientation") 
								|| name.equals("magfield")
								|| name.equals("gyroscope")) {	//3-axis
							mc.setName( "Sensor " + name + " " + (++numTouchPads) );
							Parameter parameters[] = {
									new Parameter(0, 0, widgetControllerNumber+0, "Press", Parameter.TYPE_CONTROL_CHANGE, true),
									new Parameter(1, 0, widgetControllerNumber+1, "Horizontal", Parameter.TYPE_CONTROL_CHANGE, true),
									new Parameter(2, 0, widgetControllerNumber+2, "Vertical", Parameter.TYPE_CONTROL_CHANGE, true)
									};
							mc.setParameters(parameters);
							widget = new SensorXYPad( mc );
							//widget = new XYPad("Sensor " + name, widgetControllerNumber);
						}
						else if (name.equals("light")
								|| name.equals("pressure")
								|| name.equals("proximity")
								|| name.equals("temperature")) {	//single value
							mc.setName( "Sensor " + name + " " + (++numSliders) );

							Parameter parameters[] = {
									new Parameter(0, 0, widgetControllerNumber+0, "Press", Parameter.TYPE_NOTE, false),
									new Parameter(1, 0, widgetControllerNumber+1, "Vertical", Parameter.TYPE_CONTROL_CHANGE, true)
									};
							mc.setParameters(parameters);
							widget = new SensorSlider(mc);//"Slider " + (++numSliders), widgetControllerNumber);
						}

						if (widget != null) {
							widget.x = widgetX;
							widget.y = widgetY;
							widget.setSize(widgetWidth, widgetHeight);
							wc.addSprite(widget);

							// Load any XML parameters
							updateParameters(mc, widgetElement);
						}
					}
				}

				mainContainer.addSprite( wc );

			}
		}

	}

	
	// 
	/**
	 * Overrides the default parameters for any controller.
	 * 
	 */
	protected static void updateParameters(IMidiController midiController, Element widgetElement) {

		//ArrayList<Parameter> parsedParameters = new ArrayList<Parameter>();
		NodeList parameters = widgetElement.getChildNodes();

		final int length = parameters.getLength();
		for (int e = 0; e < length; e++) {

			if (parameters.item(e).getNodeType() == Node.ELEMENT_NODE) {

				Element parameterElement = (Element) parameters.item(e);
				String name = parameterElement.getNodeName();

				if (name.equals("parameter")) {

					int parameterId = getIntegerAttribute(parameterElement, "id", -1);
					int channel = getIntegerAttribute(parameterElement, "channel", -1);
					int controllerNumber = getIntegerAttribute(parameterElement, "controllerNumber", -1);
					String parameterName = getStringAttribute(parameterElement, "name", null);
					String parameterTypeText = getStringAttribute(parameterElement, "type", null);

					Parameter parameter = midiController.getParameters()[parameterId];

					//if (parameterId != -1)
						//parameter.i = controllerNumber;

					if (parameterName != null)
						parameter.name = parameterName;

					if (channel != -1)
						parameter.channel = channel;

					if (controllerNumber != -1)
						parameter.controllerNumber = controllerNumber;
					
					if (parameterTypeText != null) {
						if ("controlChange".equals(parameterTypeText)) {
							parameter.type = Parameter.TYPE_CONTROL_CHANGE;
						} else if ("note".equals(parameterTypeText)) {
							parameter.type = Parameter.TYPE_NOTE;
						}
					}


				}
			}
		}

	}

	protected static Integer getIntegerAttribute(Element element, String attributeName) {
		return Integer.parseInt( element.getAttribute(attributeName) );
	}

	// Gets attribute integer value if the attribute is set and is a valid integer. Otherwise returns defaultValue.
	protected static Integer getIntegerAttribute(Element element, String attributeName, int defaultValue) {
		int result = defaultValue;

		try {
			if (element.hasAttribute(attributeName))
				result = Integer.parseInt( element.getAttribute(attributeName) );
		} catch (Exception e) {
		}

		return result;
	}

	protected static String getStringAttribute(Element element, String attributeName, String defaultValue) {
		String result = defaultValue;

		if (element.hasAttribute(attributeName))
			result = element.getAttribute(attributeName);
		if (result == null)
			result = defaultValue;

		return result;
	}

}
