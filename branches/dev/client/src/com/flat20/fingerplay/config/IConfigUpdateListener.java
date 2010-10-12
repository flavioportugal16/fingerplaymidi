package com.flat20.fingerplay.config;

import com.flat20.fingerplay.config.dto.ConfigLayout;

public interface IConfigUpdateListener {
	public void onConfigUpdated(ConfigLayout newLayout);
}
