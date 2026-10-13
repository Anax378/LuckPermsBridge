package org.anax.luckPermsBridge.protocol.parser;

public abstract class CommandParsingException extends Exception{
	public CommandParsingException(String message, Exception e){
		super(message, e);
	}
	public CommandParsingException(String message){
		super(message);
	}
} 


