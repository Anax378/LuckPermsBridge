package org.anax.luckPermsBridge.command;
public class BadCommandException extends Exception{

	public BadCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadCommandException(String message) {
		super(message);
	}

}


