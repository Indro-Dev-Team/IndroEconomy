package io.github.indrodevteam.indroEconomy.events;

import io.github.indrodevteam.indroEconomy.IndroEconomy;
import io.github.indrodevteam.indroEconomy.utils.ConfigManager;
import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EventOnPlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        FileConfiguration config = new ConfigManager("config.yml", true).config;
        final long defaultAmount = config.getInt("money.defaultMoney");

        //initialise the values needed
        Player player = event.getEntity();
        EconomyUtils eco = new EconomyUtils(player);
        boolean deathCausePoverty = false;
        if (player.hasMetadata("deathCausePoverty")) {
            List<MetadataValue> keys = player.getMetadata("deathCausePoverty");
            for (MetadataValue key : keys) {
                if (key.getOwningPlugin() == IndroEconomy.getInstance()) {
                    deathCausePoverty = true;
                    break;
                }
            }
        }

        if (!deathCausePoverty) {
            long wallet = eco.getProfile().getWallet();
            double moneyLost = 0;
            if (wallet > 0) {
                moneyLost = wallet * (config.getInt("money.deathLossPercent") / -100.0);
            }

            //reduce their wallet by the percentage
            player.sendMessage(LanguageLoader.TITLE + "You have died and lost " + EconomyUtils.format((long) moneyLost * -1));
            eco.changeWallet((long) moneyLost);

            if (event.getEntity().getKiller() != null && config.getBoolean("money.killerGetsDeathMoney")) {
                Player killer = event.getEntity().getKiller();
                EconomyUtils killerProfile = new EconomyUtils(killer);
                long moneyGained = (long) (moneyLost * config.getDouble("money.killerGetsDeathMoneyPercent"));
                killerProfile.changeWallet(moneyGained);
                String formatted = EconomyUtils.format(moneyGained);
                killer.sendMessage(String.format("%s You stole %s from %s", LanguageLoader.TITLE, formatted, player.getName()));
            }
        } else if (config.getBoolean("deathByPoverty")) {
            event.setDeathMessage(player.getName() + " couldn't catch up to capitalism");
            player.removeMetadata("deathCausePoverty", IndroEconomy.getInstance());
            player.sendMessage(LanguageLoader.TITLE + "Resetting your wallet and bank, since you ran out of money!");
            eco.getProfile().setWallet(defaultAmount);
            eco.getProfile().setBank(0);
        }
    }
}
