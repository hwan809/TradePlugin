package me.trade.vivace;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemStackBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemStackBuilder setType(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemStackBuilder setName(String name) {
        itemMeta.setDisplayName(ColorString.colored(name));
        return this;
    }

    public ItemStackBuilder setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemStackBuilder addLore(String lore) {
        List<String> currentLore = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
        currentLore.add(ColorString.colored(lore));
        itemMeta.setLore(currentLore);
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder setUnBreakAble(boolean value) {
        itemMeta.setUnbreakable(value);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
