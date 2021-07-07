package me.mintnetwork.Objects;

import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Wizard {

    Player player = null;
    public int Mana = 0;
    public int ManaTick = 0;
    public int Ult = 0;
    public String ClassID = "";
    public int PassiveTick = 0;

    public FallingBlock thrownAnvil = null;

    public int ElimLives = 0;
    ArrayList<Integer> ChosenSpells = new ArrayList<>();

    public Wizard(Player p){
        player = p;
    }




}
