package io.github.indrodevteam.indroEconomy.tasks;

import io.github.indrodevteam.indroEconomy.datamanager.BankDataUtils;
import io.github.indrodevteam.indroEconomy.datamanager.PlayerDataUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class TaskSaveAccounts extends BukkitRunnable {
    @Override
    public void run() {
        try {
            PlayerDataUtils.saveAccounts();
            BankDataUtils.saveBanks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
