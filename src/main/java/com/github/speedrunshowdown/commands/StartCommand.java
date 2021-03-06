package com.github.speedrunshowdown.commands;

import com.github.speedrunshowdown.SpeedrunShowdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    private SpeedrunShowdown plugin;

    public StartCommand(SpeedrunShowdown plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If plugin is running, give warning
        if (plugin.isRunning()) {
            sender.sendMessage(ChatColor.RED + "Game already running!");
        }
        // Else, schedule tasks to show titles to players
        else {
            sender.sendMessage(ChatColor.GREEN + "Countdown started!");
            sendStartingTimerTile(ChatColor.YELLOW + "Game starting soon...", false);

            int countdownTime = plugin.getConfig().getInt("countdown-time");
            for (int i = 0; i <= countdownTime; i++) {
                final int seconds = i;
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (seconds == 0) {
                            sendStartingTimerTile(ChatColor.GREEN + "Go!", true);
                            plugin.start();
                        }
                        else {
                            sendStartingTimerTile(ChatColor.YELLOW + "Starting in " + seconds + "...", false);
                        }
                    }
                }, 30L + (countdownTime - i) * 30L);
            }
        }

        return true;
    }

    public void sendStartingTimerTile(String subtitle, boolean higherDing) {
        float pitch = 1f;
        if (higherDing) {
            pitch = 2f;
        }
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, pitch);
            player.sendTitle(ChatColor.YELLOW + ChatColor.BOLD.toString() + "SPEEDRUN SHOWDOWN", subtitle, 0, 60, 10);
        }
    }
}
