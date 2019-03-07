package net.evatunaskillcommand.salkcoding.skill;

import net.evatunaskillcommand.salkcoding.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class AreaAbsorption extends CoolTime {

    public static void execute(Player player, double radius, int durability, int amplifier, int type, int cooltime) {
        if (GuidedBullet.isCoolTime(player.getUniqueId())) return;
        addPlayer(player.getUniqueId(), cooltime);
        switch (type) {
            case 1:
            default:
                AreaParticleType1 p1 = new AreaParticleType1(player, radius, durability, amplifier);
                p1.setTask(Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), p1, 1, 10));
                break;
            case 2:
                AreaParticleType2 p2 = new AreaParticleType2(player, radius, durability, amplifier);
                p2.setTask(Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), p2, 1, 0));
                break;
        }
    }

    static class AreaParticleType1 implements Runnable {

        private Player player;
        private double radius;
        private int durability;
        private int amplifier;
        private BukkitTask task;

        private Location baseLocation;

        private float repeat;
        private float semiRadius;
        private float soundPitch;

        void setTask(BukkitTask task) {
            this.task = task;
        }

        AreaParticleType1(Player player, double radius, int durability, int amplifier) {
            this.player = player;
            this.baseLocation = player.getLocation().clone();
            this.radius = radius;
            this.durability = durability;
            this.amplifier = amplifier;
            repeat = 3f;
            semiRadius = 0;
            soundPitch = 0.6f;
        }

        private final float max = (float) (Math.PI * 2);
        private final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.YELLOW, 2);

        @Override
        public void run() {
            if (semiRadius >= radius) {
                stop();
                return;
            }
            spawnParticleCircle(baseLocation, semiRadius, max, dustOptions);
            baseLocation.getWorld().playSound(baseLocation, Sound.BLOCK_NOTE_BLOCK_BELL, (float) radius / 5f, soundPitch);

            semiRadius += radius / repeat;
            soundPitch += 0.15;
        }

        private void stop() {
            task.cancel();
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> addPotionEffectNearBy(player, baseLocation, radius, durability, amplifier));
        }

    }

    static class AreaParticleType2 implements Runnable {

        private Player player;
        private double radius;
        private int durability;
        private int amplifier;
        private BukkitTask task;

        private Location baseLocation;

        void setTask(BukkitTask task) {
            this.task = task;
        }

        AreaParticleType2(Player player, double radius, int durability, int amplifier) {
            this.player = player;
            this.baseLocation = player.getLocation().clone();
            this.radius = radius;
            this.durability = durability;
            this.amplifier = amplifier;
        }

        private float i = 0;
        private final float max = (float) (Math.PI * 2);
        private float soundPitch = 0.4f;
        private final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.YELLOW, 2);

        @Override
        public void run() {
            if (i > max) {
                stop();
                return;
            }
            double x = radius * Math.cos(i);
            double z = radius * Math.sin(i);
            Location location = new Location(baseLocation.getWorld(), baseLocation.getX() + x, baseLocation.getY() + 0.2, baseLocation.getZ() + z);
            if (location.getBlock().getType() != Material.AIR)
                location.setY(location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 0.2);
            baseLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 0, 0, 0, dustOptions);
            baseLocation.getWorld().playSound(baseLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 0.2f, soundPitch);
            soundPitch += 0.02f;
            i += 0.1f;
        }

        private void stop() {
            task.cancel();
            spawnParticleCircle(baseLocation, radius, max, dustOptions);
            player.getWorld().playSound(baseLocation, Sound.BLOCK_GLASS_BREAK, (float) radius, 0.8f);

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> addPotionEffectNearBy(player, baseLocation, radius, durability, amplifier));
        }

    }

    private static void addPotionEffectNearBy(Player player, Location baseLocation, double radius, int durability, int amplifier) {
        if (baseLocation.distance(player.getLocation()) <= radius)
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, durability, amplifier));
        for (Entity entity : baseLocation.getWorld().getNearbyEntities(baseLocation, radius, radius, radius)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, durability, amplifier));
            }
        }
    }

    private static void spawnParticleCircle(Location baseLocation, double radius, double max, Particle.DustOptions dustOptions) {
        List<Location> list = new ArrayList<>();
        for (float i = 0; i < max; i += 0.1) {
            double x = radius * Math.cos(i);
            double z = radius * Math.sin(i);
            Location location = new Location(baseLocation.getWorld(), baseLocation.getX() + x, baseLocation.getY() + 0.2, baseLocation.getZ() + z);
            if (location.getBlock().getType() != Material.AIR)
                location.setY(location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 0.2);
            list.add(location);
        }
        for (Location location : list)
            baseLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 0, 0, 0, dustOptions);
    }

}
