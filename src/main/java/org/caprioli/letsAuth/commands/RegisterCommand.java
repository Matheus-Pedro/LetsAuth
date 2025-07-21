package org.caprioli.letsAuth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.caprioli.letsAuth.DatabaseManager;

public class RegisterCommand implements CommandExecutor {
    private final DatabaseManager db;

    public RegisterCommand(DatabaseManager db) {
        this.db = db;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Este comando só pode ser executado por jogadores.");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage("§cUse: /register <senha>");
            return true;
        }

        String username = p.getName();
        String password = args[0];

        try {
            if (db.isRegistered(username)) {
                p.sendMessage("§eVocê já está registrado.");
                return true;
            }

            db.register(username, password);
            p.sendMessage("§aRegistrado com sucesso!");
        } catch (Exception e) {
            p.sendMessage("§cErro ao registrar. Consulte um administrador.");
            e.printStackTrace();
        }

        return true;
    }
}
