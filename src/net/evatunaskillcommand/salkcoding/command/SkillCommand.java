package net.evatunaskillcommand.salkcoding.command;

import net.evatunaskillcommand.salkcoding.skill.AreaAbsorption;
import net.evatunaskillcommand.salkcoding.skill.GuidedBullet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkillCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 5) {
            if (strings[0].equalsIgnoreCase("gb") || strings[0].equalsIgnoreCase("guildedbullet")) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    UUID uuid = player.getUniqueId();
                    try {
                        double damage = Double.parseDouble(strings[1]);
                        //                                           damage,                         type,                    particle,                    cooltime
                        GuidedBullet.execute(player, damage, Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), Integer.parseInt(strings[4]));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        if (strings.length == 6) {
            if (strings[0].equalsIgnoreCase("aa") || strings[0].equalsIgnoreCase("areaabsorption")) {
                if (commandSender instanceof Player) {
                    try {
                        double r = Double.parseDouble(strings[1]);
                        int d = Integer.parseInt(strings[2]);
                        int a = Integer.parseInt(strings[3]);
                        int t = Integer.parseInt(strings[4]);
                        int c = Integer.parseInt(strings[5]);
                        AreaAbsorption.execute((Player) commandSender, r, d * 20, a - 1, t, c);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return true;
    }

}
