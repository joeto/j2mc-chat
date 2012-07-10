package to.joe.j2mc.chat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.chat.command.MeCommand;
import to.joe.j2mc.chat.command.MessageCommand;
import to.joe.j2mc.chat.command.MuteCommand;
import to.joe.j2mc.chat.command.NSACommand;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.event.MessageEvent;

public class J2MC_Chat extends JavaPlugin implements Listener {

    private String message_format;
    public String privatemessage_format;
    public HashSet<String> mutedPlayers;

    @Override
    public void onDisable() {
        this.getLogger().info("Chat module disabled");
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.message_format = ChatFunctions.SubstituteColors(this.getConfig().getString("message.format"));
        this.privatemessage_format = ChatFunctions.SubstituteColors(this.getConfig().getString("privatemessage.format"));
        this.getCommand("me").setExecutor(new MeCommand(this));
        this.getCommand("msg").setExecutor(new MessageCommand(this));
        this.getCommand("nsa").setExecutor(new NSACommand(this));
        this.getCommand("mute").setExecutor(new MuteCommand(this));
        if (this.getConfig().getBoolean("enableformatinjection")) {
            for (Player player : this.getServer().getOnlinePlayers()) {
                if (player != null) {
                    this.playerNameInitialize(player);
                }
            }
        }
        this.mutedPlayers = new HashSet<String>();
        this.getLogger().info("Chat module enabled");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPlayer().hasPermission("j2mc.chat.mute") || mutedPlayers.contains(event.getPlayer().getName())) {
            J2MC_Manager.getCore().adminAndLog(ChatColor.YELLOW + "[Mute Blocked] <" + event.getPlayer().getName() + ">" + ChatColor.WHITE + event.getMessage());
            event.setCancelled(true);
            return;
        }
        if (this.getConfig().getBoolean("enableformatinjection")) {
            for (final Player plr : (new HashSet<Player>(event.getRecipients()))) {
                if (!plr.hasPermission("j2mc.chat.recieve")) {
                    event.getRecipients().remove(plr);
                }
            }
            String message = this.message_format;
            message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");
            event.setFormat(message);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (this.getConfig().getBoolean("enableformatinjection")) {
            this.playerNameInitialize(player);
        }
    }

    @EventHandler
    public void onIRCMessageEvent(MessageEvent event) {
        if (this.getConfig().getBoolean("enableformatinjection")) {
            if (event.targetting("RESTORECOLOUR")) {
                this.playerNameInitialize(this.getServer().getPlayer(event.getMessage()));
            }
        }
    }

    public void playerNameInitialize(Player player) {
        try {
            final PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT color FROM users WHERE name=?");
            ps.setString(1, player.getName());
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                final int playercolor = rs.getInt("color");
                final ChatColor color = ChatFunctions.toColor(playercolor);
                player.setDisplayName(color.toString() + player.getName());
            } else {
                player.setDisplayName(ChatColor.GREEN + player.getName());
            }
        } catch (final Exception e) {
            e.printStackTrace();
            player.setDisplayName(ChatColor.GREEN + player.getName());
        }
    }

}
