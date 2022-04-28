package io.github.indrodevteam.indroEconomy.commands.admin;

import me.kodysimpson.simpapi.command.CommandList;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandList_Admin implements CommandList {
    @Override
    public void displayCommandList(CommandSender sender, List<SubCommand> subCommandList) {
        if (sender.hasPermission("indroEconomy.admin")) {

        }
    }
}
