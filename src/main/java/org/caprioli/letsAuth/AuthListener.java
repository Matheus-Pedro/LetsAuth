package org.caprioli.letsAuth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AuthListener implements Listener {

    private final AuthManager auth;
    private final int timeout;

    public AuthListener(AuthManager auth, int timeout) {
        this.auth = auth;
        this.timeout = timeout;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        auth.logout(p);

        p.sendMessage("§cFaça login com §6/login <senha>");

        // Inicia o contador de kicks
        new BukkitRunnable() {
            int seconds = timeout; // tempo limite em segundos

            @Override
            public void run() {
                if (auth.isLoggedIn(p)) {
                    cancel(); // jogador logou
                    return;
                }

                if (seconds <= 0) {
                    p.kickPlayer("§cVocê foi desconectado por inatividade. Faça login mais rápido da próxima vez.");
                    cancel();
                    return;
                }

                seconds--;
            }
        }.runTaskTimer(LetsAuth.getInstance(), 20, 20); // 20 ticks = 1 segundo

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (!auth.isLoggedIn(p)) {
            if (event.getFrom().getX() != event.getTo().getX() ||
                    event.getFrom().getZ() != event.getTo().getZ()) {
                event.setTo(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (!auth.isLoggedIn(p)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player p && !auth.isLoggedIn(p)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p && !auth.isLoggedIn(p)) {
            event.setCancelled(true);
        }
    }

}
