package Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.mrsweeter.theFloorIsLava.TheFloorIsLava;

public class Messages {
	
	private static Map<String, String> MESSAGES = new HashMap<String, String>();
	
	public static void registerMessage(String id, String message){
		MESSAGES.put(id, message);
	}
	
	public static String getMessage(String id){
		return MESSAGES.get(id);
	}
	
	public static void loadMessages(ConfigurationSection section)	{
		if (section != null)	{
			for (String str : section.getKeys(false))	{
				registerMessage(str, section.getString(str));
			}
			TheFloorIsLava.log.info(ConsoleColor.YELLOW + "Loading of " + section.getName() + " complete" + ConsoleColor.RESET);
		}
	}
}
