package to.joe.j2mc.chat;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;

public class J2MC_Chat extends JavaPlugin{

	private String message_format;

	@Override
	public void onDisable() {
		J2MC_Manager.getLog().info("Chat module disabled");
	}
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvent(Type.PLAYER_CHAT, new ChatListener(), Priority.Normal, this);
		J2MC_Manager.getLog().info("Chat module enabled");
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.message_format = this.getConfig().getString("message.format");
        J2MC_Manager.getLog().info("Using format '" + message_format + "'");
	}
	
	class ChatListener extends PlayerListener{
	    @Override
	    public void onPlayerChat(PlayerChatEvent event) {
	        if (event.isCancelled()) {
	            return;
	        }
	        String message = message_format;
	        message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");
	        event.setFormat(message);
	    }
	}
}
