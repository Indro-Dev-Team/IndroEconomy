package io.github.indrodevteam.indroEconomy.commands.economy;

import io.github.indrodevteam.indroEconomy.objects.EconomyStorageUtil;
import io.github.indrodevteam.indroEconomy.objects.PlayerEconomyModel;
import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
    This class implements:
        - /setmoney <bank/wallet> <target> <amount>
*/

public class CommandSetMoney extends SubCommand {
    @Override
    public String getName() {
        return "setmoney";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows admins to configure the amount of money a player has!";
    }

    @Override
    public String getSyntax() {
        return "/eco setmoney <bank/wallet> <player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender.isOp()) {
            if (args.length == 4) {
                // initialising values
                final String type = args[1];
                final Player target = Bukkit.getPlayer(args[2]);
                long amount;

                try {
                    amount = Long.parseLong(args[3]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(LanguageLoader.TITLE + "<amount> must be positive, and smaller than $18,446,744,073,709,551,615!");
                    return;
                }


                // error checkers
                if (target == null || EconomyStorageUtil.findAccount(target.getUniqueId()) == null) {
                    commandSender.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_ACCOUNT_NOT_EXISTING);
                    return;
                }
                EconomyUtils eco = new EconomyUtils(target);

                if (type.equals("wallet")) {
                    eco.getProfile().setWallet(amount);
                    String formatted = EconomyUtils.format(eco.getProfile().getWallet());
                    commandSender.sendMessage(ColorTranslator.translateColorCodes("&fSet &a" + target.getName() + "'s &fwallet to " + formatted));
                } else if (type.equals("bank")) {
                    eco.getProfile().setBank(amount);
                    String formatted = EconomyUtils.format(eco.getProfile().getBank());
                    commandSender.sendMessage(ColorTranslator.translateColorCodes("&fSet &a" + target.getName() + "'s &fbank to " + formatted));
                }
            } else {
                commandSender.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_INVALID_SYNTAX);
            }
        } else {
            commandSender.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_NO_PERMISSION);
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("bank");
            arguments.add("wallet");
        } else if (args.length == 3) {
            for (PlayerEconomyModel profile: EconomyStorageUtil.findAllAccounts()) {
                arguments.add(Bukkit.getOfflinePlayer(profile.getPlayerUUID()).getName());
            }
        } else if (args.length == 4) {
            arguments.add("<amount>");
        }
        return arguments;
    }
}
