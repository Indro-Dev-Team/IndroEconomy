package io.github.indrodevteam.indroEconomy.commands.economy;

import io.github.indrodevteam.indroEconomy.IndroEconomy;
import io.github.indrodevteam.indroEconomy.objects.EconomyStorageUtil;
import io.github.indrodevteam.indroEconomy.objects.PlayerEconomyModel;
import io.github.indrodevteam.indroEconomy.utils.EconomyUtils;
import io.github.indrodevteam.indroEconomy.utils.LanguageLoader;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandInfo extends SubCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows you to get your profile, or the profiles of others...";
    }

    @Override
    public String getSyntax() {
        return "/eco info (playerName)";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length == 1) {
            if (commandSender instanceof Player player) {
                PlayerEconomyModel profile = new EconomyUtils(player).getProfile();
                if (profile != null) {
                    String[] message = {
                            "&b&l=======================",
                            "&a" + player.getName() + "'s&r profile",
                            "",
                            "&lExperience: &r" + profile.getCurrentXP() + "/" + profile.getNextXP(),
                            "&lCurrent Level:&r Lvl " + profile.getXpLevel(),
                            "&b------ECONOMY------",
                            "&lWallet: &r" + EconomyUtils.format(profile.getWallet()),
                            "&lBank: &r" + EconomyUtils.format(profile.getBank()) + " / " + profile.getMaxBank(),
                            "&lNet: &r" + EconomyUtils.format(profile.getBank() + profile.getWallet()),
                            "&b&l========================",
                    };

                    for (String messageArg: message) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageArg));
                    }
                } else {
                    player.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_ACCOUNT_NOT_EXISTING);
                }
            } else {
                commandSender.sendMessage(LanguageLoader.TITLE.toString() + LanguageLoader.ERROR_NO_PERMISSION);
            }
        } else if (strings.length == 2) {
            Player player = IndroEconomy.getInstance().getServer().getPlayer(strings[1]);
            if (player != null) {
                EconomyUtils eco = new EconomyUtils(player);
                PlayerEconomyModel profile = eco.getProfile();
                if (profile != null) {
                    String[] message = {
                            "&b&l=======================",
                            "&a" + player.getName() + "'s&r profile",
                            "",
                            "&lExperience: &r" + profile.getCurrentXP() + "/" + profile.getNextXP(),
                            "&lCurrent Level:&r Lvl " + profile.getXpLevel(),
                            "&b------ECONOMY------",
                            "&lWallet: &r" + EconomyUtils.format(profile.getWallet()),
                            "&lBank: &r" + EconomyUtils.format(profile.getBank()) + " / " + profile.getMaxBank(),
                            "&lNet: &r" + EconomyUtils.format(profile.getBank() + profile.getWallet()),
                            "&b&l========================",
                    };

                    for (String messageArg: message) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageArg));
                    }
                }
            } else {
                commandSender.sendMessage(LanguageLoader.TITLE + "Stop searching for ghosts, your player doesn't exist");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        List<String> arguments = new ArrayList<>();
        if (strings.length == 3) {
            for (PlayerEconomyModel profile: EconomyStorageUtil.findAllAccounts()) {
                arguments.add(Bukkit.getOfflinePlayer(profile.getPlayerUUID()).getName());
            }
        }
        return arguments;
    }
}
