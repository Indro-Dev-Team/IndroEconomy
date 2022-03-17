package io.github.indrodevteam.indroEconomy.commands.economy;

import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandBal extends SubCommand {
    @Override
    public String getName() {
        return "bal";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get your balance on the server ";
    }

    @Override
    public String getSyntax() {
        return "/eco bal (wallet/bank)";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // checks if the sender is a player
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_PLAYER_ONLY);
        } else {
            EconomyUtils eco = new EconomyUtils(player);

            long bank;
            long wallet;

            try {
                wallet = eco.getProfile().getWallet();
                bank = eco.getProfile().getBank();
            } catch (NullPointerException e) {
                player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_ACCOUNT_NOT_EXISTING);
                return;
            }

            if (args.length == 2 && args[1].equalsIgnoreCase("bank")) {
                player.sendMessage(LanguageLoader.TITLE + "Bank Balance: " + eco.format(bank));
            } else {
                player.sendMessage(LanguageLoader.TITLE + "Wallet Balance: " + eco.format(wallet));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("bank");
            return arguments;
        }
        return null;
    }
}