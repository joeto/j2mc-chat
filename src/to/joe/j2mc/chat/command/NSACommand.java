package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;

public class NSACommand extends MasterCommand<J2MC_Chat>{
    
    public NSACommand (J2MC_Chat chat) {
        super(chat);
    }
    
    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (isPlayer) {
            String message;
            if (player.hasPermission("j2mc.chat.admin.nsa")) {
                message = ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " takes off headphones. That's enough chatter";
                J2MC_Manager.getPermissions().delFlag(player, 'N');
            } else {
                message = ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " puts on headphones. Intercepting...";
                J2MC_Manager.getPermissions().addFlag(player, 'N');
            }
            J2MC_Manager.getCore().adminAndLog(message);
        }
    }

}
