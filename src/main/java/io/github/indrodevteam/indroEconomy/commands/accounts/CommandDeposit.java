package io.github.indrodevteam.indroEconomy.commands.accounts;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.config.LanguageLoader;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandDeposit extends SubCommand {
    @Override
    public String getName() {
        return "deposit";
    }

    @Override
    public List<String> getAliases() {
        List<String> alias = new ArrayList<>();
        alias.add("d");
        return alias;
    }

    @Override
    public String getDescription() {
        return "Deposit some of your wallet to your safe";
    }

    @Override
    public String getSyntax() {
        return "/eco deposit <amount>/max";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        EconomyUtils eco = new EconomyUtils();
        if (sender instanceof Player player) {
            if (args.length == 2) {
                int value;
                if (args[1].equalsIgnoreCase("max")) {
                    value = eco.getAccount(player).getBalance();
                } else {
                    try {
                        value = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(LanguageLoader.TITLE.get() + "<amount> must be positive, non-negative and smaller than " + EconomyUtils.format(1000000) + "!");
                        return;
                    }
                }

                if (eco.depositAccount(player, value)) {
                    player.sendMessage(LanguageLoader.TITLE.get() + "Deposited " + EconomyUtils.format(value));
                    return;
                }
                player.sendMessage(LanguageLoader.TITLE.get() + "Error! Do you have enough money?");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("<amount>");
            arguments.add("max");
        }
        return arguments;
    }
}
