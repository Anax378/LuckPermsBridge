package org.anax.luckPermsBridge.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

public class Config{
	public static final String CONFFIG_FILE_NAME = "config.toml";

	int port;
	int maxRequestLength = 1028 * 1028;
	int maxWorkers = 10;
	long maxRequestAge = 5 * 60 * 1000;
	String secretSalt;

	public Config(int port, int maxRequestLength, int maxWorkers, long maxRequestAge, String secretSalt){
		this.port = port;
		this.maxRequestLength = maxRequestLength;
		this.maxWorkers = maxWorkers;
		this.maxRequestAge = maxRequestAge;
		this.secretSalt = secretSalt;
	}

	public String getSecretSalt(){
		return secretSalt;
	}

	public long getMaxRequestAge(){
		return maxRequestAge;
	}

	public int getPort() {
		return port;
	}
	public int getMaxRequestLength() {
		return maxRequestLength;
	}
	public int getMaxWorkers() {
		return maxWorkers;
	}

	public static void ensureContains(TomlParseResult res, String key) throws ConfigParsingException{
		if(!res.contains(key)){
			throw new ConfigParsingException(key+" not specified in config file");
		}

	}

	public static Config loadConfig(Class<?> resourceClass, Path dataDir) throws IOException, ConfigParsingException {
		Files.createDirectories(dataDir);
		Path configPath = dataDir.resolve(CONFFIG_FILE_NAME);
		if(Files.notExists(configPath)){
			try(InputStream is = resourceClass.getResourceAsStream("/config.toml")){
				if(is == null){
					throw new IOException("default config not found in jar");
				}
				Files.copy(is, configPath);
			}
		}
		TomlParseResult result = Toml.parse(configPath);
		if(!result.errors().isEmpty()){
			throw new ConfigParsingException("config file contains errors");
		}
		ensureContains(result, "port");
		ensureContains(result, "max-request-length");
		ensureContains(result, "max-request-age");
		ensureContains(result, "max-workers");
		ensureContains(result, "secret-salt");

		Object portO = result.get("port");
		Object maxRequestLengthO = result.get("max-request-length");
		Object maxRequestAgeO = result.get("max-request-age");
		Object maxWorkersO = result.get("max-workers");
		Object secretSaltO = result.get("secret-salt");
		
		if(!(portO instanceof Long port) || port < 0 || port > 0xffff){
			throw new ConfigParsingException("invalid value for port in config file");
		}
		if(!(maxRequestLengthO instanceof Long)){
			throw new ConfigParsingException("invalid value for max-request-length in config file");
		}
		if(!(maxRequestAgeO instanceof Long)){
			throw new ConfigParsingException("invalid value for max-request-age in config file");
		}
		if(!(maxWorkersO instanceof Long maxWorkers) || maxWorkers > Integer.MAX_VALUE){
			throw new ConfigParsingException("invalid value for max-request-age in config file");
		}
		if(!(secretSaltO instanceof String)){
			throw new ConfigParsingException("invalid value for secret-salt in config file");
		}
		return new Config(
				((Long)portO).intValue(),
				((Long)maxRequestLengthO).intValue(),
				((Long)maxWorkersO).intValue(),
				(Long)maxRequestAgeO,
				(String)secretSaltO
				);
	}

}


