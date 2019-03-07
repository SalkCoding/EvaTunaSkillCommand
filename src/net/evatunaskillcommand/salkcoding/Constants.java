package net.evatunaskillcommand.salkcoding;

import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Constants {

    public static final String Info_Format = ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.ITALIC + "[ " + ChatColor.GREEN + ChatColor.ITALIC + "!" + ChatColor.WHITE + ChatColor.ITALIC + " ] " + ChatColor.GRAY + ChatColor.ITALIC;
    public static final String Warn_Format = ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.ITALIC + "[ " + ChatColor.YELLOW + ChatColor.ITALIC + "!" + ChatColor.WHITE + ChatColor.ITALIC + " ] " + ChatColor.GRAY + ChatColor.ITALIC;
    public static final String Error_Format = ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.ITALIC + "[ " + ChatColor.RED + ChatColor.ITALIC + "!" + ChatColor.WHITE + ChatColor.ITALIC + " ] " + ChatColor.GRAY + ChatColor.ITALIC;
    public static final String Console = "[EvaTunaSkillCommand] ";

    public static void sendActionBar(Player player, String message){
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        p.getHandle().playerConnection.sendPacket(ppoc);
    }

}
