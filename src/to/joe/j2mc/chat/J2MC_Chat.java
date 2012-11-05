package to.joe.j2mc.chat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.chat.command.ListMuteCommand;
import to.joe.j2mc.chat.command.MeCommand;
import to.joe.j2mc.chat.command.MessageCommand;
import to.joe.j2mc.chat.command.MuteCommand;
import to.joe.j2mc.chat.command.MuteallCommand;
import to.joe.j2mc.chat.command.NSACommand;
import to.joe.j2mc.chat.command.ReplyCommand;
import to.joe.j2mc.chat.command.ShushCommand;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.event.MessageEvent;
import to.joe.j2mc.core.permissions.ThreadSafePermissionTracker;

public class J2MC_Chat extends JavaPlugin implements Listener {

    private String message_format;
    public String privatemessage_format;
    public HashSet<String> mutedPlayers;
    public volatile boolean everbodyMuted = false;
    public Map<String, String> lastMessage = new HashMap<String, String>();
    private Map<String, CapsTracker> infractions = new ConcurrentHashMap<String, CapsTracker>();
    private double capsThreshold;
    private ThreadSafePermissionTracker muteTracker;
    private ThreadSafePermissionTracker receiveTracker;
    private ThreadSafePermissionTracker overrideTracker;
    private ThreadSafePermissionTracker sendTracker;
    private ThreadSafePermissionTracker specTracker;
    private ThreadSafePermissionTracker capsTracker;
    private boolean redirectVanishChat;
    private boolean spectatorChat;
    private static final Pattern ALLCAPS = Pattern.compile("[A-Z]");

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
        this.getCommand("muteall").setExecutor(new MuteallCommand(this));
        this.getCommand("listmute").setExecutor(new ListMuteCommand(this));
        this.getCommand("reply").setExecutor(new ReplyCommand(this));
        this.getCommand("shush").setExecutor(new ShushCommand(this));

        J2MC_Manager.getPermissions().addFlagPermissionRelation("j2mc.chat.mute", 'M', true);
        J2MC_Manager.getPermissions().addFlagPermissionRelation("j2mc.chat.receive", 'S', false);
        J2MC_Manager.getPermissions().addFlagPermissionRelation("j2mc.chat.admin.nsa", 'N', true);

        this.muteTracker = new ThreadSafePermissionTracker(this, "j2mc.chat.mute");
        this.overrideTracker = new ThreadSafePermissionTracker(this, "j2mc.chat.admin.muteall.override");
        this.receiveTracker = new ThreadSafePermissionTracker(this, "j2mc.chat.receive");
        this.sendTracker = new ThreadSafePermissionTracker(this, "j2mc.chat.send");
        this.specTracker = new ThreadSafePermissionTracker(this, "j2mc.chat.spectator");
        this.capsTracker = new ThreadSafePermissionTracker(this, "j2mc.chat.capsexempt");

        if (this.getConfig().getBoolean("enableformatinjection")) {
            for (Player player : this.getServer().getOnlinePlayers()) {
                if (player != null) {
                    this.playerNameInitialize(player);
                }
            }
        }

        this.redirectVanishChat = getConfig().getBoolean("redirectvanishchat", true);
        this.spectatorChat = getConfig().getBoolean("spectatorchat", false);
        this.capsThreshold = this.getConfig().getDouble("capsthreshold", 0.9D);

        this.mutedPlayers = new HashSet<String>();
        this.getLogger().info("Chat module enabled");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (J2MC_Manager.getVisibility().isVanished(event.getPlayer()) && redirectVanishChat) {
            event.setCancelled(true);
            event.getPlayer().chat("/a " + event.getMessage());
        }
        if (!this.sendTracker.hasPermission(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if (event.getMessage().length() > 10 && !this.capsTracker.hasPermission(event.getPlayer())) {
            final Matcher matcher = J2MC_Chat.ALLCAPS.matcher(event.getMessage());
            int caps = 0;
            while (matcher.find()) {
                caps++;
            }
            if ((double) caps / event.getMessage().length() > capsThreshold) {
                if (!infractions.containsKey(event.getPlayer().getName())) {
                    infractions.put(event.getPlayer().getName(), new CapsTracker(this, event.getPlayer().getName()));
                }
                CapsTracker tracker = infractions.get(event.getPlayer().getName());
                tracker.infraction();
                if (tracker.getNumInfractions() == 1) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Please do not send messages in all capital letters. Warning 1/3");
                } else if (tracker.getNumInfractions() == 2 || tracker.getNumInfractions() == 3) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Do not send messages in all capital letters. Warning " + tracker.getNumInfractions() + "/3");
                    event.setMessage(event.getMessage().toLowerCase());
                } else if (tracker.getNumInfractions() > 3) {
                    event.getPlayer().kickPlayer(ChatColor.RED + "Do not send messages in all capital letters.");
                    J2MC_Manager.getCore().adminAndLog(ChatColor.RED + event.getPlayer().getName() + " has been kicked for a caps violation.");
                    event.setCancelled(true);
                    return;
                }
            }
        }
        if (event.isCancelled()) {
            return;
        }
        if (this.muteTracker.hasPermission(event.getPlayer()) || (everbodyMuted && !this.overrideTracker.hasPermission(event.getPlayer()))) {
            J2MC_Manager.getCore().adminAndLog(ChatColor.YELLOW + "[Mute Blocked] <" + event.getPlayer().getName() + "> " + ChatColor.WHITE + event.getMessage());
            event.getPlayer().sendMessage(ChatColor.RED + "You're currently muted");
            event.setCancelled(true);
            return;
        }
        if (this.getConfig().getBoolean("enableformatinjection")) {
            for (final Player plr : (new HashSet<Player>(event.getRecipients()))) {
                if (!this.receiveTracker.hasPermission(plr.getPlayer())) {
                    event.getRecipients().remove(plr);
                }
            }
            String message = this.message_format;
            message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");
            if (spectatorChat && this.specTracker.hasPermission(event.getPlayer())) {
                message = ChatColor.AQUA + "[SPEC]" + message;
                for (final Player plr : (new HashSet<Player>(event.getRecipients()))) {
                    if (!this.specTracker.hasPermission(plr)) {
                        event.getRecipients().remove(plr);
                    }
                }
            }
            event.setFormat(message);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (this.getConfig().getBoolean("enableformatinjection")) {
            this.playerNameInitialize(player);
        }
        if (mutedPlayers.contains(player.getName())) {
            J2MC_Manager.getPermissions().addFlag(player, 'M');
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
