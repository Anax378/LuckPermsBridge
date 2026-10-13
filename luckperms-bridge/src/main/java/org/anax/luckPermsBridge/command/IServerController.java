package org.anax.luckPermsBridge.command;

import java.util.UUID;

public interface IServerController {
	public void setPlayerRole(String role, UUID uuid) throws CommandException;
	public void addPlayerRole(String role, UUID uuid) throws CommandException;
	public void removePlayerRole(String role, UUID uuid) throws CommandException;
	public void setPlayerRoleToDefault(UUID uuid) throws CommandException;
	public void moveUUIDPlayerToServer(String server, UUID uuid) throws CommandException;
	public void moveUsernamePlayerToServer(String server, String username) throws CommandException;
}


