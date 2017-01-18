package me.jonasxpx.killranking;

public class ManagerRanking {

	
	/** [0] = RANK NME
	 *  [1] = MIN KILLS
	 *  [2] = MAX KILLS
	 *  [3] = TAG
	 *  
	 * **/
	
	public static int getMinFromStringRank(String string){
		return Integer.parseInt(string.split("-")[1]);
	}
	
	public static int getMaxFromStringRank(String string){
		return Integer.parseInt(string.split("-")[2]);
	}
	
	public static String getRankName(String string){
		return string.split("-")[0];
	}
	
	public static String getTagFromStringRank(String string){
		return string.split("-")[3];
	}
}
