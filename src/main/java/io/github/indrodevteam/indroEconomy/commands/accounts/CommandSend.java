package io.github.indrodevteam.indroEconomy.commands.accounts;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.config.LanguageLoader;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSend extends SubCommand {
    @Override
    public String getName() {
        return "send";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows sending money from your balance to another active player!";
    }

    @Override
    public String getSyntax() {
        return "/eco send <player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player sender) {
            EconomyUtils eco = new EconomyUtils();

            // /eco pay <player> <amount>
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[1]);
                int amount;

                // error checkers
                if (target == null || !eco.hasAccount(target)) {
                    sender.sendMessage(LanguageLoader.TITLE.get() + LanguageLoader.ERROR_ACCOUNT_NOT_EXISTING.get());
                    return;
                }

                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(LanguageLoader.TITLE.get() + "<amount> must be positive, non-negative and smaller than " + EconomyUtils.format(1000000) + "!");
                    return;
                }

                // transferring amounts to players
                boolean result = eco.sendAccount(sender, target, amount);
                String formatted = EconomyUtils.format(amount);
                if (result) {
                    sender.sendMessage(String.format(LanguageLoader.TITLE.get() + "Payment was Successful, sent %s to %s", formatted, target.getName()));
                    target.sendMessage(String.format(LanguageLoader.TITLE.get() + "Received %s from %s", formatted, sender.getName()));
                    return;
                }
                sender.sendMessage(LanguageLoader.TITLE.get() + LanguageLoader.TRANSFER_FAILURE.get());
            } else {
                sender.sendMessage(LanguageLoader.TITLE.get() + LanguageLoader.ERROR_INVALID_SYNTAX.get());
            }
        } else {
            commandSender.sendMessage(LanguageLoader.TITLE.get() + LanguageLoader.ERROR_PLAYER_ONLY.get());
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player value : playerList) {
                arguments.add(value.getName());
            }
            return arguments;
        }
        if (args.length == 3) {
            arguments.add("<amount>");
            return arguments;
        }
        return arguments;
    }
}
