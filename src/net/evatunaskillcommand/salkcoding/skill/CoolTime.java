package net.evatunaskillcommand.salkcoding.skill;

import net.evatunaskillcommand.salkcoding.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

class CoolTime {

    private static HashMap<UUID, Calendar> timer = new HashMap<>();
    private static HashMap<UUID, Integer> added = new HashMap<>();

    static void addPlayer(UUID uuid, int amount) {
        if (amount <= 0) return;
        if (!timer.containsKey(uuid)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, amount);
            timer.put(uuid, calendar);
            added.put(uuid, amount);
        }
    }

    static Calendar getTime(UUID uuid) {
        return timer.get(uuid);
    }

    static Integer getAdded(UUID uuid) {
        return added.get(uuid);
    }

    static boolean isCoolTime(UUID uuid) {
        if (timer.containsKey(uuid)) {
            Calendar now = Calendar.getInstance();
            Calendar time = timer.get(uuid);
            if (time.getTimeInMillis() - now.getTimeInMillis() < 0) {
                removePlayer(uuid);
                return false;
            }
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = GuidedBullet.getTime(uuid);
            float addedSecond = GuidedBullet.getAdded(uuid);
            float second = (c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000f;
            int percentage = (int) (10 - (second / addedSecond * 10f)) + 1;
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GREEN);
            for (int i = 0; i < percentage; i++)
                builder.append("●");
            builder.append(ChatColor.WHITE);
            for (int i = 0; i < 10 - percentage; i++)
                builder.append("●");
            builder.append(ChatColor.DARK_GRAY).append(ChatColor.ITALIC).append(ChatColor.BOLD).append(" | ").append(ChatColor.GRAY + "" + ChatColor.ITALIC + "쿨타임: " + ChatColor.GREEN + String.format("%.1f", second) + ChatColor.GRAY + "초");
            Constants.sendActionBar(Bukkit.getPlayer(uuid), builder.toString());
            return true;
        }
        return false;
    }

    static void removePlayer(UUID uuid) {
        timer.remove(uuid);
        added.remove(uuid);
    }

}
