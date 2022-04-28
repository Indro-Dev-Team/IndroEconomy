package io.github.indrodevteam.indroEconomy;

import io.github.indrodevteam.indroEconomy.commands.accounts.*;
import io.github.indrodevteam.indroEconomy.commands.admin.CommandCheckAccounts;
import io.github.indrodevteam.indroEconomy.commands.admin.CommandSetMoney;
import io.github.indrodevteam.indroEconomy.config.ConfigManager;
import io.github.indrodevteam.indroEconomy.datamanager.BankDataUtils;
import io.github.indrodevteam.indroEconomy.datamanager.PlayerDataUtils;
import io.github.indrodevteam.indroEconomy.events.EventOnPlayerJoin;
import io.github.indrodevteam.indroEconomy.events.EventOnPlayerMine;
import io.github.indrodevteam.indroEconomy.integrations.EconomyImplementer;
import io.github.indrodevteam.indroEconomy.tasks.TaskSaveAccounts;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class IndroEconomy extends JavaPlugin {
    //instance getting
    public static IndroEconomy getInstance() {
        return instance;
    }
    private static IndroEconomy instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // initialise instancing
        instance = this;

        Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), this, ServicePriority.Normal);

        // config management
        ConfigManager manager = new ConfigManager("config.yml", true);
        manager.updateFromDefaultSave(false);

        // menu updating
        MenuManager.setup(this.getServer(), this);

        // method loading
        loadAccounts();
        registerCommands();
        new TaskSaveAccounts().runTaskTimer(this, 0, 120 * 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveAccounts();
        Bukkit.getScheduler().cancelTasks(this);
    }


    // onEnable methods

    private void loadAccounts() {
        try {
            BankDataUtils.loadBanks();
            PlayerDataUtils.loadAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        PluginManager pm = this.getServer().getPluginManager();

        try {
            CommandManager.createCoreCommand(this,
                    "eco", "Handles the general handling of the economy",
                    "/eco <command", null,
                    CommandBal.class, CommandBalTop.class, CommandOpShop.class, CommandSend.class,
                    CommandDeposit.class, CommandWithdraw.class, CommandConvertPhysical.class, CommandConvertDigital.class);
            CommandManager.createCoreCommand(this,
                    "ecoa", "Handles the admin handling of the economy",
                    "/ecoa <command", null,
                    CommandCheckAccounts.class, CommandSetMoney.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        pm.registerEvents(new EventOnPlayerJoin(), this);
        pm.registerEvents(new EventOnPlayerMine(), this);
    }

    private boolean setupEconomy() { // vault integrator
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp != null;
    }

    // onDisable methods

    private void saveAccounts() {
        try {
            BankDataUtils.saveBanks();
            PlayerDataUtils.saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
