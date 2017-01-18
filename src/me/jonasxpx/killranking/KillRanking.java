package me.jonasxpx.killranking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import me.jonasxpx.killranking.commands.Commands;
import me.jonasxpx.killranking.commands.Ranks;
import me.jonasxpx.killranking.commands.RemoveTag;

/**
 * 
 * @author JonasPC
 *
 */
public class KillRanking extends JavaPlugin{

	private static File fileData;
	public static ArrayList<String> ranks = new ArrayList<String>();
	public static ArrayList<String> disabledUsers = new ArrayList<>();
	protected static Map<String, ArrayList<String>> premiacao = new HashMap<String, ArrayList<String>>();
	protected static List<String> doubleHoras = Lists.newArrayList();
	public static FileConfiguration playerFile;
	protected static int delayTime;
	protected static boolean broadcast;
	protected static String broadcastMsg;
	protected static boolean doublekills = false;
	public static KillRanking instance;
	
	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getCommand("Kills").setExecutor(new Commands());
		getCommand("Ranks").setExecutor(new Ranks());
		getCommand("removertag").setExecutor(new RemoveTag());
		fileData = getDataFolder();
		playerFile = YamlConfiguration.loadConfiguration(simpleFileManager());
		loadRanks();
		saveConfig();
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
	}
	
	private void loadRanks(){
		for(String st : getConfig().getConfigurationSection("ranks").getKeys(false)){
			String rank = getConfig().getString("ranks."+st+".Nome") + "-"
					+ getConfig().getInt("ranks."+st+".MinKills") + "-"
					+ getConfig().getInt("ranks."+st+".MaxKills") + "-"
					+ getConfig().getString("ranks."+st+".Tag").replace('&', '§');
			System.out.println(rank);
			ranks.add(rank);
			if(getConfig().contains("ranks."+st+".Comandos"))
				premiacao.put(getConfig().getString("ranks."+st+".Nome"), Lists.newArrayList(getConfig().getStringList("ranks."+st+".Comandos")));
		}
		delayTime = getConfig().getInt("DelayAntFreeKill");
		broadcast = getConfig().getBoolean("AnunciarAoPassarRank");
		broadcastMsg = getConfig().getString("Anuncio").replace('&', '§');
		if(getConfig().getBoolean("Evento.DoubleKills")){
			doubleHoras.addAll(getConfig().getStringList("Evento.Horarios.DoubleKills"));
		}
		ManagerEvent.checkTime();
	}
	
	private static File simpleFileManager(){
		File f = new File(fileData + "/players.dat");
		try{
			if(!f.exists())
				f.createNewFile();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return f;
	}
	
	public static void savePlayerFile(){
		try{
			playerFile.save(simpleFileManager());
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
}
