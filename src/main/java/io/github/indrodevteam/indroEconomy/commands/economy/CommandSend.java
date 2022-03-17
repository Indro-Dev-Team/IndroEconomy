package io.github.indrodevteam.indroEconomy.commands.economy;

import io.github.indrodevteam.indroEconomy.objects.EconomyStorageUtil;
import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return "Allows sending money from your wallet to another active player!";
    }

    @Override
    public String getSyntax() {
        return "/eco send <player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_PLAYER_ONLY);
        } else {
            EconomyUtils eco = new EconomyUtils(player);

            // /eco pay <player> <amount>
            if (args.length > 3) {
                Player target = Bukkit.getPlayer(UUID.fromString(args[1]));
                long amount;

                // error checkers
                if (target == null || EconomyStorageUtil.findAccount(target.getUniqueId()) != null) {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_ACCOUNT_NOT_EXISTING);
                    return;
                }

                if (!(args[2].equalsIgnoreCase("max"))) {
                    try {
                        amount = Long.parseLong(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(LanguageLoader.TITLE + "<amount> must be positive, non-negative and smaller than " + eco.format(1000000000) + "!");
                        return;
                    }
                } else {
                    amount = eco.getProfile().getWallet();
                    if (args[3] == null) {
                        player.sendMessage(String.format("%s Are you sure you want to transfer %s to %s?",
                                LanguageLoader.TITLE, EconomyUtils.format(amount), args[1]));
                        player.sendMessage(String.format("To confirm, do /eco pay %s %s confirm", args[1], args[2]));
                        return;
                    } else if (!(args[3].equalsIgnoreCase("confirm"))) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cInvalid Confirmation Statement, Cancelling Transfer!"));
                        return;
                    }
                }

                // transferring amounts to players
                boolean result = eco.transferToPlayer(target, amount);
                String formatted = EconomyUtils.format(amount);
                if (result) {
                    player.sendMessage(String.format(LanguageLoader.TITLE + "Payment was Successful, sent %s to %s", formatted, target.getName()));
                    target.sendMessage(String.format(LanguageLoader.TITLE + "Received %s from %s", formatted, player.getName()));
                } else {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.TRANSFER_FAILURE);
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
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player value : playerList) {
                arguments.add(value.getName());
            }
            return arguments;
        }
        if (args.length == 3) {
            arguments.add("<amount>");
            arguments.add("max");
            return arguments;
        }
        return null;
    }
}
