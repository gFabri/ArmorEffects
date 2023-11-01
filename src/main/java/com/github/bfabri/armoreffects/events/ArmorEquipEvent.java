package com.github.bfabri.armoreffects.events;

import com.github.bfabri.armoreffects.utils.ArmorType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public final class ArmorEquipEvent extends PlayerEvent implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  
  private boolean cancel = false;
  
  private final EquipMethod equipType;
  
  private final ArmorType type;
  
  private ItemStack oldArmorPiece;
  
  private ItemStack newArmorPiece;
  
  public ArmorEquipEvent(Player player, EquipMethod equipType, ArmorType type, ItemStack oldArmorPiece, ItemStack newArmorPiece) {
    super(player);
    this.equipType = equipType;
    this.type = type;
    this.oldArmorPiece = oldArmorPiece;
    this.newArmorPiece = newArmorPiece;
  }
  
  public static final HandlerList getHandlerList() {
    return handlers;
  }
  
  public final HandlerList getHandlers() {
    return handlers;
  }
  
  public final void setCancelled(boolean cancel) {
    this.cancel = cancel;
  }
  
  public final boolean isCancelled() {
    return this.cancel;
  }
  
  public final ArmorType getType() {
    return this.type;
  }
  
  public final ItemStack getOldArmorPiece() {
    return this.oldArmorPiece;
  }
  
  public final void setOldArmorPiece(ItemStack oldArmorPiece) {
    this.oldArmorPiece = oldArmorPiece;
  }
  
  public final ItemStack getNewArmorPiece() {
    return this.newArmorPiece;
  }
  
  public final void setNewArmorPiece(ItemStack newArmorPiece) {
    this.newArmorPiece = newArmorPiece;
  }
  
  public EquipMethod getMethod() {
    return this.equipType;
  }
  
  public enum EquipMethod {
    SHIFT_CLICK, DRAG, PICK_DROP, HOTBAR, HOTBAR_SWAP, DISPENSER, BROKE, DEATH;
  }
}
