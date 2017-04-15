package me.jonasxpx.killranking.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event{

	private static final HandlerList handler = new HandlerList();
	private Player player;
	public PlayerKillEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handler;
	}

	
}
