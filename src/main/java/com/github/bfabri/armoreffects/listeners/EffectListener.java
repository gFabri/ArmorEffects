package com.github.bfabri.armoreffects.listeners;

import com.github.bfabri.armoreffects.ArmorEffects;
import com.github.bfabri.armoreffects.events.ArmorEquipEvent;
import com.github.bfabri.armoreffects.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack oldPiece = event.getOldArmorPiece();
        ItemStack newPiece = event.getNewArmorPiece();

        handleRemovedEffects(event.getPlayer(), oldPiece);
        handleAddedEffects(event.getPlayer(), newPiece);
    }

    private void handleRemovedEffects(Player player, ItemStack armorPiece) {
        if (armorPiece != null && armorPiece.hasItemMeta() && armorPiece.getItemMeta().hasLore()) {
            armorPiece.getItemMeta().getLore().stream()
                    .map(ChatColor::stripColor)
                    .forEach(line -> ArmorEffects.getListOfPotions().stream()
                            .filter(s -> line.toLowerCase().startsWith(s.toLowerCase().replace("_", " ")))
                            .findFirst()
                            .ifPresent(potion -> {
                                String[] split = line.split(" ");
                                String potionName = ChatColor.stripColor(split[0]);

                                if (split.length == 3) {
                                    potionName += "_" + ChatColor.stripColor(split[1]);
                                }

                                player.removePotionEffect(PotionEffectType.getByName(potionName));
                            }));
        }
    }

    private void handleAddedEffects(Player player, ItemStack armorPiece) {
        if (armorPiece != null && armorPiece.hasItemMeta() && armorPiece.getItemMeta().hasLore()) {
            armorPiece.getItemMeta().getLore().stream()
                    .map(ChatColor::stripColor)
                    .forEach(line -> ArmorEffects.getListOfPotions().stream()
                            .filter(s -> line.toLowerCase().startsWith(s.toLowerCase().replace("_", " ")))
                            .findFirst()
                            .ifPresent(potion -> {
                                String[] split = line.split(" ");
                                String potionName = ChatColor.stripColor(split[0]);
                                int level = split.length == 3 ? Utils.getLevel(ChatColor.stripColor(split[2])) : Utils.getLevel(ChatColor.stripColor(split[1]));

                                if (split.length == 3) {
                                    potionName += "_" + ChatColor.stripColor(split[1]);
                                }

                                if (player.hasPermission("armoreffect.noparticle")) {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionName), Integer.MAX_VALUE, level - 1, false, false));
                                } else {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionName), Integer.MAX_VALUE, level - 1));
                                }
                            }));
        }
    }
}
