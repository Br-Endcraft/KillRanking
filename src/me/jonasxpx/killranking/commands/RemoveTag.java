package me.jonasxpx.killranking.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jonasxpx.killranking.KillRanking;

public class RemoveTag implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] label) {
		if(KillRanking.disabledUsers.contains(sender.getName().toLowerCase())){
			KillRanking.disabledUsers.remove(sender.getName().toLowerCase());
			sender.sendMessage("§aSua TAG foi ativada;");
		} else {
			KillRanking.disabledUsers.add(sender.getName().toLowerCase());
			sender.sendMessage("§cSua TAG foi desativada.");
		}
		return false;
	}
}
