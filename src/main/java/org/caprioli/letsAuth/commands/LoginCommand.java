package org.caprioli.letsAuth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.caprioli.letsAuth.AuthManager;
import org.caprioli.letsAuth.DatabaseManager;

public class LoginCommand implements CommandExecutor {
    private final DatabaseManager db;
    private final AuthManager auth;

    public LoginCommand(DatabaseManager db, AuthManager auth) {
        this.db = db;
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage("§cUse: /login <senha>");
            return true;
        }

        if (auth.isLoggedIn(p)) {
            p.sendMessage("§eVocê já está logado.");
            return true;
        }

        String senha = args[0];
        if (db.checkPassword(p.getName(), senha)) {
            auth.login(p);
            p.sendMessage("§aLogin bem-sucedido!");
        } else {
            p.sendMessage("§cSenha incorreta.");
        }

        return true;
    }
}
