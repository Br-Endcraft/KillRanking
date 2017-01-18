package me.jonasxpx.killranking.database;

import me.jonasxpx.killranking.KillRanking;

public class CacheManager {

	private String user;
	private int kills = -1;
	
	public CacheManager(String user){
		this.user = user;
		if(!KillRanking.instance.getXPDatabase().contains(user)){
			if(KillRanking.playerFile.contains(user.toLowerCase()))
				KillRanking.instance.getXPDatabase().register(user, KillRanking.playerFile.getInt(user.toLowerCase()));
			else
				KillRanking.instance.getXPDatabase().register(user);
		}
		kills = KillRanking.instance.getXPDatabase().get(user);
	}
	
	public void updateCache(int kills){
		this.kills = kills;
	}
	
	public int getKills(){
		return this.kills;
	}
	
	public void saveCache(){
		if(kills != -1){
			KillRanking.instance.getXPDatabase().update(user, kills);
		}
	}
}
