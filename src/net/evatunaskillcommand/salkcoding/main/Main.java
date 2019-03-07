package net.evatunaskillcommand.salkcoding.main;

import net.evatunaskillcommand.salkcoding.Constants;
import net.evatunaskillcommand.salkcoding.command.SkillCommand;
import net.evatunaskillcommand.salkcoding.skill.GuidedBullet;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public Main() {
        if (instance != null)
            throw new IllegalStateException();
        instance = this;
    }

    @Override
    public void onEnable() {
        System.out.println(Constants.Console + "Plugin is now working");
        getCommand("Skill").setExecutor(new SkillCommand());
        Bukkit.getPluginManager().registerEvents(new GuidedBullet(), this);
    }

    @Override
    public void onDisable() {
        System.out.println(Constants.Console + "Plugin is now sleeping");
    }

}
