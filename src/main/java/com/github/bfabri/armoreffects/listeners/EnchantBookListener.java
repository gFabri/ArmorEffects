package com.github.bfabri.armoreffects.listeners;

import java.util.ArrayList;
import java.util.List;

import com.github.bfabri.armoreffects.ArmorEffects;
import com.github.bfabri.armoreffects.ConfigHandler;
import com.github.bfabri.armoreffects.events.ArmorEquipEvent;
import com.github.bfabri.armoreffects.utils.ArmorType;
import com.github.bfabri.armoreffects.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantBookListener implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    ItemStack item = event.getCurrentItem();

    if (ArmorType.match(item) && isEffectBook(event.getCursor())) {
      event.setCancelled(true);
      ItemStack newItem = mergeItemWithBook(event.getCursor(), item);
      player.sendMessage(Utils.translate(ConfigHandler.Configs.LANG.getConfig().getString("EFFECT_BOOK_ADDED").replace("{effect}", ChatColor.stripColor(event.getCursor().getItemMeta().getLore().get(0)))));
      event.setCurrentItem(newItem);
      player.setItemOnCursor(new ItemStack(Material.AIR));

      if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
        ArmorEquipEvent ev = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DRAG, ArmorType.matchType(item), new ItemStack(Material.AIR), newItem);
        Bukkit.getPluginManager().callEvent(ev);
      }
    }
  }

  private ItemStack mergeItemWithBook(ItemStack book, ItemStack current) {
    ItemMeta currentMeta = current.getItemMeta();
    ItemMeta bookMeta = book.getItemMeta();

    List<String> lores = new ArrayList<>();

    if (currentMeta != null && currentMeta.hasLore()) {
      lores.addAll(currentMeta.getLore());
    }

    if (bookMeta != null && bookMeta.hasLore()) {
      lores.addAll(bookMeta.getLore());
    }

    ItemMeta mergedMeta = currentMeta != null ? currentMeta.clone() : bookMeta.clone();
    mergedMeta.setLore(lores);

    current.setItemMeta(mergedMeta);

    return current;
  }


  private boolean isEffectBook(ItemStack book) {
    if (book.getType() == Material.ENCHANTED_BOOK) {
      if (book.hasItemMeta() && book.getItemMeta().hasLore()) {
        for (String s : ArmorEffects.getListOfPotions()) {
          for (String lines : book.getItemMeta().getLore()) {
            if (ChatColor.stripColor(lines.toLowerCase()).startsWith(s.toLowerCase().replace("_", " "))) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
}
