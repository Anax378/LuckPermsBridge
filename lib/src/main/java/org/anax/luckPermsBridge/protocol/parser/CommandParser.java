package org.anax.luckPermsBridge.protocol.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.anax.luckPermsBridge.command.Command;


public class CommandParser{

	int readBytes = 0;
	int maxLength;
	boolean done = false;
	InputStream is;

	private CommandParser(InputStream is, int maxLength){
		this.maxLength = maxLength;
		this.is = is;
	}

	public static Command parse(InputStream is, int maxLength) throws CommandParsingException, IOException{
		return new CommandParser(is, maxLength).parseCommand();
	}

	private static boolean isSHA256Char(char ch){
		return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f');
	}
	private static boolean isDigit(char ch){
		return ch >= '0' && ch <= '9';
	}

	private Command parseCommand() throws CommandParsingException, IOException{
		String hash = parseSHA256();
		long timestamp = parseTimestamp();
		String action = parseWord();
		List<String> args = new ArrayList<>();
		while(!done){
			args.add(parseWord());
		}
		return new Command(hash, timestamp, action, args.toArray(new String[0]));
	}

	private int read() throws CommandParsingTooLongException, IOException{
		if(readBytes >= maxLength){
			throw new CommandParsingTooLongException(maxLength);
		}
		readBytes++;
		return is.read();
	}

	private long parseTimestamp() throws CommandParsingException, IOException{
		long ret = 0L;
		while(true){
			int in = read();
			if(in == -1){
				throw new CommandParsingBadSyntaxException("[0-9]", "<end of input>", readBytes);
			}
			char ch = (char)in;
			if(ch == ' '){
				return ret;
			}
			if(!isDigit(ch)){
				throw new CommandParsingBadSyntaxException(new String[]{"[0-9]", " "}, ""+ch, readBytes);
			}
			ret = (ret*10) + ch - '0';
		}
	}

	String parseWord() throws CommandParsingException, IOException{
		StringBuilder sb = new StringBuilder();
		while(true){
			int in = read();
			if(in == -1){
				throw new CommandParsingBadSyntaxException("<not end of input>", "<end of input>", readBytes);
			}
			char ch = (char)in;
			if(ch == ';'){
				done = true;
			}
			if(ch == ';' || ch == ' '){
				return sb.toString();
			}
			sb.append(ch);
		}
	}

	private String parseSHA256() throws CommandParsingException, IOException{
		char[] sha256 =  new char[64];
		for(int i = 0; i < 64; i++){
			int in = read();
			if(in == -1){
				throw new CommandParsingBadSyntaxException("[a-f0-9]", "<end of input>", readBytes);
			}
			char ch = (char)in; 
			if(!isSHA256Char(ch)){
				throw new CommandParsingBadSyntaxException("[a-f0-9]", "<"+((int)ch)+">"+ch, readBytes);
			}
			sha256[i] = ch;
		}
		int in = read();
		if(in != ' '){
			throw new CommandParsingBadSyntaxException(" ",
					in == -1
					? "<end of input>"
					: ""+((char)in)
					, readBytes
					);
		}
		return new String(sha256);
	}
}


