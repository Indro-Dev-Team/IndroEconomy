package io.github.indrodevteam.indroEconomy.events;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.config.ConfigManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventOnPlayerMine implements Listener {

    @EventHandler
    public void onPlayerMine(BlockBreakEvent event) {
        FileConfiguration config = new ConfigManager("config.yml", true).config;
        Player player = event.getPlayer();
        EconomyUtils eco = new EconomyUtils();
        String block = event.getBlock().getType().toString();

        List<String> blocks = new ArrayList<>(config.getConfigurationSection("blocks").getKeys(false));
        if (blocks.contains(block) &&
                !(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))) {

            List<String> drops = new ArrayList<>(config.getStringList("blocks." + block));

            Random random = new Random();
            String drop = drops.get(random.nextInt(drops.size()));

            int amount = Integer.parseInt(drop);
            eco.changeBalance(player, amount);

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GREEN + "+" + EconomyUtils.format(Integer.parseInt(drop))));
        }
    }
}
