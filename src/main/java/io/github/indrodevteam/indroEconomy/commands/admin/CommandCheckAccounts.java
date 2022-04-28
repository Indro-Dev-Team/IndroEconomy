package io.github.indrodevteam.indroEconomy.commands.admin;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.config.ConfigTags;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandCheckAccounts extends SubCommand {
    @Override
    public String getName() {
        return "checkaccounts";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "ADMINS: checks if all players have accounts, and adds them if they don't have one";
    }

    @Override
    public String getSyntax() {
        return "/eco checkaccount";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender.hasPermission("indroEconomy.admin")) {
            EconomyUtils eco = new EconomyUtils();
            sender.sendMessage("Checking Online Players for accounts");
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                if (eco.hasAccount(player)) {
                    continue;
                }

                sender.sendMessage("Creating account for " + player.getName());
                eco.createAccount(player, (int) ConfigTags.MONEY_DEFAULT_AMOUNT.get());
            }
            sender.sendMessage("Accounts Checked!");
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
