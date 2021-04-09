package me.mintnetwork.Objects;

import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Wizard {

    Player player = null;
    public int Mana = 0;
    public int ManaTick = 0;
    public int Ult = 0;
    public String ClassID = "";
    public int PassiveTick = 0;

    public int ElimLives = 0;
    ArrayList<Integer> ChosenSpells = new ArrayList<>();

    public Wizard(Player p){
        player = p;
    }




}
