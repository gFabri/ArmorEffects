package com.github.bfabri.armoreffects;

import com.github.bfabri.armoreffects.commands.utils.CommandsModule;
import com.github.bfabri.armoreffects.commands.utils.framework.SimpleCommandManager;
import com.github.bfabri.armoreffects.listeners.ArmorListener;
import com.github.bfabri.armoreffects.listeners.EffectListener;
import com.github.bfabri.armoreffects.listeners.EnchantBookListener;
import com.github.bfabri.armoreffects.utils.UpdateChecker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class ArmorEffects extends JavaPlugin {

    @Getter
    public static ArrayList<String> listOfPotions = new ArrayList<>();

    @Getter
    private static ArmorEffects instance;

    public void onEnable() {
        instance = this;
        new ConfigHandler(this);
        (new UpdateChecker()).checkForUpdate();
        PotionEffectType[] typesPot = PotionEffectType.values();
        if (listOfPotions.isEmpty()) {
            for (PotionEffectType type : typesPot) {
                if (type != null)
                    listOfPotions.add(type.getName());
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lArmorEffects&8] &bRegistered &3" + listOfPotions.size() + " &bEffects"));
        }
        registerCommands();
        registerListeners();
    }

    public void onDisable() {
        instance = null;
    }

    private void registerListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new EffectListener(), this);
        manager.registerEvents(new ArmorListener(), this);
        manager.registerEvents(new EnchantBookListener(), this);
    }

    private void registerCommands() {
        new SimpleCommandManager(this).registerAll(new CommandsModule());
    }
}
