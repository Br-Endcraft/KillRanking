package me.jonasxpx.killranking;

import java.util.Calendar;

import org.bukkit.scheduler.BukkitRunnable;

public class ManagerEvent {

	public static void checkTime(){
		new BukkitRunnable() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				for(String hor : KillRanking.doubleHoras){
					String[] formated = hor.split(";");
					if(cal.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(formated[0]) 
							&& cal.get(Calendar.MINUTE) == Integer.parseInt(formated[1])){
						KillRanking.doublekills = true;
						KillRanking.instance.getServer().broadcastMessage("§dEvento DoubleKill Iniciado, Cada Kill Vale por 2.");
						callTimeDown(60*30);
					}
				}
			}
		}.runTaskTimerAsynchronously(KillRanking.instance, 0L, 20*60);
	}
	private static void callTimeDown(int delay){
		new BukkitRunnable() {
			
			@Override
			public void run() {
				KillRanking.doublekills = false;
				KillRanking.instance.getServer().broadcastMessage("§dEvento DoubleKill finalizado.");
			}
		}.runTaskLaterAsynchronously(KillRanking.instance, 20*delay);
	}
}
