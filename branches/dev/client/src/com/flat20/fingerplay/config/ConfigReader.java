package com.flat20.fingerplay.config;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.flat20.fingerplay.config.dto.ConfigItem;
import com.flat20.fingerplay.config.dto.ConfigItemParameters;
import com.flat20.fingerplay.config.dto.ConfigLayout;
import com.flat20.fingerplay.config.dto.ConfigScreen;
import com.flat20.fingerplay.midicontrollers.MidiController;

/**
 * Reads and parses layout XML files into ControllerInfo classes
 * @author andreas.reuterberg
 *
 */
public class ConfigReader {

	final private Document mXmlDoc;

	// Version number found on the root <layouts> tag
	private int mConfigFileVersion;

	// HashMap with widget name and its default parameter values.
	private HashMap<String, ConfigItemParameters> mDefaultParameters;

	// Where do we put these?
	int numTouchPads = 0;
	int numSliders = 0;
	int numButtons = 0;


	public ConfigReader(InputStream xmlStream) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		mXmlDoc = db.parse(xmlStream);

		init();
	}

	public ConfigReader(File xmlFile) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		mXmlDoc = db.parse( xmlFile );

		init();
	}

	private void init() throws Exception {

		mXmlDoc.getDocumentElement().normalize();

		Element root = mXmlDoc.getDocumentElement();
		if ( !root.getNodeName().equals("layouts") )
			throw new Exception("Root element of XML was not <layouts>");

		// Get the version number if it's set, default to 1 otherwise.
		mConfigFileVersion = XMLUtils.getIntegerAttribute(root, "version", 1);

		// Load all Widget definitions and their default settings.
		mDefaultParameters = parseDefinitions();
	}


	/**
	 * Selects the layout from the XML Document which best fits the
	 * specified width and height.
	 * 
	 * Perhaps this function should be moved outside of ConfigReader? 
	 * 
	 * @param width		Ideal width
	 * @param height	Ideal height
	 */
	public ConfigLayout selectLayout(int width, int height) throws Exception {

		ConfigLayout configLayout = new ConfigLayout();
		configLayout.ID = -1;

		// Get the correct layout for the current resolution
		NodeList layouts = mXmlDoc.getElementsByTagName("layout");
		int bestDiff = 10000;

		for (int l = 0; l < layouts.getLength(); l++) {
			if (layouts.item(l).getNodeType() == Node.ELEMENT_NODE) {
				Element tempLayout = (Element) layouts.item(l);
				int layoutWidth = XMLUtils.getIntegerAttribute(tempLayout, "screenWidth");
				int layoutHeight = XMLUtils.getIntegerAttribute(tempLayout, "screenHeight");
				int diff = Math.abs(width - layoutWidth);
				if (diff < bestDiff) {
					bestDiff = diff;
					configLayout.ID = l;
					configLayout.width = layoutWidth;
					configLayout.height = layoutHeight;
				}
			}
		}

		// Couldn't find any layout
		if (configLayout.ID == -1)
			throw new Exception("Unable to find a suitable layout for w: " + width + " h: " + height);

		return configLayout;
	}

	/**
	 * Fills the ConfigLayout with information parsed from the XML file.
	 * 
	 * @param layout
	 * @return
	 * @throws Exception
	 */
	public void parseLayout(ConfigLayout configLayout) throws Exception {

		// Get all layout tags and select the one specified by ConfigLayout ID.
		NodeList layouts = mXmlDoc.getElementsByTagName("layout");
		Element layout = (Element) layouts.item(configLayout.ID);
		NodeList screenElements = layout.getElementsByTagName("screen");

		// Get width & height
		configLayout.width = XMLUtils.getIntegerAttribute(layout, "screenWidth");
		configLayout.height = XMLUtils.getIntegerAttribute(layout, "screenHeight");

		final int length = screenElements.getLength();
		for (int s = 0; s < length; s++) {

			if (screenElements.item(s).getNodeType() == Node.ELEMENT_NODE) {

				Element screenElement = (Element) screenElements.item(s);

				ConfigScreen configScreen = new ConfigScreen();
				configScreen.x = XMLUtils.getIntegerAttribute(screenElement, "x");
				configScreen.y = XMLUtils.getIntegerAttribute(screenElement, "y");
				configScreen.width = XMLUtils.getIntegerAttribute(screenElement, "width");
				configScreen.height = XMLUtils.getIntegerAttribute(screenElement, "height");

				NodeList widgets = screenElement.getChildNodes();
				final int wlength =  widgets.getLength();
				for (int e = 0; e < wlength; e++) {

					if (widgets.item(e).getNodeType() == Node.ELEMENT_NODE) {

						Element widgetElement = (Element) widgets.item(e);
						ConfigItem configWidget = parseWidget(widgetElement);
						configScreen.items.add(configWidget);
					}
				}

				configLayout.screens.add(configScreen);

			}
		}

	}

	private HashMap<String, ConfigItemParameters> parseDefinitions() {
		// Get all controller definitions and their default values

		HashMap<String, ConfigItemParameters> defaultParameters = new HashMap<String, ConfigItemParameters>();

		NodeList defaults = mXmlDoc.getElementsByTagName("defaults");
		for (int l = 0; l < defaults.getLength(); l++) {
			if (defaults.item(l).getNodeType() == Node.ELEMENT_NODE) {
				Element tempDefault = (Element) defaults.item(l);

				// loop through defaults/controllers
				NodeList controllers = tempDefault.getChildNodes();
				for (int i = 0; i < controllers.getLength(); i++) {
					if (controllers.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element controller = (Element) controllers.item(i);

						String name = controller.getNodeName();
						//String widgetClass = getStringAttribute(controller, "class", null);

						// TODO Create the widget based on the class name.

						//System.out.println(name + " class = " + widgetClass);
						/*
						if (widgetClass != null) {
							try {
								Class<?> WidgetClass = Class.forName(widgetClass);
								Class parameterTypes[] = new Class[] { IMidiController.class };
								Constructor<?> ct = WidgetClass.getConstructor(parameterTypes);
								Object argumentList[] = new Object[] { null };

								Widget widget = (Widget) WidgetClass.newInstance();

								System.out.println(widget);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
							 */

						ConfigItemParameters hardcoded = new ConfigItemParameters();
						ConfigItemParameters parameters = parseParameters(controller, hardcoded);
						//if (parameters != null) {
							//System.out.println("Defaults for " + name);
							///System.out.println(parameters);
							
							defaultParameters.put(name, parameters);
							//updateParameters(parameters, controller);
						//} else {
							//Log.i("LayoutManager", "Couldn't find hardcoded defaults for " + name);
						//}
					}
				}
			}
		}
		
		return defaultParameters;

	}

	/**
	 * Parses an individual Widget XML tag in the layout and returns
	 * a ConfigWidgetItem
	 * 
	 * @param widgetElement
	 */
	private ConfigItem parseWidget(Element widgetElement) {
		String elementName = widgetElement.getNodeName();
		String widgetName = null;

		ConfigItem configWidget = null;
		IConfigurable widget = null;

		//Widget widget = null;
		if (elementName.equals("button") || elementName.equals("pad")) {
			MidiController mc = new MidiController();
			widgetName = "Button " + (++numButtons);
			mc.setName( widgetName );

			//Parameter[] parameters = cloneParameters(defaultParameters.get("pad"));

			//mc.setParameters( parameters );
			widget = mc;//new Pad(mc);

		} else if (elementName.equals("slider")) {
			MidiController mc = new MidiController();
			widgetName =  "Slider " + (++numSliders);
			mc.setName( widgetName );

			//Parameter[] parameters = cloneParameters(defaultParameters.get("slider"));

			//mc.setParameters( parameters );
			//widget = new Slider(mc);
			widget = mc;

		} else if (elementName.equals("touchpad") || elementName.equals("xypad")) {
			MidiController mc = new MidiController();
			widgetName = "XY Pad " + (++numTouchPads);
			mc.setName( widgetName );

			//Parameter[] parameters = cloneParameters(defaultParameters.get("xypad"));

			//mc.setParameters( parameters );
			//widget = new XYPad(mc);
			widget = mc;
		}
		else if (elementName.equals("accelerometer") 
				|| elementName.equals("orientation") 
				|| elementName.equals("magfield")
				|| elementName.equals("gyroscope")) {	//3-axis
			MidiController mc = new MidiController();
			widgetName = "Sensor " + elementName;
			mc.setName( widgetName );

			//Parameter[] parameters = cloneParameters(defaultParameters.get("xypad"));

			//mc.setParameters( parameters );
			//widget = new SensorXYPad( mc );
			widget = mc;
		}
		else if (elementName.equals("light")
				|| elementName.equals("pressure")
				|| elementName.equals("proximity")
				|| elementName.equals("temperature")) {	//single value
			MidiController mc = new MidiController();
			widgetName = "Sensor " + elementName;
			mc.setName( widgetName );

			//Parameter[] parameters = cloneParameters(defaultParameters.get("slider"));

			//mc.setParameters( parameters );
			//widget = new SensorSlider(mc);
			widget = mc;

		} else if (elementName.equals("label")) {
			System.out.println("This is where we'd create a label widget..");
		}

		if (widget != null) {
			configWidget = new ConfigItem();
			configWidget.item = widget;
			configWidget.tagName = elementName;
			configWidget.displayName = widgetName;
			configWidget.x = XMLUtils.getIntegerAttribute(widgetElement, "x");
			configWidget.y = XMLUtils.getIntegerAttribute(widgetElement, "y");
			configWidget.width = XMLUtils.getIntegerAttribute(widgetElement, "width");
			configWidget.height = XMLUtils.getIntegerAttribute(widgetElement, "height");

			// Load any XML parameters

			// need to load default parameters.
			ConfigItemParameters defaultParameters = mDefaultParameters.get(elementName);
			
			System.out.println(" WIDGET ------ " + widgetName);

			ConfigItemParameters parameters = parseParameters( widgetElement, defaultParameters );
			configWidget.item.setParameters(parameters);

			//updateParameters(mc.getParameters(), widgetElement);
			//System.out.println(mc);
		} 

		// Returns null if widget was null
		return configWidget;
	}


	// Needs to 
	private ConfigItemParameters parseParameters(Element widgetElement, ConfigItemParameters defaults) {

		ConfigItemParameters itemParameters = new ConfigItemParameters();
		NodeList parameterElements = widgetElement.getChildNodes();

		// Copy default values

		for (HashMap<String, Object> parameter : defaults.data) {
			
			HashMap<String, Object> parameterCopy = new HashMap<String, Object>();
			for (String key : parameter.keySet()) {
				parameterCopy.put(key, parameter.get(key));
			}
			
			itemParameters.data.add( parameterCopy );
		}

		
		final int length = parameterElements.getLength();
		
		for (int e = 0; e < length; e++) {

			if (parameterElements.item(e).getNodeType() == Node.ELEMENT_NODE) {

				Element parameterElement = (Element) parameterElements.item(e);
				String name = parameterElement.getNodeName();

				if (name.equals("parameter")) {



					//HashMap<String, Object> parameters = new HashMap<String, Object>();
					NamedNodeMap attributes = parameterElement.getAttributes();

					int id = Integer.parseInt((String)attributes.getNamedItem("id").getNodeValue());

					HashMap<String, Object> parameters = itemParameters.getParameterById(id);
					if (parameters == null) {
						parameters = new HashMap<String, Object>();
						itemParameters.data.add(parameters);
					}

					System.out.println("attributes length: " + attributes.getLength());
					for (int a=0; a < attributes.getLength(); a++) {
						Node node = attributes.item(a);
						parameters.put(node.getNodeName(), node.getNodeValue());
					}

					// Only add <parameter> if it has id set.
					//if (parameters.get("id") != null) {
						/*
						int id = Integer.parseInt( (String)parameters.get("id") );
						HashMap<String, Object> def = defaults.getParameterById(id);

						// Look through defaults and add any missing values from the defaults.
						// TODO: def should never be null when this code is finished
						if (def != null) {
							for (String key : def.keySet()) {
								if (parameters.get(key) == null)
									parameters.put(key, def.get(key));
							}
						}*/

						//itemParameters.data.add(parameters);
					//} else {
						//System.out.println("Warning: <parameter> didn't have id set.");
					//}
				}
			}
		}

		return itemParameters;

	}

}
