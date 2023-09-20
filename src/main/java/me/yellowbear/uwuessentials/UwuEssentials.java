package me.yellowbear.uwuessentials;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class UwuEssentials extends JavaPlugin implements Listener {
    FileConfiguration config = this.getConfig();
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new UwuEssentials(), this);
        MiniMessage msg = MiniMessage.miniMessage();
        config.addDefault("footer", "I love foot <tps>");
        config.addDefault("header", "I love head <tps>");
        config.addDefault("delay", 100);
        config.addDefault("frequency", 200);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,() -> {
            reloadGlobalPlayerList(msg);
        },config.getLong("delay"),config.getLong("frequency"));
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        reloadPlayerList(MiniMessage.miniMessage(), event.getPlayer());
    }

    public void reloadGlobalPlayerList(MiniMessage msg){
        for (Player player : getServer().getOnlinePlayers()) {
            reloadPlayerList(msg, player);
        };
    }
    public void reloadPlayerList(MiniMessage msg, Player player){
        Component footer = msg.deserialize(
                config.getString("footer"),
                Placeholder.component("tps", Component.text(Math.round(getServer().getTPS()[0]), NamedTextColor.LIGHT_PURPLE))
        );
        Component header = msg.deserialize(
                config.getString("header"),
                Placeholder.component("tps", Component.text(Math.round(getServer().getTPS()[0]), NamedTextColor.LIGHT_PURPLE))
        );
        player.sendPlayerListFooter(footer);
        player.sendPlayerListHeader(header);
    }
}
