package me.mintnetwork.spells;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public abstract class KitItems {
    public ArrayList<ItemStack> wands = new ArrayList<>();
    public ItemStack ult = new ItemStack(Material.DIAMOND_HOE);
    public ItemStack passive = new ItemStack(Material.PAPER);
    public ItemStack menuItem = new ItemStack(Material.FIRE);
}
