/*
 *     Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 *     Copyright (C) 2021  cyoung06
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package kr.syeyoung.dungeonsguide.commands;

import kr.syeyoung.dungeonsguide.DungeonsGuide;
import kr.syeyoung.dungeonsguide.chat.ChatTransmitter;
import kr.syeyoung.dungeonsguide.config.guiconfig.GuiConfigV2;
import kr.syeyoung.dungeonsguide.cosmetics.CosmeticsManager;
import kr.syeyoung.dungeonsguide.features.impl.misc.playerpreview.FeatureViewPlayerStatsOnJoin;
import kr.syeyoung.dungeonsguide.features.impl.misc.playerpreview.api.ApiFetcher;
import kr.syeyoung.dungeonsguide.party.PartyManager;
import kr.syeyoung.dungeonsguide.utils.simple.SimpleLock;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandDungeonsGuide extends CommandBase {
    private boolean openConfig = false;


    private SimpleLock openOldLock = new SimpleLock();

    {
        openOldLock.lock();
    }

    @Override
    public String getCommandName() {
        return "dg";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "dg";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            openConfig = true;
        } else if (args[0].equalsIgnoreCase("reparty")) {
            if (!DungeonsGuide.getDungeonsGuide().getCommandReparty().requestReparty(false)) {
                ChatTransmitter.addToQueue(new ChatComponentText("§eDungeons Guide §7:: §cCurrently Repartying"));
            }
        } else if (args[0].equalsIgnoreCase("gui")) {
            openConfig = true;

        } else if (args[0].equalsIgnoreCase("old")) {
            openOldLock.unLock();
        } else if (args[0].equalsIgnoreCase("pvall")) {
            PartyManager.INSTANCE.requestPartyList((context) -> {
                if (context == null) {
                    ChatTransmitter.addToQueue(new ChatComponentText("§eDungeons Guide §7:: §cNot in Party"));
                    return;
                }
                FeatureViewPlayerStatsOnJoin.processPartyMembers(context);
            });
        } else if (args[0].equals("pv")) {
            try {
                ApiFetcher.fetchUUIDAsync(args[1])
                        .thenAccept(a -> {
                            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e" + args[1] + "§f's Profile ").appendSibling(new ChatComponentText("§7view").setChatStyle(new ChatStyle().setChatHoverEvent(new FeatureViewPlayerStatsOnJoin.HoverEventRenderPlayer(a.orElse(null))))));
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args[0].equals("purge")) {
            ApiFetcher.purgeCache();
            CosmeticsManager cosmeticsManager = DungeonsGuide.getDungeonsGuide().getCosmeticsManager();
            cosmeticsManager.requestPerms();
            cosmeticsManager.requestCosmeticsList();
            cosmeticsManager.requestActiveCosmetics();

            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §fSuccessfully purged API Cache!"));
        } else if (args[0].equals("partymax") || args[0].equals("pm")) {
            if (args.length == 1) {
                sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §fCurrent party max is §e" + PartyManager.INSTANCE.getMaxParty()));
            } else if (args.length == 2) {
                try {
                    int partyMax = Integer.parseInt(args[1]);
                    if (partyMax < 2) {
                        sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §cparty max can't be smaller than 2"));
                        return;
                    }

                    PartyManager.INSTANCE.setMaxParty(partyMax);
                    sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §fSuccessfully set partymax to §e" + PartyManager.INSTANCE.getMaxParty()));
                } catch (Exception e) {
                    sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §c" + args[1] + " is not valid number."));
                }
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg §7-§fOpens configuration gui"));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg gui §7-§fOpens configuration gui"));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg help §7-§fShows command help"));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg reloadah §7-§f Reloads price data from server."));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg reparty §7-§f Reparty."));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg asktojoin or /dg atj §7-§f Toggle ask to join §cRequires Discord Rich Presence enabled. (/dg -> Advanced)"));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg partymax [number] or /dg pm [number] §7-§f Sets partymax §7(maximum amount people in party, for discord rpc)"));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg pv [ign] §7-§f Profile Viewer"));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg pvall §7-§f Profile Viewer For all people on party"));
            sender.addChatMessage(new ChatComponentText("§eDungeons Guide §7:: §e/dg purge §7-§f Purge api cache."));
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        try {
            if(!openOldLock.isLocked()) {
                openOldLock.lock();
                Minecraft.getMinecraft().displayGuiScreen(new GuiConfigV2());
            }
            if (openConfig && e.phase == TickEvent.Phase.START) {
                openConfig = false;

                DungeonsGuide.config.openGui();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
