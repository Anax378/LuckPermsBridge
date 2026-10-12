package org.anax.luckPermsBridge.command;

import java.util.UUID;

public class TestServerController implements IServerController{

	@Override
	public void addPlayerRole(String role, UUID uuid) throws BadCommandException {
		System.out.println("*adding role "+role+" to player with uuid "+uuid.toString());
	}

	@Override
	public void moveUUIDPlayerToServer(String server, UUID uuid) throws BadCommandException {
		System.out.println("*moving player with uuid "+uuid.toString()+" to server "+server);
	}

	@Override
	public void moveUsernamePlayerToServer(String server, String username) throws BadCommandException {
		System.out.println("*moving player with username"+username+"to server "+server);
	}

	@Override
	public void removePlayerRole(String role, UUID uuid) throws BadCommandException {
		System.out.println("*removing role "+role+" from player with uuid "+uuid.toString());
	}

	@Override
	public void setPlayerRole(String role, UUID uuid) throws BadCommandException {
		System.out.println("*setting role "+role+" for player with uuid "+uuid.toString());
	}

	@Override
	public void setPlayerRoleToDefault(UUID uuid) throws BadCommandException {
		System.out.println("*setting role of player with uuid "+uuid.toString()+" to the default");
	}

}

