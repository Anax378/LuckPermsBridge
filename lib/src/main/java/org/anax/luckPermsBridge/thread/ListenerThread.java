package org.anax.luckPermsBridge.thread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.anax.luckPermsBridge.command.CommandHandler;
import org.anax.luckPermsBridge.config.Config;
import org.slf4j.Logger;

class ListenerThread extends Thread{
	volatile boolean isRunning;
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

	@Override
	public void run(){
		isRunning = true;
		while(isRunning){
			try{
				Socket socket = serverSocket.accept();
				ConnectionHandler handler = new ConnectionHandler(socket, config, commandHandler, logger);
				pool.enqueue(handler::handle);
			}catch(IOException e){

			}
		}
	}

}


