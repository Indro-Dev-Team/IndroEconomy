package io.github.indrodevteam.indroEconomy.events;

import io.github.indrodevteam.indroEconomy.config.ConfigTags;
import io.github.indrodevteam.indroEconomy.EconomyUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventOnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        EconomyUtils eco = new EconomyUtils();

        if (!eco.hasAccount(player)) {
            eco.createAccount(player,  (int) ConfigTags.MONEY_DEFAULT_AMOUNT.get());
        }
    }
}
