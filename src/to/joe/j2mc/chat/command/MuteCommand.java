package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.exceptions.BadPlayerMatchException;

public class MuteCommand extends MasterCommand {

    J2MC_Chat plugin;

    public MuteCommand(J2MC_Chat chat) {
        super(chat);
        this.plugin = chat;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (args.length == 1) {
            final String targetName = args[0];
            Player target = null;
            try {
                target = J2MC_Manager.getVisibility().getPlayer(targetName, sender);
            } catch (final BadPlayerMatchException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
                return;
            }
            String n = target.getName();
            if (this.plugin.mutedPlayers.contains(n)) {
                this.plugin.mutedPlayers.remove(n);
                J2MC_Manager.getCore().adminAndLog(ChatColor.RED + sender.getName() + " unmuted " + n);
            } else {
                this.plugin.mutedPlayers.add(n);
                J2MC_Manager.getCore().adminAndLog(ChatColor.RED + sender.getName() + " muted " + n);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /mute <playername>");
        }
    }
}
