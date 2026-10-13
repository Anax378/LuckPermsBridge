package org.anax.luckPermsBridge;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;

import javax.xml.catalog.CatalogManager;

import org.anax.luckPermsBridge.command.CommandHandler;
import org.anax.luckPermsBridge.command.LuckPermsVelocityController;
import org.anax.luckPermsBridge.config.Config;
import org.anax.luckPermsBridge.config.ConfigParsingException;
import org.anax.luckPermsBridge.thread.ListenerThread;
import org.slf4j.Logger;


@Plugin(
	id = "luckperms-bridge",
	name = "LuckPerms Bridge",
	version = "1.0.0-SNAPSHOT",
	description = "Provides a bridge via TCP to send predefined commands",
	url = "https://github.com/Anax378/LuckPermsBridge",
	authors = {"Anax378"},
	dependencies = {@Dependency(id = "luckperms", optional = false)}
)
public class LuckPermsBridge{

	private final ProxyServer proxy;
	private final Logger logger;
	private final Path dataDir;
	private Config config;
	private ServerSocket socket;
	private ListenerThread lthread = null;

	@Inject
	public LuckPermsBridge(ProxyServer proxy, Logger logger, @DataDirectory Path dataDir) {
		this.proxy = proxy;
		this.logger = logger;
		this.dataDir = dataDir;
		try{
			this.config = Config.loadConfig(getClass(), dataDir);
		}catch(IOException | ConfigParsingException e){
			throw new RuntimeException(e);
		}

		try{
			socket = new ServerSocket(config.getPort());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		logger.info("loaded successfully");
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event){
		LuckPermsVelocityController controller = new LuckPermsVelocityController(LuckPermsProvider.get(), proxy);
		CommandHandler handler = new CommandHandler(controller);
		this.lthread = new ListenerThread(socket, handler, config, logger);
		this.lthread.start();
		logger.info("ListenerThread started");
	}

	@Subscribe
	public void onProxyShutdown(ProxyShutdownEvent event){
		if(lthread != null){
			lthread.shutdown();
		}
	}
}


