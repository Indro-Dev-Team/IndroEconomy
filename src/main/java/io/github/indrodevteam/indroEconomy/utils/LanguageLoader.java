package io.github.indrodevteam.indroEconomy.utils;

import me.kodysimpson.simpapi.colors.ColorTranslator;

public enum LanguageLoader {
    TITLE("&9[&eIndroEconomy&9]&f "),

    TRANSFER_SUCCESS("Transferred %amount% to %target%!"),
    TRANSFER_FAILURE("Could not transfer!"),

    RELOADING("Reloading Config Files!"),
    AUTOSAVE("Autosaving!"),

    ERROR_ACCOUNT_NOT_EXISTING("This account does not exist!"),
    ERROR_NO_PERMISSION("You do not have permission to do this!"),
    ERROR_NO_MONEY("You do not have enough money!"),
    ERROR_PLAYER_ONLY("This can only be executed by players!"),
    ERROR_INVALID_SYNTAX("Invalid syntax! Did you type the command correctly?"),
    ERROR_NUMBER_FORMAT("This is an invalid number, try a smaller number, or use a number!");

    final String message;

    LanguageLoader(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ColorTranslator.translateColorCodes(message);
    }
}
