package to.joe.j2mc.chat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.chat.J2MC_Chat;
import to.joe.j2mc.core.command.MasterCommand;

public class ListMuteCommand extends MasterCommand {
	
	J2MC_Chat plugin;

    public ListMuteCommand(J2MC_Chat chat) {
        super(chat);
        this.plugin = chat;
    }
    
    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
    	StringBuilder s = new StringBuilder(ChatColor.RED + "Muted players: ");
    	for (String p : this.plugin.mutedPlayers) {
    		s.append(p + ", ");
    	}
    	s.setLength(s.length()-2);
    	sender.sendMessage(s.toString());
    }
}
