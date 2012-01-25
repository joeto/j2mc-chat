package to.joe.j2mc.chat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
        final ChatColor PlayerNameColor = ChatColor.GOLD; //TODO: Replace with actual player's name colour.
        String message = this.message_format;
        message = message.replace("*playernamecolor*", PlayerNameColor.toString());
        this.getLogger().info(message);
        message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");
        message = ChatFunctions.SubstituteColors(message);
        this.getLogger().info(message);
        event.setFormat(message);
    }
}
