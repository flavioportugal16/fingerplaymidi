package com.flat20.fingerplay.config.parsers;

import java.io.File;
import java.io.InputStream;

import com.flat20.fingerplay.config.dto.ConfigLayout;

public interface IParser {
	public void setInput(InputStream stream) throws Exception;
	public void setInput(File file) throws Exception;
	public ConfigLayout selectLayout(int width, int height) throws Exception;
	public void parseLayout(ConfigLayout configLayout) throws Exception;
}
