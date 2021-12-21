package me.trade.vivace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TradeInventoryManager {

    public final static int[] p1Slots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
    public final static int[] p2Slots = {14, 15, 16, 23, 24, 25, 32, 33, 34};

    public final static int P1ACCEPT_SLOT = 18;
    public final static int P2ACCEPT_SLOT = 26;

    Player p1, p2;
    Inventory tradeInventory;
    String inventoryName;

    boolean p1Agree, p2Agree = false;

    public TradeInventoryManager(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.inventoryName = ChatColor.GOLD + "[ " + ChatColor.WHITE + "거래 : " + getPlayerNames() + ChatColor.GOLD + " ]";

        this.tradeInventory = Bukkit.createInventory(null, 45, this.inventoryName);

        for (int slotX = 0; slotX < 9; slotX++) {
            for (int slotY = 0; slotY < 5; slotY++) {
                int nowSlot = slotX + slotY * 9;

                if ((1 <= slotY && slotY <= 3) &&
                        ( (1 <= slotX && slotX <= 3) ||
                                (5 <= slotX && slotX <= 7) )) {
                    continue;
                }

                this.tradeInventory.setItem(nowSlot, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
            }
        }

        p1.openInventory(this.tradeInventory);
        p2.openInventory(this.tradeInventory);

        this.tradeInventory.setItem(P1ACCEPT_SLOT, new ItemStackBuilder(Material.RED_GLAZED_TERRACOTTA)
                .setName(ChatColor.GOLD + "[ " + p1.getName() + " ] " + ChatColor.GRAY + "거래 대기").build());
        this.tradeInventory.setItem(P2ACCEPT_SLOT, new ItemStackBuilder(Material.RED_GLAZED_TERRACOTTA)
                .setName(ChatColor.GOLD + "[ " + p2.getName() + " ] " + ChatColor.GRAY + "거래 대기").build());
    }

    public static boolean isp1Location(int slot) {
        return Arrays.stream(p1Slots).anyMatch(i -> i == slot);
    }

    public static boolean isp2Location(int slot) {
        return Arrays.stream(p2Slots).anyMatch(i -> i == slot);
    }

    public String getPlayerNames() {
        return p1.getName() + " | " + p2.getName();
    }
}
