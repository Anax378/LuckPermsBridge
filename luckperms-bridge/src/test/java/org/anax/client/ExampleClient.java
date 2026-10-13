package org.anax.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class ExampleClient {
	String host;
	int port;
	String secretHash;

	public ExampleClient(String host, int port, String secretHash){
		this.host = host;
		this.port = port;
		this.secretHash = secretHash;
	}

	public String setRole(String role, String uuid) throws UnknownHostException, NoSuchAlgorithmException, IOException{
		return sendCommand("SET-ROLE "+role+" "+uuid+";");
	}
	public String addRole(String role, String uuid) throws UnknownHostException, NoSuchAlgorithmException, IOException{
		return sendCommand("ADD-ROLE "+role+" "+uuid+";");
	}
	public String setDefaultRole(String uuid) throws UnknownHostException, NoSuchAlgorithmException, IOException{
		return sendCommand("SET-DEF-ROLE "+uuid+";");
	}
	public String movePlayerName(String player, String server) throws UnknownHostException, NoSuchAlgorithmException, IOException{
		return sendCommand("MOVE-PLAYER-NAME "+server+" "+player+";");
	}
	public String movePlayerUUID(String uuid, String server) throws UnknownHostException, NoSuchAlgorithmException, IOException{
		return sendCommand("MOVE-PLAYER-UUID "+server+" "+uuid+";");
	}

	public String sendCommand(String command) throws UnknownHostException, IOException, NoSuchAlgorithmException{
		Socket socket = new Socket(host, port);
		String timestamp = Long.toString(System.currentTimeMillis());
		MessageDigest digest = MessageDigest.getInstance("SHA256");
		digest.update(timestamp.getBytes(StandardCharsets.US_ASCII));
		digest.update((byte)' ');
		digest.update(command.getBytes(StandardCharsets.US_ASCII));
		digest.update(secretHash.getBytes(StandardCharsets.US_ASCII));

		OutputStream os = socket.getOutputStream();
		os.write(HexFormat.of().formatHex(digest.digest()).getBytes(StandardCharsets.US_ASCII));
		os.write((byte)' ');
		os.write(timestamp.getBytes(StandardCharsets.US_ASCII));
		os.write((byte)' ');
		os.write(command.getBytes(StandardCharsets.US_ASCII));

		InputStream is = socket.getInputStream();

		StringBuilder sb = new StringBuilder();
		int in;
		while((in = is.read()) != -1){
			char ch = (char)in;
			sb.append(ch);
			if(ch == ';'){
				break;
			}
		}
		socket.close();
		return sb.toString();
	}
}

