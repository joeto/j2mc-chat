package to.joe.j2mc.chat.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.J2MC_Core;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.event.MessageEvent;

public class MeCommand extends MasterCommand {

    J2MC_Chat plugin;

    public MeCommand(J2MC_Chat j2mc_chat) {
        super(j2mc_chat);
        this.plugin = j2mc_chat;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (isPlayer && (args.length > 0)) {
            final String message = J2MC_Core.combineSplit(0, args, " ");
            if (player.hasPermission("j2mc.chat.mute") || (this.plugin.everbodyMuted && !player.hasPermission("j2mc.chat.admin.muteall.override"))) {
            	J2MC_Manager.getCore().adminAndLog(ChatColor.YELLOW + "[Mute Blocked] *" + player.getName() + " " + message);
                return;
            }
            Bukkit.broadcast("*" + player.getDisplayName() + " " + message, "j2mc.chat.receive");
            this.plugin.getServer().getPluginManager().callEvent(new MessageEvent(MessageEvent.compile("GAMEMSG"), "*" + player.getName() + " " + message));
        }
    }

}
