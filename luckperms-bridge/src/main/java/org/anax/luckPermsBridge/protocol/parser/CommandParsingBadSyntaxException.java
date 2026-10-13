package org.anax.luckPermsBridge.protocol.parser;

import java.util.Arrays;

public class CommandParsingBadSyntaxException extends CommandParsingException{
	public CommandParsingBadSyntaxException(String[] expected, String actual, int position, Exception e){
		super(makeMessage(expected, actual, position), e);
	}
	public CommandParsingBadSyntaxException(String[] expected, String actual, int position){
		this(expected, actual, position ,null);
	}
	public CommandParsingBadSyntaxException(String expected, String actual, int position, Exception e){
		this(new String[]{expected}, actual, position, e);
	}
	public CommandParsingBadSyntaxException(String expected, String actual, int position){
		this(expected, actual, position, null);
	}
	private static String makeMessage(String[] expected, String actual, int position){
		return "expected one of " + Arrays.toString(expected) + " but found \""+actual+"\" instead at position "+position;
	}

}


