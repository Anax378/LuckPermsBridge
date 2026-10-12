package org.anax.luckPermsBridge.command;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;

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
		getUser(uuid).thenAccept(user -> {
			user.data().clear(NodeType.INHERITANCE::matches);
			user.data().add(Node.builder(role).build());
			lp.getUserManager().saveUser(user);
		});
	}

	@Override
	public void addPlayerRole(String role, UUID uuid) throws CommandException {
		getUser(uuid).thenAccept(user -> {
			user.data().add(Node.builder(role).build());
			lp.getUserManager().saveUser(user);
		});
	}

	@Override
	public void removePlayerRole(String role, UUID uuid) throws CommandException {
		getUser(uuid).thenAccept(user -> {
			user.data().remove(Node.builder(role).build());
			lp.getUserManager().saveUser(user);
		});
	}

	@Override
	public void setPlayerRoleToDefault(UUID uuid) throws CommandException {
		setPlayerRole("deffault", uuid);
	}

	@Override
	public void moveUUIDPlayerToServer(String server, UUID uuid) throws CommandException {
		Optional<Player> mplayer = proxy.getPlayer(uuid);
		if(mplayer.isEmpty()){
			throw new CommandException("player with uuid "+uuid.toString()+" is not online");
		}
		for(RegisteredServer rs : proxy.getAllServers()){
			if(rs.getServerInfo().getName().equals(server)){
				mplayer.get().transferToHost(rs.getServerInfo().getAddress());
			}
		}
		throw new CommandException("no server with name"+server+" is registered");
	}

	@Override
	public void moveUsernamePlayerToServer(String server, String username) throws CommandException {
		throw new UnsupportedOperationException("Unimplemented method 'moveUsernamePlayerToServer'");
	}
}

