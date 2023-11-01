package com.github.bfabri.armoreffects.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum ArmorType {
  HELMET(5),
  CHESTPLATE(6),
  LEGGINGS(7),
  BOOTS(8);

  private final int slot;

  ArmorType(int slot) {
    this.slot = slot;
  }

  public static ArmorType matchType(ItemStack itemStack) {
    if (itemStack == null || itemStack.getType().equals(Material.AIR))
      return null;
    String type = itemStack.getType().name();
    if (type.endsWith("_HELMET") || type.endsWith("_SKULL") || type.startsWith("SKULL_") || type.endsWith("_helmet") || type.endsWith("_HEAD") || type.endsWith("_head"))
      return HELMET;
    if (type.endsWith("_CHESTPLATE") || type.endsWith("_chestplate") || type.equals("ELYTRA") || type.equals("elytra"))
      return CHESTPLATE;
    if (type.endsWith("_LEGGINGS") || type.endsWith("_leggings"))
      return LEGGINGS;
    if (type.endsWith("_BOOTS") || type.endsWith("_boots"))
      return BOOTS;
    return null;
  }

  public static boolean match(ItemStack itemStack) {
    return matchType(itemStack) != null;
  }

}
