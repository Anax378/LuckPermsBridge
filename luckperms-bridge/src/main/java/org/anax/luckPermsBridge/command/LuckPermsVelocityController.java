package org.anax.luckPermsBridge.command;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.swing.text.html.Option;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;

public class LuckPermsVelocityController implements IServerController{
	LuckPerms lp;
	ProxyServer proxy;

	public LuckPermsVelocityController(LuckPerms lp, ProxyServer proxy){
		this.lp = lp;
		this.proxy = proxy;
	}

	public CompletableFuture<User> getUser(UUID uuid){
		return lp.getUserManager().loadUser(uuid);
	}

	@Override
	public void setPlayerRole(String role, UUID uuid) throws CommandException {
		getUser(uuid).thenAcceptAsync(user -> {
			user.data().clear(NodeType.INHERITANCE::matches);
			user.data().add(InheritanceNode.builder(role).build());
			lp.getUserManager().saveUser(user);
		});
	}

	@Override
	public void addPlayerRole(String role, UUID uuid) throws CommandException {
		getUser(uuid).thenAcceptAsync(user -> {
			user.data().add(InheritanceNode.builder(role).build());
			lp.getUserManager().saveUser(user);
		});
	}

	@Override
	public void removePlayerRole(String role, UUID uuid) throws CommandException {
		getUser(uuid).thenAcceptAsync(user -> {
			user.data().remove(InheritanceNode.builder(role).build());
			lp.getUserManager().saveUser(user);
		});
	}

	@Override
	public void setPlayerRoleToDefault(UUID uuid) throws CommandException {
		setPlayerRole("default", uuid);
	}

	@Override
	public void moveUUIDPlayerToServer(String server, UUID uuid) throws CommandException {
		Optional<Player> mplayer = proxy.getPlayer(uuid);
		if(mplayer.isEmpty()){
			throw new CommandException("player with uuid "+uuid.toString()+" is not online");
		}
		movePlayer(mplayer.get(), server);
	}

	@Override
	public void moveUsernamePlayerToServer(String server, String username) throws CommandException {
		Optional<Player> mplayer = proxy.getPlayer(username);
		if(mplayer.isEmpty()){
			throw new CommandException("player with username "+username+" is not online");
		}
		movePlayer(mplayer.get(), server);
	}

	private void movePlayer(Player player, String server) throws CommandException {
		Optional<RegisteredServer> ms = proxy.getServer(server);
		if(ms.isEmpty()){
			throw new CommandException("no server with name "+server+" is registered");
		}
		player.createConnectionRequest(ms.get()).connect();
	}

}

