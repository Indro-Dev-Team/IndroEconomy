package io.github.indrodevteam.indroEconomy.events;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.IndroEconomy;
import io.github.indrodevteam.indroEconomy.config.ConfigTags;
import io.github.indrodevteam.indroEconomy.config.LanguageLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EventOnPlayerDeath implements Listener {
    EconomyUtils eco = new EconomyUtils();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final int defaultAmount = (int) ConfigTags.MONEY_DEFAULT_AMOUNT.get();
        final int deathLossPercent = (int) ConfigTags.DEATH_LOSS_PERCENT.get();
        final boolean killerGainsDeathMoney = (boolean) ConfigTags.DEATH_KILLER_GAIN_PERCENT.get();
        final double killerGainsPercent = (int) ConfigTags.DEATH_KILLER_GAIN_PERCENT.get();
        final boolean deathByPoverty = (boolean) ConfigTags.POVERTY_PERMITTED.get();

        //initialise the values needed
        Player player = event.getEntity();
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
            int wallet = eco.getAccount(player).getBalance();
            double moneyLost = 0;
            if (wallet > 0) {
                moneyLost = wallet * (deathLossPercent / 100.0);
            }

            //reduce their wallet by the percentage
            player.sendMessage(LanguageLoader.TITLE.get() + "You have died and lost " + EconomyUtils.format((int) moneyLost));
            eco.changeBalance(player, (int) -moneyLost);

            if (event.getEntity().getKiller() != null && killerGainsDeathMoney) {
                Player killer = event.getEntity().getKiller();
                int moneyGained = (int) (moneyLost * killerGainsPercent);
                eco.changeBalance(killer, moneyGained);
                String formatted = EconomyUtils.format(moneyGained);
                killer.sendMessage(String.format("%s You stole %s from %s", LanguageLoader.TITLE.get(), formatted, player.getName()));
            }
        } else if (deathByPoverty) {
            event.setDeathMessage(player.getName() + " discovered the dark side to capitalism");
            player.removeMetadata("deathCausePoverty", IndroEconomy.getInstance());
            player.sendMessage(LanguageLoader.TITLE.get() + "Resetting your wallet and bank, since you ran out of money!");
            eco.changeBalance(player, defaultAmount);
        }
    }
}
