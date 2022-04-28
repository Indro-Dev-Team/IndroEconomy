package io.github.indrodevteam.indroEconomy.commands.accounts;

import io.github.indrodevteam.indroEconomy.EconomyUtils;
import io.github.indrodevteam.indroEconomy.data.PlayerAccount;
import io.github.indrodevteam.indroEconomy.datamanager.PlayerDataUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommandBalTop extends SubCommand {
    @Override
    public String getName() {
        return "baltop";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "get the top 10 player balances in the server";
    }

    @Override
    public String getSyntax() {
        return "/eco baltop";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Comparator<PlayerAccount> comparator = (o1, o2) -> {
            int o1Balance = o1.getBalance();
            int o2Balance = o2.getBalance();

            return o2Balance-o1Balance;
        };
        // sort them
        ArrayList<PlayerAccount> allAccounts = PlayerDataUtils.readAllAccounts();
        allAccounts.sort(comparator);

        // message sender
        String top = "| %d - %s - Balance: %s"; // position, player name, balance
        ArrayList<String> message = new ArrayList<>();
        message.add("+======Top 10 Balances======+");

        if (allAccounts.size() == 1) {
            String name = Bukkit.getOfflinePlayer(allAccounts.get(0).getOwnerID()).getName();
            if (name == null) {name = ChatColor.RED + "Disconnected" + ChatColor.RESET;}
            message.add(String.format(top, 1, name, EconomyUtils.format(allAccounts.get(0).getBalance())));
        } else if (allAccounts.size() >= 11) {
            for (int i = 0; i < 11; i++) {
                String name = Bukkit.getOfflinePlayer(allAccounts.get(i).getOwnerID()).getName();
                if (name == null) {name = ChatColor.RED + "Disconnected" + ChatColor.RESET;}
                message.add(String.format(top, i+1, name, EconomyUtils.format(allAccounts.get(i).getBalance())));
            }
        } else {
            for (int i = 0; i < allAccounts.size(); i++) {
                String name = Bukkit.getOfflinePlayer(allAccounts.get(i).getOwnerID()).getName();
                if (name == null) {name = ChatColor.RED + "Disconnected" + ChatColor.RESET;}
                message.add(String.format(top, i+1, name, EconomyUtils.format(allAccounts.get(i).getBalance())));
            }
        }
        message.add("+===========================+");

        for (String s : message) {
            sender.sendMessage(s);
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }
}
