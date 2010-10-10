package com.flat20.fingerplay.config.dto;

import java.util.ArrayList;

/**
 * Contains parsed info from one <layout> in the config file.
 * 
 * @author andreas.reuterberg
 *
 */
public class ConfigLayout {
	
	public int ID;
	
	public ArrayList<ConfigScreen> screens = new ArrayList<ConfigScreen>();
	public int version;
	public int width;
	public int height;
}
