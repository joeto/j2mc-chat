package to.joe.j2mc.chat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;

public class J2MC_Chat extends JavaPlugin implements Listener {

    private String message_format;

    @Override
    public void onDisable() {
        this.getLogger().info("Chat module disabled");
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("Chat module enabled");
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.message_format = this.getConfig().getString("message.format");
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        String message = this.message_format;
        this.getLogger().info(message);
        message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");
        message = ChatFunctions.SubstituteColors(message);
        this.getLogger().info(message);
        event.setFormat(message);
    }
    
    @EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		try{
		PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT color FROM j2users WHERE name=?");
		ps.setString(1, player.getName());
		ResultSet rs = ps.executeQuery();
		rs.next();
		int playercolor = rs.getInt("color");
		ChatColor color = ChatFunctions.toColor(playercolor);
		player.setDisplayName(color.toString() + player.getName());
		}catch(SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
	
}
