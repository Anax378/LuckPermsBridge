package org.anax.luckPermsBridge.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public enum Response{
	OK("OK"),
	SYNTAX_ERROR("ERR-SYN"),
	TOO_LONG_ERROR("ERR-LEN"),
	COMMAND_ERROR("ERR-CMD"),
	TOO_OLD_ERROR("ERR-OLD"),
	INVALID_HASH_ERROR("ERR-HASH"),
	TIMEOUT_ERROR("ERR-TIME");

	public final String response;

	Response(String response){
		this.response = response;
	}

	public void writeOnOutputstream(OutputStream os) throws IOException {
		os.write(response.getBytes(StandardCharsets.US_ASCII));
	}
}


