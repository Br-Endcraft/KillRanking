package me.jonasxpx.killranking;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.jonasxpx.killranking.database.CacheManager;
import me.jonasxpx.killranking.event.PlayerLevelUpEvent;

public class Listeners implements Listener{

	@EventHandler
	public void playerSendMens(ChatMessageEvent ev){
		if(KillRanking.disabledUsers.contains(ev.getSender().getName().toLowerCase())){
			return;
		}
		if(ev.getSender().hasPermission("killranking.ignoretag") && !ev.getSender().isOp()){
			return;
		}
		for(String ranks : KillRanking.ranks){
			if(ManagerPlayer.getKills(ev.getSender().getName()) >= ManagerRanking.getMinFromStringRank(ranks) 
					&& ManagerPlayer.getKills(ev.getSender().getName()) <= ManagerRanking.getMaxFromStringRank(ranks)){
				ev.setTagValue("killranking", ManagerRanking.getTagFromStringRank(ranks));
				break;
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerLogin(PlayerJoinEvent e){
		KillRanking.cacheManager.put(e.getPlayer().getName().toLowerCase(), new CacheManager(e.getPlayer().getName().toLowerCase()));
	}

	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e){
		CacheManager cache = KillRanking.cacheManager.get(e.getPlayer().getName().toLowerCase());
		cache.saveCache();
		KillRanking.cacheManager.remove(e.getPlayer().getName().toLowerCase());
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e){
			if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
					if(KillRanking.delayTime != 0 && !ManagerPlayer.isInFreeTime(e.getEntity())){
						Player killer = e.getEntity().getKiller();
						Player player = e.getEntity();
						World world = killer.getWorld();
								ManagerPlayer.addPlayerToFreeTime(player, killer);
								if(KillRanking.doublekills){
									ManagerPlayer.addKills(killer.getName(), 1);
									ManagerPlayer.addKills(killer.getName(), 1);
								}else
									ManagerPlayer.addKills(killer.getName(), 1);
								if(KillRanking.broadcast && ManagerRanking.getMinFromStringRank(ManagerPlayer.getNextKillRankingByPlayer(killer.getName())) == ManagerPlayer.getKills(killer.getName())){
									String rank = ManagerRanking.getRankName(ManagerPlayer.getNextKillRankingByPlayer(killer.getName()));
									KillRanking.instance.getServer().getPluginManager().callEvent(new PlayerLevelUpEvent(killer, rank));
									world.strikeLightningEffect(killer.getLocation());
									Arrays.asList(Bukkit.getOnlinePlayers()).forEach(p -> p.sendMessage(KillRanking.broadcastMsg.replaceAll("%player", killer.getName()).replaceAll("%rank", rank)));
								}
								//Código do antigo Plugin - Funcional ;)
								killer.sendMessage(ChatColor.DARK_GREEN+"§m" + "-------------------------------");
								killer.sendMessage(ChatColor.GOLD +"[" + ChatColor.ITALIC + "§c"+ "PVP" + ChatColor.GOLD + "]" + "§a" +  "Matou"+"§6 " + e.getEntity().getName() );
								killer.sendMessage(ChatColor.DARK_GREEN +"§m" +  "-------------------------------");
					    		
								player.sendMessage(ChatColor.DARK_GREEN+"§m" + "-------------------------------");
								player.sendMessage(ChatColor.GOLD +"[" + ChatColor.ITALIC + "§c"+ "PVP" + ChatColor.GOLD + "]" + "§a" +  "Morto por"+"§c " 
										+ killer.getName());
								player.sendMessage(ChatColor.DARK_GREEN +"§m" +  "-------------------------------");
			}
		}
	}
	
	@EventHandler
	public void eventoPlayerUpRank(PlayerLevelUpEvent ev){
		if(KillRanking.premiacao.containsKey(ev.getRank())){
			for(String string : KillRanking.premiacao.get(ev.getRank())){
				KillRanking.instance.getServer().dispatchCommand(KillRanking.instance.getServer().getConsoleSender(),
						string.replaceAll("@player", ev.getPlayer().getName()));
			}
		}
	}
}
