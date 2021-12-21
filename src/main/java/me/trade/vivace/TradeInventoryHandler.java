package me.trade.vivace;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeInventoryHandler implements Listener {
    public static List<TradeInventoryManager> tradeInventory = new ArrayList<>();

    @EventHandler
    public void clickTradeInventory(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        String inventoryTitle = event.getView().getTitle();
        TradeInventoryManager nowTradeInventory = null;

        for (TradeInventoryManager tim : tradeInventory) {
            if (tim.inventoryName.equals(inventoryTitle)) {
                nowTradeInventory = tim;
            }
        }

        if (nowTradeInventory == null) return;
        if (!event.getClickedInventory().getTitle().equals(nowTradeInventory.inventoryName)) return;

        Player player = (Player) event.getWhoClicked();
        boolean isPlayerP1 = nowTradeInventory.p1.equals(player);
        int slot = event.getSlot();

        if (
                (isPlayerP1 && TradeInventoryManager.isp1Location(slot)) ||
                        (!isPlayerP1 && TradeInventoryManager.isp2Location(slot))
        ) {
            return;
        } else if (
                (isPlayerP1 && TradeInventoryManager.isp2Location(slot)) ||
                        (!isPlayerP1 && TradeInventoryManager.isp1Location(slot))
        ) {
            logMessage(player, ChatColor.RED + "상대편 아이템창을 수정할 수 없습니다.");
            event.setCancelled(true);
            return;
        }

        if (isPlayerP1 && slot == TradeInventoryManager.P1ACCEPT_SLOT) {
            nowTradeInventory.p1Agree = true;
            nowTradeInventory.tradeInventory.setItem(
                    TradeInventoryManager.P1ACCEPT_SLOT, new ItemStackBuilder(Material.GREEN_GLAZED_TERRACOTTA)
                            .setName(ChatColor.GOLD + "[ " + nowTradeInventory.p1.getName() + " ] " + ChatColor.GREEN + "수락").build()
            );
            event.setCancelled(true);
        }

        if (!isPlayerP1 && slot == TradeInventoryManager.P2ACCEPT_SLOT) {
            nowTradeInventory.p2Agree = true;
            nowTradeInventory.tradeInventory.setItem(
                    TradeInventoryManager.P2ACCEPT_SLOT, new ItemStackBuilder(Material.GREEN_GLAZED_TERRACOTTA)
                            .setName(ChatColor.GOLD + "[ " + nowTradeInventory.p2.getName() + " ] " + ChatColor.GREEN + "수락").build()
            );
            event.setCancelled(true);
        }

        if (nowTradeInventory.p1Agree && nowTradeInventory.p2Agree) {
            nowTradeInventory.p1.closeInventory();
            nowTradeInventory.p2.closeInventory();

            logMessage(nowTradeInventory.p1, ChatColor.YELLOW + nowTradeInventory.p2.getName() +
                    ChatColor.GRAY + "님과의 거래가 성사되었습니다.");
            logMessage(nowTradeInventory.p2, ChatColor.YELLOW + nowTradeInventory.p1.getName() +
                    ChatColor.GRAY + "님과의 거래가 성사되었습니다.");

            for (int s = 0; s < 45; s++) {
                ItemStack nowItemStack = nowTradeInventory.tradeInventory.getItem(s);

                if (nowItemStack == null) continue;
                if (nowItemStack.getType() == Material.AIR) continue;

                if (TradeInventoryManager.isp1Location(s)) {
                    nowTradeInventory.p2.getInventory().addItem(nowItemStack);
                } else if (TradeInventoryManager.isp2Location(s)) {
                    nowTradeInventory.p1.getInventory().addItem(nowItemStack);
                }
            }

            tradeInventory.remove(nowTradeInventory);
        }

        event.setCancelled(true);
    }

    public void logMessage(Player p, String message) {
        p.sendMessage(ChatColor.GOLD + "[ 거래 ] " + ChatColor.WHITE + message);
    }
}
