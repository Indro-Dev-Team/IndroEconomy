package io.github.indrodevteam.indroEconomy;

import io.github.indrodevteam.indroEconomy.commands.economy.*;
import io.github.indrodevteam.indroEconomy.events.*;
import io.github.indrodevteam.indroEconomy.objects.EconomyStorageUtil;
import io.github.indrodevteam.indroEconomy.tasks.TaskUpdateFile;
import io.github.indrodevteam.indroEconomy.utils.ConfigManager;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class IndroEconomy extends JavaPlugin {
    private static IndroEconomy plugin;

    @Override
    public void onEnable() {
        // Set up the MenuManager
        MenuManager.setup(getServer(), this);
        plugin = this;

        ConfigManager messagesConfigManager = new ConfigManager("config.yml", true);
        messagesConfigManager.saveResource("config.yml", this.getDataFolder() + File.separator + messagesConfigManager.getResourceName(), false);

        registerCommands();
        applyBukkitTasks();

        // load accounts from the accountProgram
        try {
            EconomyStorageUtil.loadAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);
        new ConfigManager("config.yml", false).save(true);
        try {
            EconomyStorageUtil.saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // an instance of the JavaPlugin program

    public static IndroEconomy getInstance() {
        return plugin;
    }

    private void registerCommands() {
        // commands
        PluginManager pm = getServer().getPluginManager();

        try {
            CommandManager.createCoreCommand(this, "eco",
                    "The Economy Module of the Plugin.", "/eco",
                    null,
                    CommandBal.class, CommandOpShop.class, CommandSend.class, CommandSetMoney.class,
                    CommandTransfer.class, CommandInfo.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //register events
        pm.registerEvents(new EventOnPlayerJoinLeave(), this);
        pm.registerEvents(new EventOnPlayerDeath(), this);
        pm.registerEvents(new EventOnPlayerMine(), this);
        pm.registerEvents(new EventOnEntityKill(), this);
        pm.registerEvents(new EventOnLootBox(), this);
        pm.registerEvents(new EventOnExperience(), this);
    }

    /*
    private void registerIntegrations() {
        // checks if vault exists
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServer().getServicesManager().register(Economy.class, new EconomyImplementer(), this, ServicePriority.Highest);
            this.getLogger().info("Vault Found, integrating with it.");
        } else {
            this.getLogger().info("Vault could not be found, disabling Vault Integration.");
        }
    }
     */

    private void applyBukkitTasks() {
        new TaskUpdateFile(this).runTaskTimer(this, 20L, 6000L);
    }
}
