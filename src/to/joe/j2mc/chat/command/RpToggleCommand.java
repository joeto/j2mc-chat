package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.command.MasterCommand;

public class RpToggleCommand extends MasterCommand<J2MC_Chat> {

    public RpToggleCommand(J2MC_Chat plugin) {
        super(plugin);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (!isPlayer) {
            sender.sendMessage("Silly console. Console can't ignore rp-chat.");
            return;
        }
        if (this.plugin.rolePlayers.contains(player.getName())) {
            this.plugin.rolePlayers.remove(player.getName());
            player.sendMessage(ChatColor.BLUE + "You are no longer in the RolePlay chat.");
        } else {
            this.plugin.rolePlayers.add(player.getName());
            player.sendMessage(ChatColor.BLUE + "You are now in the RolePlay chat.");
        }
    }

}
