package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

// Based on
// Logout400: https://www.spigotmc.org/threads/class-simple-custom-configs.51124/

/**
 * 
 * @author MrSweeter
 * @author Logout400
 *
 */
public class PluginConfiguration extends YamlConfiguration {
   
    private File file;
    private JavaPlugin plugin;
   
    /**
     * Creates new PluginFile, without defaults
     * @param plugin - Your plugin
     * @param fileName - Name of the file
     */
    public PluginConfiguration(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }
   
    /**
     * Creates new PluginFile, with defaults
     * @param plugin - Your plugin
     * @param fileName - Name of the file
     * @param folder - Name of the folder
     */
    public PluginConfiguration(JavaPlugin plugin, String fileName, String folder) {
        this.plugin = plugin;
        if (folder != null)	{
        	folder = "\\" + folder;
        	this.file = new File(plugin.getDataFolder() + folder, fileName);
        } else {
        	this.file = new File(plugin.getDataFolder(), fileName);
        }
        reload();
    }
    
   
    /**
     * Reload configuration
     */
    public void reload() {
       
    	createIfNotExists(file);
    	
    	try {
			load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			 plugin.getLogger().severe("File not found " + file.getName());
		} catch (IOException e) {
			e.printStackTrace();
			 plugin.getLogger().severe("Error while saving file " + file.getName());
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			 plugin.getLogger().severe("Invalid configuration exception for " + file.getName());
		}
    }
    
    /**
     * Create file if it not exist
     * @param file File to create
     */
    private void createIfNotExists(File file)	{
        if(!file.exists()){
            try{
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                InputStream inputStream = plugin.getResource(file.getName());
	            if (inputStream != null)	{
	            	Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	                inputStream.close();
	            } else {
	            	file.createNewFile();
	            }
            }catch(IOException exception){
            	exception.printStackTrace();
            	plugin.getLogger().severe("Error during creation of file " + file.getName());
            }
        }
    }
   
    /**
     * Save configuration
     */
    public void save() {
       
        try {
            options().indent(2);
            save(file);
           
        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while saving file " + file.getName());
        }
       
    }
   
}
