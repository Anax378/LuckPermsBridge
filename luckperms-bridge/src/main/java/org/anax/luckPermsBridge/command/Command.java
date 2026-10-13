package org.anax.luckPermsBridge.command;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;

public record Command(
		String hash,
		long timestamp,
		String action,
		String[] args
	){

	public boolean validateHash(String secretSalt){
		MessageDigest digest;
		try{
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(Long.toString(timestamp).getBytes(StandardCharsets.US_ASCII));
			digest.update((byte)' ');
			digest.update(action.getBytes(StandardCharsets.US_ASCII));
			for(String arg : args){
				digest.update((byte)' ');
				digest.update(arg.getBytes(StandardCharsets.US_ASCII));
			}
			digest.update((byte)';');
			digest.update(secretSalt.getBytes(StandardCharsets.US_ASCII));

			byte[] hash = digest.digest();
			return HexFormat.of().formatHex(hash).equals(this.hash);

		}catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
	}

	public boolean validateAge(Long timeout){
		return (timestamp - timeout) < System.currentTimeMillis();
	}

	@Override
	public String toString(){
		return "{hash: "+hash+", timestampt: "+timestamp+", action: "+action+", args: "+Arrays.toString(args)+"}";
	}
}


