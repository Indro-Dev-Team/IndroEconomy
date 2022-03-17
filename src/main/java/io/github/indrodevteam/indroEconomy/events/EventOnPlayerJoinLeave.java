package io.github.indrodevteam.indroEconomy.events;

import io.github.indrodevteam.indroEconomy.objects.EconomyStorageUtil;
import io.github.indrodevteam.indroEconomy.utils.ConfigManager;
import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class    EventOnPlayerJoinLeave implements Listener {
    // HashMap<String, LocalDateTime> cooldown = new HashMap<>();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        EconomyUtils eco = new EconomyUtils(player);
        FileConfiguration config = new ConfigManager("config.yml", true).config;

        // getting config values
        int defaultMoney = config.getInt("money.defaultAmount");

        // creating a player if they don't exist
        if (!player.hasPlayedBefore()) {
            player.sendMessage(LanguageLoader.TITLE + "You start with " + EconomyUtils.format(defaultMoney));
        }
        player.sendMessage(String.valueOf(eco.getProfile() == null));
        if (eco.getProfile() == null) {
            EconomyStorageUtil.createAccount(player.getUniqueId(), defaultMoney, 0, 500, 0, 10, 1);
        }
    }
}
