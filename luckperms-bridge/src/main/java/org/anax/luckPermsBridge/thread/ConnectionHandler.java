package org.anax.luckPermsBridge.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.anax.luckPermsBridge.command.CommandException;
import org.anax.luckPermsBridge.command.Command;
import org.anax.luckPermsBridge.command.CommandHandler;
import org.anax.luckPermsBridge.config.Config;
import org.anax.luckPermsBridge.protocol.Response;
import org.anax.luckPermsBridge.protocol.Response.ResponseType;
import org.anax.luckPermsBridge.protocol.parser.CommandParser;
import org.anax.luckPermsBridge.protocol.parser.CommandParsingException;
import org.anax.luckPermsBridge.protocol.parser.CommandParsingTooLongException;
import org.slf4j.Logger;

public class ConnectionHandler{
	Socket socket;
	Config config;
	CommandHandler handler;
	Logger logger;

	public ConnectionHandler(Socket socket, Config config, CommandHandler handler, Logger logger){
		this.socket = socket;
		this.config = config;
		this.handler = handler;
		this.logger = logger;
	}

	public void handle(){
		Response response = new Response(ResponseType.INTERNAL_SERVER_ERROR);
		try{
			InputStream is = socket.getInputStream();
			Command command = CommandParser.parse(is, config.getMaxRequestLength());
			if(!command.validateAge(config.getMaxRequestAge())){
				throw new CommandTooOldException();
			}
			if(!command.validateHash(config.getSecretSalt())){
				throw new InvalidHashException();
			}
			response = new Response(ResponseType.OK, handler.handle(command));

		} catch (SocketTimeoutException e){
			logger.info(e.toString());
			response = new Response(ResponseType.TIMEOUT_ERROR);
		} catch(IOException e){
			logger.warn(e.toString());
		} catch (CommandParsingException e) {
			if(e instanceof CommandParsingTooLongException){
				response = new Response(ResponseType.TOO_LONG_ERROR);
				logger.info(e.toString());
			}else{
				response = new Response(ResponseType.SYNTAX_ERROR);
				logger.info(e.toString());
			}
			logger.warn(e.toString());
		} catch (CommandTooOldException e) {
			response = new Response(ResponseType.TOO_OLD_ERROR);
			logger.info(e.toString());
		} catch (InvalidHashException e) {
			response = new Response(ResponseType.INVALID_HASH_ERROR);
			logger.info(e.toString());
		} catch (CommandException e) {
			response = new Response(ResponseType.COMMAND_ERROR);
			logger.warn(e.toString());
		} catch (Throwable t){
			response = new Response(ResponseType.INTERNAL_SERVER_ERROR);
			logger.warn(t.toString());
		}
		finally{
			try{
				response.writeOnOutputstream(socket.getOutputStream());
				socket.getOutputStream().write(';');
			}catch(IOException e){
				logger.warn("could not write response: " + e.toString());
			}finally{
				try{
					socket.close();
				}catch(IOException e){
					logger.warn("could not close socket: " + e.toString());
				}
			}
		}
	}
}

