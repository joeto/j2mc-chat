package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;

public class MuteallCommand extends MasterCommand<J2MC_Chat> {
	
    public MuteallCommand(J2MC_Chat chat) {
        super(chat);
    }
    
    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
    	this.plugin.everbodyMuted = !this.plugin.everbodyMuted;
    	if (this.plugin.everbodyMuted) {
    		J2MC_Manager.getCore().adminAndLog(ChatColor.RED + sender.getName() + " has muted all players");
    		this.plugin.getServer().broadcastMessage(ChatColor.RED + "All players have been muted. You may still use /msg");
    	} else {
    		J2MC_Manager.getCore().adminAndLog(ChatColor.RED + sender.getName() + " has unmuted all players");
    		this.plugin.getServer().broadcastMessage(ChatColor.RED + "All players have been unmuted");
    	}
    }

}
