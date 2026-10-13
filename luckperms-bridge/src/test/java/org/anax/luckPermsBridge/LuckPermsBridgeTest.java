package org.anax.luckPermsBridge;

import org.anax.client.ExampleClient;
import org.anax.luckPermsBridge.command.Command;
import org.anax.luckPermsBridge.command.CommandHandler;
import org.anax.luckPermsBridge.command.TestServerController;
import org.anax.luckPermsBridge.config.Config;
import org.anax.luckPermsBridge.protocol.parser.CommandParser;
import org.anax.luckPermsBridge.thread.ConnectionHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class LuckPermsBridgeTest {
	@Test
	public void TestParseCommand(){
		String input = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08 42 TEST arg1 arg2 arg3;";
		InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
		try{
			Command c = CommandParser.parse(is, input.length());
			System.out.println(c);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testHandler(){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Socket sock = createTestSocket("bad", os);
		Config config = new Config(0, 1028*1028, 10, 10000, "secret");
		CommandHandler commandHandler = new CommandHandler(new TestServerController());
		Logger logger = LoggerFactory.getLogger("TEST");
		ConnectionHandler handler = new ConnectionHandler(sock, config, commandHandler, logger);
		handler.handle();
		System.out.println(os.toString(StandardCharsets.US_ASCII));
	}

	public Socket createTestSocket(String in, ByteArrayOutputStream os){
		return new Socket(){
			private InputStream is = new ByteArrayInputStream(in.getBytes(StandardCharsets.US_ASCII));

			@Override 
			public InputStream getInputStream(){
				return is;
			}
			@Override
			public OutputStream getOutputStream(){
				return os;
			}
		};
	}
}

