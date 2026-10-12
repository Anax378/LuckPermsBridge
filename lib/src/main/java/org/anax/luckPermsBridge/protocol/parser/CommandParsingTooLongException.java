package org.anax.luckPermsBridge.protocol.parser;

public class CommandParsingTooLongException extends CommandParsingException {

	CommandParsingTooLongException(int maxLength, Exception e){
		super("maximum length of "+maxLength+" exceeded.", e);
	}
	CommandParsingTooLongException(int maxLength){
		this(maxLength, null);
	}
}

