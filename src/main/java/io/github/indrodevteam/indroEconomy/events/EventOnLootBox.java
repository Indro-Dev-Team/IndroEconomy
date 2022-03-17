package io.github.indrodevteam.indroEconomy.events;

import io.github.indrodevteam.indroEconomy.IndroEconomy;
import io.github.indrodevteam.indroEconomy.utils.ConfigManager;
import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.heads.SkullCreator;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventOnLootBox implements Listener {
    @EventHandler
    public void onLootBoxOpen(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.PLAYER_HEAD) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;

        Player player = event.getPlayer();
        EconomyUtils eco = new EconomyUtils(player);
        if (eco.getProfile() == null) return;

        player.sendMessage(ChatColor.DARK_AQUA + "Opening the box..." + ChatColor.MAGIC + "411431425152");
        event.getItem().setAmount(event.getItem().getAmount()-1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(IndroEconomy.getInstance(), () -> {
            Random r = new Random();
            int i = r.nextInt(Material.values().length);
            while(Material.values()[i] == null){
                r = new Random();
                i = r.nextInt(Material.values().length);
            }
            ItemStack item = new ItemStack(Material.values()[i], new Random().nextInt(1, 16));
            if (player.getInventory().contains(Material.AIR, 1)) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), item);
            }
            player.sendMessage(ChatColor.DARK_AQUA + "You've won " + item.getAmount() + " " + WordUtils.capitalize(item.getType().name().replace('_', ' ')));
        }, 100);
    }

    @EventHandler
    public void onLootBoxGet(BlockBreakEvent event) {
        double chance = new Random().nextDouble(0.001F, 100F);
        FileConfiguration config = new ConfigManager("config.yml", true).config;
        if (chance <= config.getDouble("lootbox.chance") &&
                config.getBoolean("lootbox.enabled")) {
            ItemStack crate = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM0YTM2MTlkYzY2ZmM1Zjk0MGY2OWFhMzMxZTU4OGI1Mjg1ZjZlMmU5OTgxYjhmOTNiOTk5MTZjMjk0YjQ4In19fQ==");
            ItemMeta crateMeta = crate.getItemMeta();
            assert crateMeta != null;
            crateMeta.setDisplayName(ChatColor.AQUA + "Loot Crate");
            List<String> lore = new ArrayList<>();
            lore.add(ColorTranslator.translateColorCodes("&5This crate can give you good, &kor bad luck!"));
            lore.add(ColorTranslator.translateColorCodes("&5Interact with this item to open it!"));
            crateMeta.setLore(lore);
            crateMeta.setLocalizedName(ChatColor.AQUA + "Loot Crate");
            crate.setItemMeta(crateMeta);

            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), crate);
        }
    }
}
