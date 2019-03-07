package net.evatunaskillcommand.salkcoding.skill;

import net.evatunaskillcommand.salkcoding.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

import static org.bukkit.Bukkit.getScheduler;

public class GuidedBullet extends CoolTime implements Listener {

    private static HashMap<Integer, DataInfo> dataHashMap = new HashMap<>();

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof LargeFireball || entity instanceof  WitherSkull){
            event.setCancelled(true);
        }
    }

    public static void execute(Player player, double damage, int type, int particle, int cooltime) {
        if(damage > 1024) return;
        if (GuidedBullet.isCoolTime(player.getUniqueId())) return;
        addPlayer(player.getUniqueId(), cooltime);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 5, 0.5f);
        Projectile bullet;
        Sound sound;
        switch (type) {
            case 1:
                bullet = player.getWorld().spawn(player.getEyeLocation(), ShulkerBullet.class);
                sound = Sound.ENTITY_GENERIC_EXPLODE;
                break;
            case 2:
                bullet = player.getWorld().spawn(player.getEyeLocation(), WitherSkull.class);
                sound = Sound.ENTITY_GENERIC_EXPLODE;
                break;
            case 3:
                bullet = player.getWorld().spawn(player.getEyeLocation(), Arrow.class);
                sound = Sound.ENTITY_ARROW_HIT;//ENTITY_ARROW_HIT_PLAYER
                break;
            case 4:
                bullet = player.getWorld().spawn(player.getEyeLocation(), DragonFireball.class);
                sound = Sound.ENTITY_GENERIC_EXPLODE;
                break;
            case 5:
                bullet = player.getWorld().spawn(player.getEyeLocation(), LargeFireball.class);
                sound = Sound.ENTITY_GENERIC_EXPLODE;
                break;
            case 6:
                bullet = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
                sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;//ENTITY_ARROW_HIT
                break;
            default:
                bullet = player.getWorld().spawn(player.getEyeLocation(), ShulkerBullet.class);
                sound = Sound.ENTITY_GENERIC_EXPLODE;
        }
        bullet.setGlowing(true);
        bullet.setShooter(player);
        DataInfo info = new DataInfo(600, damage, sound, (particle != 0 ? Particle.EXPLOSION_HUGE : null));
        dataHashMap.put(bullet.getEntityId(), info);
        info.setTask(Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            DataInfo i = dataHashMap.get(bullet.getEntityId());
            if (!player.isOnline())
                i.getTask().cancel();
            if (i.getTimer() > 0 && !bullet.isDead()) bullet.setVelocity(player.getEyeLocation().getDirection());
            else {
                bullet.remove();
                dataHashMap.remove(bullet.getEntityId());
                info.getTask().cancel();
            }
            i.setTimer(i.getTimer() - 1);
        }, 1, 1));
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (dataHashMap.containsKey(event.getEntity().getEntityId())) {
            Entity bullet = event.getEntity();
            Location location = bullet.getLocation();
            DataInfo info = dataHashMap.get(bullet.getEntityId());
            if (info.getParticle() != null)
                location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, bullet.getLocation(), (int) (info.getDamage() / 5));
            if (info.getSound() != null)
                location.getWorld().playSound(location, info.getSound(), 10, 0.5f);
            location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), (float) (info.getDamage() / 12f), false, false);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        int id = event.getDamager().getEntityId();
        if (dataHashMap.containsKey(id)) {
            if (event.getEntity() == null)
                return;
            if (event.getEntity() instanceof LivingEntity) {
                event.setDamage(dataHashMap.get(id).getDamage());
                getScheduler().runTaskLater(Main.getInstance(), () -> ((LivingEntity) event.getEntity()).removePotionEffect(PotionEffectType.LEVITATION), 5);
            }
        }
    }

}

class DataInfo {

    private int timer;
    private double damage;
    private Sound sound;
    private Particle particle;
    private BukkitTask task;

    DataInfo(int timer, double damage, Sound sound, Particle particle) {
        this.timer = timer;
        this.damage = damage;
        this.sound = sound;
        this.particle = particle;
    }

    int getTimer() {
        return timer;
    }

    double getDamage() {
        return damage;
    }

    Sound getSound() {
        return sound;
    }

    Particle getParticle() {
        return particle;
    }

    BukkitTask getTask() {
        return task;
    }

    void setTimer(int timer) {
        this.timer = timer;
    }

    void setTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "{timer : " +
                timer +
                ", damage : " +
                damage +
                ", sound : " +
                sound +
                ", particle : " +
                particle +
                ", task : " +
                task +
                "}";
    }

}

