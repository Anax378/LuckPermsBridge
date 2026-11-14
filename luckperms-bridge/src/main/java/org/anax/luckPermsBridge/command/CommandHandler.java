package org.anax.luckPermsBridge.command;

import java.util.Optional;
import java.util.UUID;

public class CommandHandler{
	IServerController controller;

	public CommandHandler(IServerController controller){
		this.controller = controller;
	}

	public Optional<String> handle(Command command) throws CommandException{
		if(command.action().equals("SET-ROLE")){
			validateArgsLen(2, command);
			String role = command.args()[0];
			UUID uuid = parseUUID(command.args()[1]);
			controller.setPlayerRole(role, uuid);
			return Optional.empty();
		}
		else if(command.action().equals("ADD-ROLE")){
			validateArgsLen(2, command);
			String role = command.args()[0];
			UUID uuid = parseUUID(command.args()[1]);
			controller.addPlayerRole(role, uuid);
			return Optional.empty();
		}
		else if(command.action().equals("REM-ROLE")){
			validateArgsLen(2, command);
			String role = command.args()[0];
			UUID uuid = parseUUID(command.args()[1]);
			controller.removePlayerRole(role, uuid);
			return Optional.empty();
		}
		else if(command.action().equals("HAS-ROLE")){
			validateArgsLen(2, command);
			String role = command.args()[0]; 
			UUID uuid = parseUUID(command.args()[1]);
			boolean has = controller.hasRole(role, uuid);
			return Optional.of(has ? "TRUE" : "FALSE");
		}
		else if(command.action().equals("MOVE-PLAYER-UUID")){
			validateArgsLen(2, command);
			String server = command.args()[0];
			UUID uuid = parseUUID(command.args()[1]);
			controller.moveUUIDPlayerToServer(server, uuid);
			return Optional.empty();
		}
		else if(command.action().equals("MOVE-PLAYER-NAME")){
			validateArgsLen(2, command);
			String server = command.args()[0];
			String username = command.args()[1];
			controller.moveUsernamePlayerToServer(server, username);
			return Optional.empty();
		}
		else if(command.action().equals("SET-DEF-ROLE")){
			validateArgsLen(1, command);
			UUID uuid = parseUUID(command.args()[0]);
			controller.setPlayerRoleToDefault(uuid);
			return Optional.empty();
		}
		else{
			throw new CommandException("unknown action: " + command.action());
		}
	}

	private void validateArgsLen(int len, Command command) throws CommandException{
		if(command.args().length != len){
			throw new CommandException("expected "+len+" arguments, but got "+command.args().length);
		}
	}

	private UUID parseUUID(String uuid) throws CommandException{
		try{
			return UUID.fromString(uuid);
		}catch(IllegalArgumentException e){
			throw new CommandException("uuid has invalid format", e);
		}
	}
}

