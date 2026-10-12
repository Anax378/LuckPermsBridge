package org.anax.luckPermsBridge;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;

import org.slf4j.Logger;

@Plugin(id = "luck-perms-bridge", name = "Luck Perms Bridge", version = "0.1.0-SNAPSHOT",
description = "here will be a description", authors = {"Anax378"})
public class LuckPermsBridge{

	private final ProxyServer server;
	private final Logger logger;
	private final Path dataDir;

	@Inject
	public LuckPermsBridge(ProxyServer server, Logger logger, @DataDirectory Path dataDir) {
		this.server = server;
		this.logger = logger;
		this.dataDir = dataDir;
	}
}


