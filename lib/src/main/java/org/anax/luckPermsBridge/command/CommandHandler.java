package org.anax.luckPermsBridge.command;

import java.util.UUID;

public class CommandHandler{
	IServerController controller;

	public CommandHandler(IServerController controller){
		this.controller = controller;
	}

	public void handle(Command command) throws BadCommandException{
		if(command.action().equals("SET-ROLE")){
			validateArgsLen(2, command);
			String role = command.args()[0];
			UUID uuid = parseUUID(command.args()[1]);
			controller.setPlayerRole(role, uuid);
		}
		else if(command.action().equals("ADD-ROLE")){
			validateArgsLen(2, command);
			String role = command.args()[0];
			UUID uuid = parseUUID(command.args()[1]);
			controller.addPlayerRole(role, uuid);
		}
		else if(command.action().equals("MOVE-PLAYER-UUID")){
			validateArgsLen(2, command);
			String server = command.args()[0];
			UUID uuid = parseUUID(command.args()[1]);
			controller.moveUUIDPlayerToServer(server, uuid);
		}
		else if(command.action().equals("MOVE-PLAYER-NAME")){
			validateArgsLen(2, command);
			String server = command.args()[0];
			String username = command.args()[1];
			controller.moveUsernamePlayerToServer(server, username);
		}
		else if(command.action().equals("SET-DEF-ROLE")){
			validateArgsLen(1, command);
			UUID uuid = parseUUID(command.args()[0]);
			controller.setPlayerRoleToDefault(uuid);
		}
		throw new BadCommandException("unknown action: " + command.action());
	}

	private void validateArgsLen(int len, Command command) throws BadCommandException{
		if(command.args().length != len){
			throw new BadCommandException("expected "+len+" arguments, but got "+command.args().length);
		}
	}

	private UUID parseUUID(String uuid) throws BadCommandException{
		try{
			return UUID.fromString(uuid);
		}catch(IllegalArgumentException e){
			throw new BadCommandException("uuid has invalid format", e);
		}
	}
}

