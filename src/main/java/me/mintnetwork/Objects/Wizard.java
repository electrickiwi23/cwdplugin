package me.mintnetwork.Objects;

import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Wizard {

    Player player = null;
    public int Mana = 0;
    public int ManaTick = 0;
    public int Ult = 0;
    public Kit kitID = Kit.NONE;
    public int PassiveTick = 0;

    public ArrayList<ItemStack> wands = new ArrayList<>();
    public FallingBlock thrownAnvil = null;
    public ArrayList<Block> HealPillars = new ArrayList<>();

    public int ElimLives = 0;
    public Wizard(Player p){
        player = p;
    }




}
