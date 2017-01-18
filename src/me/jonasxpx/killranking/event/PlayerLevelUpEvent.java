package me.jonasxpx.killranking.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelUpEvent extends Event {
	
	private static final HandlerList handler = new HandlerList();
	private final Player player;
	private final String rank;
	
	public PlayerLevelUpEvent(Player player, String rankName) {
		this.player = player;
		this.rank = rankName;
	}

	@Override
	public HandlerList getHandlers() {
		return handler;
	}

	public static HandlerList getHandlerList() {
	    return handler;
	}
	
	public String getRank(){
		return rank;
	}
	
	public Player getPlayer() {
		return player;
	}

}
