package org.caprioli.letsAuth;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;

public class AuthManager {
    private final Set<String> loggedIn = new HashSet<>();

    public void login(Player player) {
        loggedIn.add(player.getName().toLowerCase());
    }

    public boolean isLoggedIn(Player player) {
        return loggedIn.contains(player.getName().toLowerCase());
    }

    public void logout(Player player) {
        loggedIn.remove(player.getName().toLowerCase());
    }

    public void clearAll() {
        loggedIn.clear();
    }
}
