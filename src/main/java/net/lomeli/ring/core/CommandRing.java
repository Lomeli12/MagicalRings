package net.lomeli.ring.core;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.Rings;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketAdjustClientPos;
import net.lomeli.ring.network.PacketManaHud;

public class CommandRing extends CommandBase {

    @Override
    public String getCommandName() {
        return "rings";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + this.getCommandName() + " help";
    }

    @Override
    public void processCommand(ICommandSender cSender, String[] args) {
        if (args.length >= 1) {
            String command = args[0];
            EntityPlayer player = cSender.getEntityWorld().getPlayerEntityByName(cSender.getCommandSenderName());
            if (command.equalsIgnoreCase("manaDisplay") && player != null) {
                if (args.length >= 3) {
                    int x = parseString(args[1]), y = parseString(args[2]);

                    if (x < 0)
                        x = ModLibs.DISPLAY_X;
                    if (y < 0)
                        y = ModLibs.DISPLAY_Y;

                    if (player != null)
                        Rings.pktHandler.sendTo(new PacketAdjustClientPos(x, y), player);
                } else
                    this.sendMessage(cSender, "\u00a7c/rings manaDisplay [xPosition] [yPosition]");
            } else if (command.equalsIgnoreCase("enableManaHUD") && player != null) {
                Rings.pktHandler.sendToPlayerAndServer(new PacketManaHud(player), player);
                boolean display = SimpleUtil.displayHud(player);
                String message;
                if (display)
                    message = ModLibs.COMMAND_DISPLAY_ON;
                else
                    message = ModLibs.COMMAND_DISPLAY_OFF;
                this.sendMessage(cSender, StatCollector.translateToLocal(message));
            } else if (command.equalsIgnoreCase("help") || command.equalsIgnoreCase("?")) {
                this.sendMessage(cSender, "/rings manaDisplay [xPosition] [yPosition] - " + StatCollector.translateToLocal(ModLibs.COMMAND_MANA_POS));
                this.sendMessage(cSender, "/rings enableManaHUD - " + StatCollector.translateToLocal(ModLibs.COMMAND_DISPLAY_MANA));
            } else
                this.sendMessage(cSender, StatCollector.translateToLocal("magiaclrings.command.unknown") + " " + StatCollector.translateToLocal(ModLibs.COMMAND_HELP));
        } else
            this.sendMessage(cSender, StatCollector.translateToLocal(ModLibs.COMMAND_HELP));
    }

    private void sendMessage(ICommandSender cSender, String msg) {
        if (cSender != null)
            cSender.addChatMessage(new ChatComponentText(msg));
    }

    private int parseString(String msg) {
        try {
            return Integer.parseInt(msg);
        } catch (Exception e) {
        }
        return -1;
    }

}
