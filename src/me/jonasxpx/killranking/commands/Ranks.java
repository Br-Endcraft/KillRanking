package me.jonasxpx.killranking.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jonasxpx.killranking.KillRanking;
import me.jonasxpx.killranking.ManagerRanking;

public class Ranks implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		for(String ranks : KillRanking.ranks){
			sender.sendMessage("§0|- §6" + ManagerRanking.getMinFromStringRank(ranks) + " §2à §6" + ManagerRanking.getMaxFromStringRank(ranks) + " §2- " + ManagerRanking.getTagFromStringRank(ranks));
		}
		return false;
	}
	
}