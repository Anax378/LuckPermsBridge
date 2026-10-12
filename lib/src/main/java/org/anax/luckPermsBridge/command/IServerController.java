package org.anax.luckPermsBridge.command;

import java.util.UUID;

public interface IServerController {
	public void setPlayerRole(String role, UUID uuid) throws BadCommandException;
	public void addPlayerRole(String role, UUID uuid) throws BadCommandException;
	public void removePlayerRole(String role, UUID uuid) throws BadCommandException;
	public void setPlayerRoleToDefault(UUID uuid) throws BadCommandException;
	public void moveUUIDPlayerToServer(String server, UUID uuid) throws BadCommandException;
	public void moveUsernamePlayerToServer(String server, String username) throws BadCommandException;
}


