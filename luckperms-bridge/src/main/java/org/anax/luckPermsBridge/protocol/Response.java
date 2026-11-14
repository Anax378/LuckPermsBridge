package org.anax.luckPermsBridge.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Response{
	public static enum ResponseType{
		OK("OK"),
		SYNTAX_ERROR("ERR-SYN"),
		TOO_LONG_ERROR("ERR-LEN"),
		COMMAND_ERROR("ERR-CMD"),
		TOO_OLD_ERROR("ERR-OLD"),
		INVALID_HASH_ERROR("ERR-HASH"),
		TIMEOUT_ERROR("ERR-TIME"),
		INTERNAL_SERVER_ERROR("ERR-SRV"),
			;

		public final String response;
		ResponseType(String response){
			this.response = response;
		}
	}

	ResponseType type;
	Optional<String> result;

	public Response(ResponseType type, Optional<String> result){
		this.type = type;
		this.result = result;
	}
	public Response(ResponseType type, String result){
		this(type, Optional.of(result));
	}
	public Response(ResponseType type){
		this(type, Optional.empty());
	}

	public void writeOnOutputstream(OutputStream os) throws IOException {
		os.write(type.response.getBytes(StandardCharsets.US_ASCII));
		if(result.isPresent()){
			os.write(' ');
			os.write(result.get().getBytes(StandardCharsets.US_ASCII));
		}
	}
}


