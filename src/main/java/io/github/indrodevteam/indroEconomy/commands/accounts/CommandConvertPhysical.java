package io.github.indrodevteam.indroEconomy.commands.accounts;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.config.LanguageLoader;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandConvertPhysical extends SubCommand {
    @Override
    public String getName() {
        return "banknotes";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Converts your money into paper money";
    }

    @Override
    public String getSyntax() {
        return "/eco banknotes <amount>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        EconomyUtils eco = new EconomyUtils();
        if (sender instanceof Player player) {
            if (args.length == 2) {
                int value;
                try {
                    value = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(LanguageLoader.TITLE.get() + "<amount> must be positive, non-negative and smaller than " + EconomyUtils.format(1000000) + "!");
                    return;
                }
                player.sendMessage(LanguageLoader.TITLE.get() + "Processing " + EconomyUtils.format(value) + "...");

                if (!eco.hasAmount(player, value)) {
                    player.sendMessage(LanguageLoader.TITLE.get() + LanguageLoader.ERROR_NO_MONEY.get());
                    return;
                }
                eco.changeBalance(player, -value);

                //hundred handling
                ItemStack hundredBill = new ItemStack(Material.PAPER);
                int hundred = (value - (value % 100)) / 100;
                value %= 100;
                hundredBill.setAmount(hundred);
                ItemMeta hundredMeta = hundredBill.getItemMeta();
                assert hundredMeta != null;
                hundredMeta.setCustomModelData(100);
                hundredMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(100));
                hundredBill.setItemMeta(hundredMeta);

                if (hundred > 0) {
                    player.getWorld().dropItem(player.getLocation(), hundredBill);
                }

                // fifty handling
                ItemStack fiftyBill = new ItemStack(Material.PAPER);
                int fifty = (value - (value % 50)) / 50;
                value %= 50;
                fiftyBill.setAmount(fifty);
                ItemMeta fiftyMeta = fiftyBill.getItemMeta();
                assert fiftyMeta != null;
                fiftyMeta.setCustomModelData(50);
                fiftyMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(50));
                fiftyBill.setItemMeta(fiftyMeta);

                if (fifty > 0) {
                    player.getWorld().dropItem(player.getLocation(), fiftyBill);
                }

                //twenty handling
                ItemStack twentyBill = new ItemStack(Material.PAPER);
                int twenty = (value - (value % 20)) / 20;
                value %= 20;
                twentyBill.setAmount(twenty);
                ItemMeta twentyMeta = twentyBill.getItemMeta();
                assert twentyMeta != null;
                twentyMeta.setCustomModelData(20);
                twentyMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(20));
                twentyBill.setItemMeta(twentyMeta);

                if (twenty > 0) {
                    player.getWorld().dropItem(player.getLocation(), twentyBill);
                }

                // ten handling
                ItemStack tenBill = new ItemStack(Material.PAPER);
                int ten = (value - (value % 10)) / 10;
                value %= 10;
                tenBill.setAmount(ten);
                ItemMeta tenMeta = tenBill.getItemMeta();
                assert tenMeta != null;
                tenMeta.setCustomModelData(10);
                tenMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(10));
                tenBill.setItemMeta(tenMeta);

                if (ten > 0) {
                    player.getWorld().dropItem(player.getLocation(), tenBill);
                }

                // one handling
                int one = 0;
                ItemStack oneBill = new ItemStack(Material.PAPER);
                while (value != 0) {
                    one += 1;
                    value -= 1;
                }
                oneBill.setAmount(one);
                ItemMeta oneMeta = oneBill.getItemMeta();
                assert oneMeta != null;
                oneMeta.setCustomModelData(1);
                oneMeta.setDisplayName(ChatColor.GREEN + EconomyUtils.format(1));
                oneBill.setItemMeta(oneMeta);

                if (one > 0) {
                    player.getWorld().dropItem(player.getLocation(), oneBill);
                }

                player.sendMessage(LanguageLoader.TITLE.get() + "Processed your money!");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("<amount>");
        }
        return arguments;
    }
}
