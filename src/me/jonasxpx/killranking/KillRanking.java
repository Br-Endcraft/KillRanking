package me.jonasxpx.killranking;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import me.jonasxpx.killranking.commands.Commands;
import me.jonasxpx.killranking.commands.Ranks;
import me.jonasxpx.killranking.commands.RemoveTag;
import me.jonasxpx.killranking.database.CacheManager;
import me.jonasxpx.killranking.database.DBManager;

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
	public static HashMap<String, CacheManager> cacheManager = new HashMap<>();
	public static FileConfiguration playerFile;
	protected static int delayTime;
	protected static boolean broadcast;
	protected static String broadcastMsg;
	protected static boolean doublekills = false;
	public static KillRanking instance;
	private DBManager db;
	
	
	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getCommand("Kills").setExecutor(new Commands());
		getCommand("Ranks").setExecutor(new Ranks());
		getCommand("removertag").setExecutor(new RemoveTag());
		fileData = getDataFolder();
		playerFile = YamlConfiguration.loadConfiguration(simpleFileManager());
		loadConfig();
		saveConfig();
	}
	
	@Override
	public void onDisable() {
		if(db != null){
			cacheManager.forEach((s,c) -> c.saveCache());
		}
		HandlerList.unregisterAll(this);
	}
	
	public DBManager getXPDatabase(){
		return this.db;
	}
	
	public void loadConfig(){
		try{
			if(!(new File(getDataFolder(), "config.yml").exists())){
				getConfig().options().copyDefaults(true);
				saveConfig();
			}
			if(!getConfig().contains("enable_mysql"))
				getConfig().set("enable_mysql", false);
			
			if(getConfig().getBoolean("enable_mysql")){
				
				db = new DBManager(
						getConfig().getString("mysql.host"),
						getConfig().getString("mysql.username"),
						getConfig().getString("mysql.database"), 
						getConfig().getString("mysql.password"));
				
				if(!cacheManager.isEmpty()){
					cacheManager.forEach((s,c) -> c.saveCache());
				}
				Arrays.asList(getServer().getOnlinePlayers()).forEach(player -> 
					cacheManager.put(player.getName().toLowerCase(), new CacheManager(player.getName().toLowerCase()))
				);
			}else
				db = null;
		}catch(SQLException e){
			if(e.getMessage().contains("Access denied for user")){
				e.printStackTrace();
				getLogger().log(Level.SEVERE, "Acesso negado ao usuário informado. | Ativando FlatFile");
			} else if (e.getMessage().contains("Communications link failure")){
				e.printStackTrace();
				getLogger().log(Level.SEVERE, "Falha ao se conectar ao servidor de banco de dados. | Ativando FlatFile");
			}
			e.printStackTrace();
		} finally {
			loadRanks();
		}
	}
	
	public void loadRanks(){
		if(db == null)
			getLogger().log(Level.INFO, "FlatFile enabled.");
		else
			getLogger().log(Level.INFO, "MySQL enabled");
		ranks.clear();
		premiacao.clear();
		doubleHoras.clear();
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
