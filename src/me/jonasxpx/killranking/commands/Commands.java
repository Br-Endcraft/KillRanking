package me.jonasxpx.killranking.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jonasxpx.killranking.ManagerPlayer;

public class Commands implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		try{
			if(args.length == 0){
				if(!(sender instanceof Player)){return true;}
				String player = sender.getName();
				if(!ManagerPlayer.isRegistred(player)){sender.sendMessage("§cVocê não matou ninguem"); return true;}
				sender.sendMessage("§0| §eVocê tem §f[§2§l"+ManagerPlayer.getKills(player)+"§f]§a Kills\n"
						+ "§0| §f[§2§l" + ManagerPlayer.getRemainderForNextRank(player) + "§f] §aKills §ePara o próximo Rank");
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
					}
				}
				if(!sender.isOp() && Bukkit.getPlayer(args[0]) == null){
					sender.sendMessage("§cNão é possível checar Kills de jogadores offline");
					return true;
				}
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
				if(!ManagerPlayer.isRegistred(player.getName())){
					sender.sendMessage("§cEle não matou ninguem");
					return true;
				}
				sender.sendMessage("§0| §e"+player.getName()+" tem §f[§2§l"+ManagerPlayer.getKills(player.getName())+"§f]§a Kills\n"
						+ "§0| §f[§2§l" + ManagerPlayer.getRemainderForNextRank(player.getName()) + "§f] §aKills §ePara o próximo Rank");
			}
		}catch(Exception e){
			sender.sendMessage("§cErro, informe em http://bit.ly/killranking-is");
			e.printStackTrace();
		}
		return false;
	}
	
	
}
