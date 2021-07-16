package me.mintnetwork.Objects;

import me.mintnetwork.spells.*;

public enum Kit {
    ALCHEMIST(new Alchemist()),
    BARD(new Bard()),
    BERSERKER(new Berserker()),
    BLOOD_MAGE(new BloodMage()),
    BUILDER(new Builder()),
    CLERIC(new Cleric()),
    AVIATOR(new Aviator()),
    DEMOLITIONIST(new Demolitionist()),
    PAINTER(new Painter()),
//    PILLAR_MAN(new PillarMan()),
    SHADOW(new Shadow()),
    SPELL_SLINGER(new SpellSlinger()),
    TACTICIAN(new Tactician()),
    PROTECTOR(new Protector()),
    NONE(null);

    public final KitItems KitItems;


    Kit(KitItems aKitItems) {
        this.KitItems = aKitItems;
    }
}
