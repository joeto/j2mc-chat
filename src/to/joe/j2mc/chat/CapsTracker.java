package to.joe.j2mc.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CapsTracker implements Runnable {

    private final J2MC_Chat plugin;
    private int trimTask = -1;
    private int infractions;
    private final String name;

    public CapsTracker(J2MC_Chat chat, String name) {
        this.plugin = chat;
        this.name = name;
    }

    public void infraction() {
        this.infractions++;
        if (this.plugin.getServer().getScheduler().isQueued(this.trimTask)) {
            this.plugin.getServer().getScheduler().cancelTask(this.trimTask);
        }
        this.trimTask = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 600);
    }

    public int getNumInfractions() {
        return this.infractions;
    }

    @Override
    public void run() {
        this.trimTask = -1;
        if (this.getNumInfractions() > 0) {
            this.infractions--;
            if (this.getNumInfractions() > 0) {
                this.trimTask = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 600);
            } else {
                final Player player = this.plugin.getServer().getPlayer(this.name);
                if (player != null) {
                    player.sendMessage(ChatColor.AQUA + "Your warning level on all-caps messages has decreased.");
                    player.sendMessage(ChatColor.AQUA + "Be more careful in the future!");
                }
            }
        }
    }

}
