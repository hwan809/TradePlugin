package me.trade.vivace;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand(TradeCommand.tradeCommand).setExecutor(new TradeCommand());
        getServer().getPluginManager().registerEvents(new TradeInventoryHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
