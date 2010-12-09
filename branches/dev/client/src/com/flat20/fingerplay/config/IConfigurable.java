package com.flat20.fingerplay.config;

import com.flat20.fingerplay.config.dto.ConfigItemParameters;

/**
 * 
 * @author andreas.reuterberg
 *
 */
public interface IConfigurable {
	// XML <parameters> contained within the tag is passed to the IConfigurable
	// as an array. The first being attributes found on the main element itself.
	// A MidiWidget could decide to use the first attributes as defaults for any
	// missing attributes inside the tag. Or controllerNumber from the main
	// element could be assigned incrementally to any of the following parameters.

	public void setParameters( ConfigItemParameters parameters);

	// What about controllers without views?
	public void setView(IConfigItemView view) throws Exception;
	public IConfigItemView getView();


	// Would this be useful? Call once to set the default parameters statically on the 
	// object? Or the Config file could be clever enough to feed default values
	// to setParameter if needed.
	//public void setDefaultParameters( ArrayList<HashMap<String, Object>> parameters );

	// Maybe the defaults gets added to a XYPadFactory class which then decides what to do?
}
