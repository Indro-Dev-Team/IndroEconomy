package io.github.indrodevteam.indroEconomy.commands.economy;

import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandTransfer extends SubCommand {
    @Override
    public String getName() {
        return "transfer";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows you to transfer money to and from your bank, keeping it safe";
    }

    @Override
    public String getSyntax() {
        return "/eco transfer <bank/wallet> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_PLAYER_ONLY);
        } else {
            if (args.length == 3) {
                EconomyUtils eco = new EconomyUtils(player);

                // initialise values
                String type = args[1].toUpperCase(Locale.ROOT);
                long amount;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_NUMBER_FORMAT);
                    return;
                }

                EconomyUtils.StorageType moneyStorage;

                try {
                    moneyStorage = EconomyUtils.StorageType.valueOf(type);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_INVALID_SYNTAX);
                    return;
                }

                boolean result = eco.transferToStorage(moneyStorage, amount);
                if (result) {
                    final long wallet = eco.getProfile().getWallet();
                    final long bank = eco.getProfile().getBank();
                    player.sendMessage(ColorTranslator.translateColorCodes(LanguageLoader.TITLE + "Transferred " + EconomyUtils.format(amount)));
                    player.sendMessage(LanguageLoader.TITLE + "Current Wallet Balance: " + EconomyUtils.format(wallet));
                    player.sendMessage(LanguageLoader.TITLE + "Current Bank Balance: " + EconomyUtils.format(bank));
                } else {
                    if (type.equalsIgnoreCase("bank")) {
                        player.sendMessage(LanguageLoader.TITLE + "Do you expect that much to fit in your bank?");
                    } else {
                        player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_NO_MONEY);
                    }
                }
            } else {
                player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_INVALID_SYNTAX);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("bank");
            arguments.add("wallet");
            return arguments;
        }
        if (args.length == 3) {
            arguments.add("<amount>");
            return arguments;
        }
        return null;
    }
}
