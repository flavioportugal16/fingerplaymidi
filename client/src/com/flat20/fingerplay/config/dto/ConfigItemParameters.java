package com.flat20.fingerplay.config.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigItemParameters {

	public ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();

	public HashMap<String, Object> getParameter(String name) {
		for (HashMap<String, Object> parameter : data) {
			if (name.equals(parameter.get("name")))
				return parameter;
		}
		return null;
	}

	public HashMap<String, Object> getParameterById(int id) {
		String stringId = ""+id;
		for (HashMap<String, Object> parameter : data) {
			if (stringId.equals(parameter.get("id")))
				return parameter;
		}
		return null;
	}

	/**
	 * Doesn't create copies of values
	 */
	public ConfigItemParameters clone() {

		ConfigItemParameters copy = new ConfigItemParameters(); 

		for (HashMap<String, Object> parameter : data) {

			HashMap<String, Object> parameterCopy = new HashMap<String, Object>();
			for (String key : parameter.keySet()) {
				parameterCopy.put(key, parameter.get(key));
			}

			copy.data.add( parameterCopy );
		}

		return copy;
	}

	public String toString() {
		String res = "ConfigItemParameters: ";
		for (HashMap<String, Object> parameter : data) {
			for (String key : parameter.keySet()) {
				res += key + ": " + parameter.get(key) + ",";
			}
		}
		return res;
	}
}
