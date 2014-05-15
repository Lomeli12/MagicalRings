package net.lomeli.modjam4.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class MagicHandler {
    private List<PlayerMagic> playerMG = new ArrayList<PlayerMagic>();
    
    public MagicHandler(){
        
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