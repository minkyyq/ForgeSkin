package ru.minkyyq.forgeskin;

import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ForgeSkin extends JavaPlugin implements Listener, TabExecutor {

    private String defaultSkin;
    private String reloadMessage;

    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfigValues();
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("forgeskin").setExecutor(this);
        this.getCommand("forgeskin").setTabCompleter(this);
    }

    public void onDisable() {
        this.getLogger().info("forgeskin has been disabled.");
    }

    private void reloadConfigValues() {
        this.reloadConfig();
        this.defaultSkin = this.getConfig().getString("skin", "notch");
        this.reloadMessage = this.colorize(this.getConfig().getString("msg.reloaded", "&6Скины &7>> &fВы успешно перезагрузили плагин"));
    }

    private String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("forgeskin.bypass")) {
            try {
                String command = String.format("skin set %s %s", this.defaultSkin, player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } catch (Exception var4) {
                this.getLogger().warning("Failed to set skin for player " + player.getName());
            }

        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("forgeskin.admin")) {
                this.reloadConfigValues();
                sender.sendMessage(this.reloadMessage);
            } else {
                sender.sendMessage(ChatColor.RED + "У вас нет прав для использования этой команды.");
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Использование: /forgeskin reload");
            return false;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 1 ? Collections.singletonList("reload") : Collections.emptyList();
    }
}
