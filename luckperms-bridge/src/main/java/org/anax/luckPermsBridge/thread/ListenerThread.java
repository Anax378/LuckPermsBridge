package org.anax.luckPermsBridge.thread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.anax.luckPermsBridge.command.CommandHandler;
import org.anax.luckPermsBridge.config.Config;
import org.slf4j.Logger;

public class ListenerThread extends Thread{
	volatile boolean isRunning = true;
	ServerSocket serverSocket;
	CommandHandler commandHandler;
	Config config;
	Logger logger;
	ThreadPool pool;

	public ListenerThread(
			ServerSocket serverSocket,
			CommandHandler commandHandler,
			Config config,
			Logger logger
			){
		this.serverSocket = serverSocket;
		this.commandHandler = commandHandler;
		this.config = config;
		this.logger = logger;
		this.pool = new ThreadPool(config.getMaxWorkers());
	}

	public void shutdown(){
		this.isRunning = false;
	}

	@Override
	public void run(){
		while(isRunning && serverSocket != null && !serverSocket.isClosed()){
			try{
				Socket socket = serverSocket.accept();
				socket.setSoTimeout(config.getSocketTimeout());
				ConnectionHandler handler = new ConnectionHandler(socket, config, commandHandler, logger);
				pool.enqueue(handler::handle);
			}catch(IOException e){
				logger.warn("error while accepting socket", e);
			}
		}
		try{
			serverSocket.close();
		}catch(IOException e){
			logger.warn("could not close serverSocket", e);
		}
	}

}


