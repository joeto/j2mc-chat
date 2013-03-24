package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.J2MC_Core;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.exceptions.BadPlayerMatchException;
import to.joe.j2mc.core.log.LogColors;

public class MessageCommand extends MasterCommand<J2MC_Chat> {

    public MessageCommand(J2MC_Chat j2mc_chat) {
        super(j2mc_chat);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (isPlayer) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Correct usage: /msg player message");
                return;
            }
            Player to = null;
            boolean adminAvailable = false;
            try {
                to = J2MC_Manager.getVisibility().getPlayer(args[0], player);
            } catch (final BadPlayerMatchException e) {
                if (args[0].equalsIgnoreCase("admin") && player.hasPermission("j2mc.chat.admin.msg")) {
                    player.sendMessage(ChatColor.RED + "You're an admin, you don't need to message yourself!");
                    return;
                }
                if (args[0].equalsIgnoreCase("admin")) {
                    for (final Player plr : this.plugin.getServer().getOnlinePlayers()) {
                        if (plr.hasPermission("j2mc.chat.admin.msg")) {
                            if (!J2MC_Manager.getVisibility().isVanished(plr)) {
                                adminAvailable = true;
                                break;
                            }
                        }
                    }
                    if (!adminAvailable) {
                        player.sendMessage(ChatColor.RED + "There are no admins online. Please use /report <issue> instead.");
                        return;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + e.getMessage());
                    return;
                }
            }
            if (player.equals(to)) {
                player.sendMessage(ChatColor.RED + "I think you're lonely.");
                return;
            }
            final String message = J2MC_Core.combineSplit(1, args, " ");
            String finalmessage = this.plugin.privatemessage_format;
            finalmessage = finalmessage.replace("%from", player.getDisplayName());
            finalmessage = finalmessage.replace("%to", (to != null ? to.getDisplayName() : "ADMIN"));
            finalmessage = finalmessage.replace("%message", message);
            final String nsamessage = ChatColor.DARK_AQUA + "[NSA] " + finalmessage;
            for (Player plr : this.plugin.getServer().getOnlinePlayers()) {
                if (plr.hasPermission("j2mc.chat.admin.nsa")) {
                    if (!plr.equals(player) && !plr.equals(to)) {
                        plr.sendMessage(nsamessage);
                    }
                }
            }
            player.sendMessage(finalmessage);
            if (to == null) {
                finalmessage = ChatColor.DARK_AQUA + "[AMSG] " + finalmessage;
                for (final Player plr : this.plugin.getServer().getOnlinePlayers()) {
                    if (plr.hasPermission("j2mc.chat.admin.msg")) {
                        plr.sendMessage(finalmessage);
                    }
                }
            } else {
                to.sendMessage(finalmessage);
                this.plugin.lastMessage.put(to.getName(), player.getName());
            }
            this.plugin.getLogger().info(LogColors.process(finalmessage));
        }
    }

}
