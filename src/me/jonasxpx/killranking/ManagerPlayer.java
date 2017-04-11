package me.jonasxpx.killranking;

import java.util.HashMap;
import java.util.Map;

import me.jonasxpx.killranking.database.CacheManager;
import me.jonasxpx.killranking.event.PlayerLevelUpEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ManagerPlayer {
	
	protected static Map<String, Long> freeTime = new HashMap<>();
	
	public static boolean isRegistred(String player){
		if(KillRanking.playerFile.contains(player.toLowerCase()))
			return true;
		else
			return false;
	}
	
	public static void forceRegister(String player){
		KillRanking.playerFile.set(player.toLowerCase(), 1);
		KillRanking.savePlayerFile();
	}
	
	/**
	 * Retorna a quantidades de Kills de um jogador.
	 * @param player
	 * @return retorna kills do jogador, returna -1 caso não exista.
	 */
	public static int getKills(String player){
		if(KillRanking.instance.getXPDatabase() == null){
			if(isRegistred(player)){
				return KillRanking.playerFile.getInt(player.toLowerCase());
			}
		} else {
			CacheManager cache = KillRanking.cacheManager.get(player.toLowerCase());
			return cache.getKills();
		}
		return -1;
	}

	/**
	 * Adiciona kills a conta do jogador, o valor é incrementado com a quantidade de
	 * kills que o jogador já tem.
	 * @param player 
	 * @param count Sempre maior que 0
	 */
	public static void addKills(String player, int count){
		if(KillRanking.instance.getXPDatabase() == null){
			if(count > 0){
				if(isRegistred(player))
					KillRanking.playerFile.set(player.toLowerCase(), getKills(player)+count);
				else
					forceRegister(player);
				KillRanking.savePlayerFile();
			}
		} else {
			CacheManager cache = KillRanking.cacheManager.get(player.toLowerCase());
			cache.updateCache(getKills(player.toLowerCase()) + count);
		}
	}
	
	/**
	 * Remove Kills de uma conta, a quantidade informa é removida da quantidade que o jogador já contem.
	 * Ex. Jogador tem 20 Kills, removo 5, retorno 15.
	 * @param player
	 * @param count
	 */
	public static void remKills(String player, int count){
		if(KillRanking.instance.getXPDatabase() == null){
			if(isRegistred(player))
				KillRanking.playerFile.set(player.toLowerCase(), getKills(player)-count);
			KillRanking.savePlayerFile();
		} else {
			CacheManager cache = KillRanking.cacheManager.get(player.toLowerCase());
			cache.updateCache(getKills(player.toLowerCase()) - count);
		}
	}
	
	/**
	 * Adiciona o jogador a o anti-freeKills, empede que o jogador mate o mesmo jogador, e assim as kills não será contabilizado.
	 * @param player
	 * @param deatPlayer
	 */
	public static void addPlayerToFreeTime(Player player, Player deatPlayer){
		freeTime.put(player.getName(), (System.currentTimeMillis() / 1000 + KillRanking.delayTime 
				+ (player.getAddress().getAddress().getHostAddress().equalsIgnoreCase(deatPlayer.getAddress().getAddress().getHostAddress()) ? 240 : 0)));
	}
	
	
	public static boolean isInFreeTime(Player player){
		if(freeTime.containsKey(player.getName())){
			if(freeTime.get(player.getName()) >= System.currentTimeMillis() / 1000){
				return true;
			}else
				freeTime.remove(player.getName());
		}
		return false;
	}
	
	public static String getRankingByPlayer(String player){
		String string = "";
		for(String ranks : KillRanking.ranks){
			if(ManagerPlayer.getKills(player) >= ManagerRanking.getMinFromStringRank(ranks) 
					&& ManagerPlayer.getKills(player) <= ManagerRanking.getMaxFromStringRank(ranks)){
				string = ranks;
			}
		}
		return string;
	}
	
	public static String getNextKillRankingByPlayer(String player){
		for(String ranks : KillRanking.ranks){
			if(ManagerPlayer.getKills(player)+1 >= ManagerRanking.getMinFromStringRank(ranks) 
					&& ManagerPlayer.getKills(player)+1 <= ManagerRanking.getMaxFromStringRank(ranks)){
				return ranks;
			}
		}
		return null;
	}
	
	public static int getRemainderForNextRank(String player){
		return ManagerRanking.getMaxFromStringRank(getRankingByPlayer(player)) - getKills(player);
	}
	
	public static void nextRanking(String player){
		addKills(player, getRemainderForNextRank(player) + 1);
		String rank = ManagerRanking.getRankName(getNextKillRankingByPlayer(player));
		KillRanking.instance.getServer().getPluginManager().callEvent(new PlayerLevelUpEvent(Bukkit.getPlayerExact(player), rank));
		Bukkit.getPlayerExact(player).getWorld().strikeLightningEffect(Bukkit.getPlayerExact(player).getLocation());
		for(Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(KillRanking.broadcastMsg.replaceAll("%player", player).replaceAll("%rank", rank));
	}
}
