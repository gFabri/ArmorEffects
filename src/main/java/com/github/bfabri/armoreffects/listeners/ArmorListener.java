package com.github.bfabri.armoreffects.listeners;

import com.github.bfabri.armoreffects.events.ArmorEquipEvent;
import com.github.bfabri.armoreffects.utils.ArmorType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {
  private final List<String> blockedMaterials = getBlocks();

  @EventHandler
  public final void inventoryClick(InventoryClickEvent e) {
    boolean shift = false, numberkey = false;
    if (e.isCancelled())
      return;
    if (e.getAction() == InventoryAction.NOTHING)
      return;
    if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT))
      shift = true;
    if (e.getClick().equals(ClickType.NUMBER_KEY))
      numberkey = true;
    if (e.getSlotType() != InventoryType.SlotType.ARMOR && e.getSlotType() != InventoryType.SlotType.QUICKBAR && e.getSlotType() != InventoryType.SlotType.CONTAINER)
      return;
    if (e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER))
      return;
    if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER))
      return;
    if (!(e.getWhoClicked() instanceof Player))
      return;
    ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
    if (!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot())
      return;
    if (shift) {
      newArmorType = ArmorType.matchType(e.getCurrentItem());
      if (newArmorType != null) {
        boolean equipping = true;
        if (e.getRawSlot() == newArmorType.getSlot())
          equipping = false;
        if ((newArmorType.equals(ArmorType.HELMET) && equipping == isAirOrNull(e.getWhoClicked().getInventory().getHelmet())) || (newArmorType.equals(ArmorType.CHESTPLATE) && equipping == isAirOrNull(e.getWhoClicked().getInventory().getChestplate())) || (newArmorType.equals(ArmorType.LEGGINGS) && equipping == isAirOrNull(e.getWhoClicked().getInventory().getLeggings())) || (newArmorType.equals(ArmorType.BOOTS) && equipping == isAirOrNull(e.getWhoClicked().getInventory().getBoots()))) {
          ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)e.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
          Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
          if (armorEquipEvent.isCancelled())
            e.setCancelled(true);
        }
      }
    } else {
      ItemStack newArmorPiece = e.getCursor();
      ItemStack oldArmorPiece = e.getCurrentItem();
      if (numberkey) {
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
          ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
          if (!isAirOrNull(hotbarItem)) {
            newArmorType = ArmorType.matchType(hotbarItem);
            newArmorPiece = hotbarItem;
            oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
          } else {
            newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
          }
        }
      } else if (isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem())) {
        newArmorType = ArmorType.matchType(e.getCurrentItem());
      }
      if (newArmorType != null && e.getRawSlot() == newArmorType.getSlot()) {
        ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
        if (e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey)
          method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
        ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)e.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
        Bukkit.getServer().getPluginManager().callEvent((Event)armorEquipEvent);
        if (armorEquipEvent.isCancelled())
          e.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void playerInteractEvent(PlayerInteractEvent e) {
    if (e.getAction() == Action.PHYSICAL)
      return;
    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      Player player = e.getPlayer();
      if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
        Material mat = e.getClickedBlock().getType();
        for (String s : this.blockedMaterials) {
          if (mat.name().equalsIgnoreCase(s))
            return;
        }
      }
      ArmorType newArmorType = ArmorType.matchType(e.getItem());
      if (newArmorType != null && ((newArmorType
        .equals(ArmorType.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet())) || (newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate())) || (newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings())) || (newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots())))) {
        ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
        Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
        if (armorEquipEvent.isCancelled()) {
          e.setCancelled(true);
          player.updateInventory();
        }
      }
    }
  }

  @EventHandler
  public void inventoryDrag(InventoryDragEvent event) {
    ArmorType type = ArmorType.matchType(event.getOldCursor());
    if (event.getRawSlots().isEmpty())
      return;
    if (type != null && type.getSlot() == ((Integer)event.getRawSlots().stream().findFirst().orElse(Integer.valueOf(0))).intValue()) {
      ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
      Bukkit.getServer().getPluginManager().callEvent((Event)armorEquipEvent);
      if (armorEquipEvent.isCancelled()) {
        event.setResult(Event.Result.DENY);
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void itemBreakEvent(PlayerItemBreakEvent e) {
    ArmorType type = ArmorType.matchType(e.getBrokenItem());
    if (type != null) {
      Player p = e.getPlayer();
      ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
      Bukkit.getServer().getPluginManager().callEvent((Event)armorEquipEvent);
      if (armorEquipEvent.isCancelled()) {
        ItemStack i = e.getBrokenItem().clone();
        i.setAmount(1);
        i.setDurability((short)(i.getDurability() - 1));
        if (type.equals(ArmorType.HELMET)) {
          p.getInventory().setHelmet(i);
        } else if (type.equals(ArmorType.CHESTPLATE)) {
          p.getInventory().setChestplate(i);
        } else if (type.equals(ArmorType.LEGGINGS)) {
          p.getInventory().setLeggings(i);
        } else if (type.equals(ArmorType.BOOTS)) {
          p.getInventory().setBoots(i);
        }
      }
    }
  }

  @EventHandler
  public void playerDeathEvent(PlayerDeathEvent e) {
    Player p = e.getEntity();
    for (ItemStack i : p.getInventory().getArmorContents()) {
      if (!isAirOrNull(i))
        Bukkit.getServer().getPluginManager().callEvent((Event)new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
    }
  }

  private boolean isAirOrNull(ItemStack item) {
    return (item == null || item.getType().equals(Material.AIR));
  }
  
  private List<String> getBlocks() {
    List<String> blocks = new ArrayList<>();
    blocks.add("DAYLIGHT_DETECTOR");
    blocks.add("DAYLIGHT_DETECTOR_INVERTED");
    blocks.add("FURNACE");
    blocks.add("CHEST");
    blocks.add("TRAPPED_CHEST");
    blocks.add("BEACON");
    blocks.add("DISPENSER");
    blocks.add("DROPPER");
    blocks.add("HOPPER");
    blocks.add("WORKBENCH");
    blocks.add("ENCHANTMENT_TABLE");
    blocks.add("ENDER_CHEST");
    blocks.add("ANVIL");
    blocks.add("BED_BLOCK");
    blocks.add("FENCE_GATE");
    blocks.add("SPRUCE_FENCE_GATE");
    blocks.add("BIRCH_FENCE_GATE");
    blocks.add("ACACIA_FENCE_GATE");
    blocks.add("JUNGLE_FENCE_GATE");
    blocks.add("DARK_OAK_FENCE_GATE");
    blocks.add("IRON_DOOR_BLOCK");
    blocks.add("WOODEN_DOOR");
    blocks.add("SPRUCE_DOOR");
    blocks.add("BIRCH_DOOR");
    blocks.add("JUNGLE_DOOR");
    blocks.add("ACACIA_DOOR");
    blocks.add("DARK_OAK_DOOR");
    blocks.add("WOOD_BUTTON");
    blocks.add("STONE_BUTTON");
    blocks.add("TRAP_DOOR");
    blocks.add("IRON_TRAPDOOR");
    blocks.add("DIODE_BLOCK_OFF");
    blocks.add("DIODE_BLOCK_ON");
    blocks.add("REDSTONE_COMPARATOR_OFF");
    blocks.add("REDSTONE_COMPARATOR_ON");
    blocks.add("FENCE");
    blocks.add("SPRUCE_FENCE");
    blocks.add("BIRCH_FENCE");
    blocks.add("JUNGLE_FENCE");
    blocks.add("DARK_OAK_FENCE");
    blocks.add("ACACIA_FENCE");
    blocks.add("NETHER_FENCE");
    blocks.add("BREWING_STAND");
    blocks.add("CAULDRON");
    blocks.add("SIGN_POST");
    blocks.add("WALL_SIGN");
    blocks.add("SIGN");
    blocks.add("DRAGON_EGG");
    blocks.add("LEVER");
    blocks.add("SHULKER_BOX");
    return blocks;
  }
}
