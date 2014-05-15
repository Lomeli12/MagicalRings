package net.lomeli.modjam4.magic;

import java.util.ArrayList;
import java.util.List;

import net.lomeli.modjam4.Rings;
import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;

public class MagicHandler {
    private List<PlayerMagic> playerMG = new ArrayList<PlayerMagic>();
    private List<ISpell> registeredSpells = new ArrayList<ISpell>();
    
    public MagicHandler(){
        this.registerSpell(new DebugSpell());
    }
    
    public static MagicHandler getMagicHandler(){
        return Rings.proxy.magicHandler;
    }
    
    public static ISpell getSpellLazy(int index) {
        return getMagicHandler().getSpell(index);
    }
    
    public static boolean canUse(EntityPlayer player, int cost) {
        boolean can = false;
        PlayerMagic pm = getMagicHandler().getPlayer(player);
        if (pm != null)
            can = pm.getMP() >= cost;
        return can;
    }
    
    public static PlayerMagic getPlayerData(String uuid) {
        return getMagicHandler().getPlayer(uuid);
    }
    
    public void registerSpell(ISpell spell) {
        if (this.registeredSpells.contains(spell))
            return;
        this.registeredSpells.add(spell);
    }
    
    public ISpell getSpell(int index) {
        if (index < this.registeredSpells.size())
            return this.registeredSpells.get(index);
        return null;
    }
    
    public void updatePlayerMP(EntityPlayer player, int amount) {
        PlayerMagic pl = getPlayer(player);
        if (pl != null)
            pl.updateMP(amount);
    }
    
    public void givePlayerMagicSession(EntityPlayer player) {
        PlayerMagic pl = new PlayerMagic(player, 0, ModLibs.BASE_MP);
        if (!this.playerMG.contains(pl))
            this.playerMG.add(pl);
    }
    
    public PlayerMagic getPlayer(EntityPlayer player) {
        String uuid = player.getGameProfile().getId();
        return getPlayer(uuid);
    }
    
    public PlayerMagic getPlayer(String uuid) {
        for(PlayerMagic m : playerMG) {
            if (m.player().getGameProfile().getId().equals(uuid))
                return m;
        }
        return null;
    }
    
    public List<PlayerMagic> getList() {
        return playerMG;
    }
}