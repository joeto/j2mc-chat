package to.joe.j2mc.chat;

import org.bukkit.ChatColor;

public class ChatFunctions {

    public ChatColor toColor(int input) {
        switch (input) {
            case 0:
                return ChatColor.BLACK;
            case 1:
                return ChatColor.DARK_BLUE;
            case 2:
                return ChatColor.DARK_GREEN;
            case 3:
                return ChatColor.DARK_AQUA;
            case 4:
                return ChatColor.DARK_RED;
            case 5:
                return ChatColor.DARK_PURPLE;
            case 6:
                return ChatColor.GOLD;
            case 7:
                return ChatColor.GRAY;
            case 8:
                return ChatColor.DARK_GRAY;
            case 9:
                return ChatColor.BLUE;
            case 10:
                return ChatColor.GREEN;
            case 11:
                return ChatColor.AQUA;
            case 12:
                return ChatColor.RED;
            case 13:
                return ChatColor.LIGHT_PURPLE;
            case 14:
                return ChatColor.YELLOW;
            case 15:
                return ChatColor.WHITE;
        }
        return null;
    }
    
    public String SubstituteColors(String input){
    	String output = null;
    	output = input.replace("*black*", ChatColor.BLACK.toString());
    	output = output.replace("*dblue*", ChatColor.DARK_BLUE.toString());
    	output = output.replace("*dgreen*", ChatColor.DARK_GREEN.toString());
    	output = output.replace("*darkaqua*", ChatColor.DARK_AQUA.toString());
    	output = output.replace("*dred*", ChatColor.DARK_RED.toString());
    	output = output.replace("*dpurple*", ChatColor.DARK_PURPLE.toString());
    	output = output.replace("*gold*", ChatColor.GOLD.toString());
    	output = output.replace("*gray*", ChatColor.GRAY.toString());
    	output = output.replace("*dgray*", ChatColor.DARK_GRAY.toString());
    	output = output.replace("*blue*", ChatColor.BLUE.toString());
    	output = output.replace("*green*", ChatColor.GREEN.toString());
    	output = output.replace("*aqua*", ChatColor.AQUA.toString());
    	output = output.replace("*red*", ChatColor.RED.toString());
    	output = output.replace("*lpurple*", ChatColor.LIGHT_PURPLE.toString());
    	output = output.replace("*yellow*", ChatColor.YELLOW.toString());
    	output = output.replace("*white*", ChatColor.WHITE.toString());
    	return output;
    }
	
}
