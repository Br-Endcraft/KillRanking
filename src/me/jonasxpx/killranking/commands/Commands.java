package me.jonasxpx.killranking.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jonasxpx.killranking.KillRanking;
import me.jonasxpx.killranking.ManagerPlayer;
import me.jonasxpx.killranking.ManagerRanking;
import me.jonasxpx.killranking.database.CacheManager;

public class Commands implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		try{
			if(args.length == 0){
				if(!(sender instanceof Player)){return true;}
				String player = sender.getName();
				if(!ManagerPlayer.isRegistred(player) && KillRanking.instance.getXPDatabase() == null){sender.sendMessage("§cVocê não matou ninguem"); return true;}
				
				String rank = ManagerPlayer.getRankingByPlayer(player);
				int max = ManagerRanking.getMaxFromStringRank(rank);
				int kills = ManagerPlayer.getKills(player);
				sender.sendMessage("§b§m------------------------------------------\n");
				sender.sendMessage("§b|");
				sender.sendMessage("§b| Você tem §f[§a§l"+kills+"§f]§a Kills");
				sender.sendMessage("§b| §cProgresso: §0[§a"+kills+"§0/§a"+max+"§0]");
				sender.sendMessage("§b| Veja todos os ranks com /ranks");
				sender.sendMessage("§b|");
				sender.sendMessage("§b§m------------------------------------------");
			}
			if(args.length >= 1){
				if(sender.isOp()){
					if(args.length == 3){
						if(args[0].equalsIgnoreCase("add")){
								ManagerPlayer.addKills(args[1], Integer.parseInt(args[2]));
								sender.sendMessage("§c Kills adicionado");
								return true;
						}
						if(args[0].equalsIgnoreCase("rem")){
							ManagerPlayer.remKills(args[1], Integer.parseInt(args[2]));
							sender.sendMessage("§c Kills removido");
							return true;
						}
					}
					if(args.length == 2){
						if(args[0].equalsIgnoreCase("next")){
							ManagerPlayer.nextRanking(args[1]);
							return true;
						}
					}
					sender.sendMessage("§7 /kills <add,rem> <player> <integer>");
					if(args[0].equalsIgnoreCase("help")){
						sender.sendMessage("§6¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨\n"
								+ "§c- /kills add <User> <Kills[int]>\n"
								+ "§c- /kills rem <User> <Kills[int]>\n"
								+ "§c- /kills next <User> - §fAvançar 1 Rank.");
						return true;
					}
					if(args[0].equalsIgnoreCase("reload")){
						KillRanking.instance.reloadConfig();
						KillRanking.instance.loadConfig();
						KillRanking.instance.saveConfig();
						sender.sendMessage("§cRechargeable setup");
						return true;
					}
				}
				if(!sender.isOp() && Bukkit.getPlayer(args[0]) == null){
					sender.sendMessage("§cNão é possível checar Kills de jogadores offline");
					return true;
				}
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
				if(!ManagerPlayer.isRegistred(player.getName()) && KillRanking.instance.getXPDatabase() == null){
					sender.sendMessage("§cEle não matou ninguem");
					return true;
				}
				if(Bukkit.getPlayer(args[0]) == null)
					KillRanking.cacheManager.put(player.getName().toLowerCase(), new CacheManager(player.getName().toLowerCase()));
				String rank = ManagerPlayer.getRankingByPlayer(player.getName());
				int max = ManagerRanking.getMaxFromStringRank(rank);
				int kills = ManagerPlayer.getKills(player.getName());
				sender.sendMessage("§0|  tem §f[§2§l"+kills+"§f]§a Kills");
				sender.sendMessage("§cProgresso: §0[§b"+kills+"§0/§b"+max+"§0]");
				sender.sendMessage("§b§m------------------------------------------\n");
				sender.sendMessage("§b|");
				sender.sendMessage("§b| "+player.getName()+" tem §f[§a§l"+kills+"§f]§a Kills");
				sender.sendMessage("§b| §cProgresso: §0[§a"+kills+"§0/§a"+max+"§0]");
				sender.sendMessage("§b| Veja todos os ranks com §f/ranks");
				sender.sendMessage("§b|");
				sender.sendMessage("§b§m------------------------------------------");
				if(Bukkit.getPlayer(args[0]) == null)
					KillRanking.cacheManager.remove(player.getName().toLowerCase());
			}
		}catch(Exception e){
			sender.sendMessage("§cErro, informe em http://bit.ly/killranking-is");
			e.printStackTrace();
		}
		return false;
	}
}
