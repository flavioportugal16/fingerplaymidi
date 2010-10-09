package com.flat20.fingerplay;

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
