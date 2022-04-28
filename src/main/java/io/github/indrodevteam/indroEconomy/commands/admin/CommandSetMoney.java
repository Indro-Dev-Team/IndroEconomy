package io.github.indrodevteam.indroEconomy.commands.admin;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        return "ADMINS: sets the balance of players";
    }

    @Override
    public String getSyntax() {
        return "/eco <player> <amount>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 3 && sender.hasPermission("indroEconomy.admin")) {
            EconomyUtils eco = new EconomyUtils();
            Player player = Bukkit.getPlayer(args[1]);
            int value;
            try {
                value = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("You must specify a number!");
                return;
            }
            if (player == null) {
                sender.sendMessage("Player does not exist!"); // add config messages
                return;
            }

            if (!eco.hasAccount(player)) {
                sender.sendMessage("Player's account does not exist, have them join the server!");
                return;
            }

            eco.setBalance(player, value);
            sender.sendMessage("Set " + player.getName() + "'s balance to " + EconomyUtils.format(value));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> arguments = new ArrayList<>();
        switch (args.length) {
            case 2 -> {
                for (Player player1: Bukkit.getServer().getOnlinePlayers()) {
                    arguments.add(player1.getName());
                }
            }
            case 3 -> arguments.add("<amount>");
        }
        return arguments;
    }
}
