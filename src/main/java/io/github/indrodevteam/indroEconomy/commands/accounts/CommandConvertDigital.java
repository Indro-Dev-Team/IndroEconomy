package io.github.indrodevteam.indroEconomy.commands.accounts;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CommandConvertDigital extends SubCommand {
    @Override
    public String getName() {
        return "digitise";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Convert all paper money in your inv to digital money";
    }

    @Override
    public String getSyntax() {
        return "/eco digitise";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            //hundred handling
            ItemStack hundredBill = new ItemStack(Material.PAPER);
            ItemMeta hundredMeta = hundredBill.getItemMeta();
            assert hundredMeta != null;
            hundredMeta.setCustomModelData(100);
            hundredMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(100));
            hundredBill.setItemMeta(hundredMeta);

            // fifty handling
            ItemStack fiftyBill = new ItemStack(Material.PAPER);
            ItemMeta fiftyMeta = fiftyBill.getItemMeta();
            assert fiftyMeta != null;
            fiftyMeta.setCustomModelData(50);
            fiftyMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(50));
            fiftyBill.setItemMeta(fiftyMeta);

            //twenty handling
            ItemStack twentyBill = new ItemStack(Material.PAPER);
            ItemMeta twentyMeta = twentyBill.getItemMeta();
            assert twentyMeta != null;
            twentyMeta.setCustomModelData(20);
            twentyMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(20));
            twentyBill.setItemMeta(twentyMeta);

            // ten handling
            ItemStack tenBill = new ItemStack(Material.PAPER);
            ItemMeta tenMeta = tenBill.getItemMeta();
            assert tenMeta != null;
            tenMeta.setCustomModelData(10);
            tenMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(10));
            tenBill.setItemMeta(tenMeta);

            // one handling
            ItemStack oneBill = new ItemStack(Material.PAPER);
            ItemMeta oneMeta = oneBill.getItemMeta();
            assert oneMeta != null;
            oneMeta.setCustomModelData(1);
            oneMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(1));
            oneBill.setItemMeta(oneMeta);

            int value = 0;
            int bills = 0;

            for (ItemStack next : player.getInventory()) {
                if (next == null) {
                    continue;
                }
                ItemMeta nextMeta = next.getItemMeta();
                assert nextMeta != null;
                if (nextMeta.hasCustomModelData()) {
                    if (oneBill.equals(next) || (nextMeta.getCustomModelData() == 1 && next.getType().equals(Material.PAPER))) {
                        bills += next.getAmount();
                        value += next.getAmount();
                        player.getInventory().remove(next);
                    } else if (tenBill.equals(next) || (nextMeta.getCustomModelData() == 10 && next.getType().equals(Material.PAPER))) {
                        bills += next.getAmount();
                        value += next.getAmount() * 10;
                        player.getInventory().remove(next);
                    } else if (twentyBill.equals(next) || (nextMeta.getCustomModelData() == 20 && next.getType().equals(Material.PAPER))) {
                        bills += next.getAmount();
                        value += next.getAmount() * 20;
                        player.getInventory().remove(next);
                    } else if (fiftyBill.equals(next) || (nextMeta.getCustomModelData() == 50 && next.getType().equals(Material.PAPER))) {
                        bills += next.getAmount();
                        value += next.getAmount() * 50;
                        player.getInventory().remove(next);
                    } else if (hundredBill.equals(next) || (nextMeta.getCustomModelData() == 100 && next.getType().equals(Material.PAPER))) {
                        bills += next.getAmount();
                        value += next.getAmount() * 100;
                        player.getInventory().remove(next);
                    }
                }
            }
            new EconomyUtils().changeBalance(player, value);
            player.sendMessage("Converted " + bills + " bills to " + EconomyUtils.format(value));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
