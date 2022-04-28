package io.github.indrodevteam.indroEconomy.commands.accounts;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.config.LanguageLoader;
import io.github.indrodevteam.indroEconomy.data.PlayerAccount;
import io.github.indrodevteam.indroEconomy.datamanager.PlayerDataUtils;
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
        return "Get your balance, or your safe";
    }

    @Override
    public String getSyntax() {
        return "/eco bal (safe)";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            // account checker
            PlayerAccount account = PlayerDataUtils.readAccount(player.getUniqueId());;
            if (account == null) {
                player.sendMessage(LanguageLoader.TITLE.get() + LanguageLoader.ERROR_NO_PERMISSION);
                return;
            }

            // command parser
            switch (args.length) {
                case 1 -> // /bal
                        player.sendMessage(LanguageLoader.TITLE.get() + "Balance: " + EconomyUtils.format(account.getBalance()));
                case 2 -> { // /bal safe
                    if (args[1].equalsIgnoreCase("safe")) {
                        player.sendMessage(LanguageLoader.TITLE.get() + "Safe: " + EconomyUtils.format(account.getSafe()));
                    }
                }
            }
            return;
        }
        sender.sendMessage("This cannot be executed by a player");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            arguments.add("safe");
        }
        return arguments;
    }
}
