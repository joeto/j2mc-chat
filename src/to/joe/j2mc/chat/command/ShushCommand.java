package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;

public class ShushCommand extends MasterCommand {
	
	J2MC_Chat plugin;

    public ShushCommand(J2MC_Chat chat) {
        super(chat);
        this.plugin = chat;
    }
    
    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
    	if (isPlayer) {
    		if (J2MC_Manager.getPermissions().hasFlag(player.getName(), 'S')) {
    			J2MC_Manager.getPermissions().delFlag(player, 'S');
    			J2MC_Manager.getCore().adminAndLog(ChatColor.DARK_AQUA + player.getName() + " can now hear you again");
    		} else {
    			J2MC_Manager.getPermissions().addFlag(player, 'S');
    			J2MC_Manager.getCore().adminAndLog(ChatColor.DARK_AQUA + player.getName() + " has fingers to ears and is singing");
    		}
    	}
    }
}
