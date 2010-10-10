package com.flat20.fingerplay.config;

import com.flat20.fingerplay.config.dto.ConfigItemParameters;

/**
 * 
 * @author andreas.reuterberg
 *
 */
public interface IConfigurable {
	// XML <parameters> contained within the tag is passed to the IConfigurable
	// as an array. The first being attributes found on the main element itself
	
	public void setParameters( ConfigItemParameters parameters);
	
	// Would this be useful? Call once to set the default parameters statically on the 
	// object? Or the Config file could be clever enough to feed default values
	// to setParameter if needed.
	//public void setDefaultParameters( ArrayList<HashMap<String, Object>> parameters );

	// Maybe the defaults gets added to a XYPadFactory class which then decides what to do?
}
