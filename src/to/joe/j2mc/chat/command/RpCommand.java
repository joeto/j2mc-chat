package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.ChatFunctions;
import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.J2MC_Core;
import to.joe.j2mc.core.command.MasterCommand;

public class RpCommand extends MasterCommand<J2MC_Chat> {

    public RpCommand(J2MC_Chat plugin) {
        super(plugin);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (!isPlayer) {
            sender.sendMessage("Silly you. Seniors doesn't RolePlay.");
            return;
        }
        if (!this.plugin.rolePlayers.contains(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "You are not in the RolePlay chat. " + ChatColor.DARK_PURPLE + "/rptoggle" + ChatColor.RED + " to join.");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/rp <message>");
            return;
        }
        
        final String chat = J2MC_Core.combineSplit(0, args, " ");
        String message = this.plugin.getConfig().getString("message.format");
        message = ChatColor.BLUE + "[RP] " + ChatColor.WHITE + message;
        message = message.replace("%message", chat);
        message = message.replace("%displayname", player.getDisplayName());
        message = ChatFunctions.SubstituteColors(message);
        for (String name : this.plugin.rolePlayers) {
            Player p = this.plugin.getServer().getPlayer(name);
            p.sendMessage(message);
        }
        this.plugin.getLogger().info(message);
    }
}
