package org.caprioli.letsAuth;

import org.bukkit.plugin.java.JavaPlugin;
import org.caprioli.letsAuth.commands.LoginCommand;
import org.caprioli.letsAuth.commands.RegisterCommand;

public final class LetsAuth extends JavaPlugin {
    private static LetsAuth instance;
    private DatabaseManager db;
    private AuthManager auth;

    public static LetsAuth getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        auth = new AuthManager();
        try {
            String url = getConfig().getString("database.url");
            String user = getConfig().getString("database.user");
            String pass = getConfig().getString("database.password");
            db = new DatabaseManager(url, user, pass);
        } catch (Exception e) {
            getLogger().severe("Erro ao conectar no banco de dados!");
            return;
        }

        getCommand("register").setExecutor(new RegisterCommand(db));
        getCommand("login").setExecutor(new LoginCommand(db, auth));

        getServer().getPluginManager().registerEvents(new AuthListener(auth), this);


        getLogger().info("LetsAuth iniciado!");
    }


    @Override
    public void onDisable() {
        auth.clearAll();
    }
}
