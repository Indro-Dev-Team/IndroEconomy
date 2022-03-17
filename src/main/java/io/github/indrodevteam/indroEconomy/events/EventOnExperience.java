package io.github.indrodevteam.indroEconomy.events;

import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Random;

public class EventOnExperience implements Listener {
    @EventHandler
    public void onPlayerExperience(PlayerExpChangeEvent event) {
        EconomyUtils eco = new EconomyUtils(event.getPlayer());

        long currentXP = eco.getProfile().getCurrentXP();
        long nextXP = eco.getProfile().getNextXP();
        currentXP += event.getAmount();
        eco.getProfile().setCurrentXP(currentXP);
        while (currentXP >= nextXP) {
            eco.getProfile().setXpLevel(eco.getProfile().getXpLevel() + 1);
            eco.getProfile().setMaxBank(eco.getProfile().getMaxBank() + new Random().nextLong(eco.getProfile().getMaxBank()/2));
            currentXP -= nextXP;
            nextXP = nextXP + new Random().nextLong(nextXP*2);

            eco.getProfile().setCurrentXP(currentXP);
            eco.getProfile().setNextXP(nextXP);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
        eco.saveToProfile();
    }
}
