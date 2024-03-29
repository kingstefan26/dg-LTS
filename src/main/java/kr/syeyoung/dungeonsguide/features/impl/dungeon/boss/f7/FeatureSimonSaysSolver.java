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

package kr.syeyoung.dungeonsguide.features.impl.dungeon.boss.f7;

import kr.syeyoung.dungeonsguide.dungeon.DungeonContext;
import kr.syeyoung.dungeonsguide.dungeon.DungeonFacade;
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.bossfight.BossfightProcessorNecron;
import kr.syeyoung.dungeonsguide.features.SimpleFeatureV2;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import kr.syeyoung.dungeonsguide.utils.BlockCache;
import kr.syeyoung.dungeonsguide.utils.RenderUtils;
import kr.syeyoung.dungeonsguide.utils.SkyblockStatus;
import kr.syeyoung.dungeonsguide.utils.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.joml.Vector3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FeatureSimonSaysSolver extends SimpleFeatureV2 {
    private final List<Vector3i> orderbuild = new ArrayList<>();
    private final LinkedList<Vector3i> orderclick = new LinkedList<>();
    private boolean wasButton = false;

    public FeatureSimonSaysSolver() {
        super("Dungeon.Bossfight.simonsays2");
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (!SkyblockStatus.isOnSkyblock()) return;
        if (!DgOneCongifConfig.simonySaysSolver) return;

        DungeonContext dc = DungeonFacade.context;
        if (dc == null) return;
        if (!(dc.bossfightProcessor instanceof BossfightProcessorNecron)) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        BlockPos pos = event.pos.add(1, 0, 0);
        if (120 <= pos.getY() && pos.getY() <= 123 && pos.getX() == 310 && 291 <= pos.getZ() && pos.getZ() <= 294) {
            if (BlockCache.getBlockState(event.pos).getBlock() != Blocks.stone_button)
                return;
            if (pos.equals(orderclick.peek())) {
                orderclick.poll();
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.type != TickEvent.Type.CLIENT) {
            return;
        }
        if (!SkyblockStatus.isOnSkyblock()) return;
        DungeonContext dc = DungeonFacade.context;
        if (dc == null) {
            wasButton = false;
            return;
        }
        if (!(dc.bossfightProcessor instanceof BossfightProcessorNecron)) return;

        if (wasButton && BlockCache.getBlockState(new BlockPos(309, 123, 291)).getBlock() == Blocks.air) {
            orderclick.clear();
            orderbuild.clear();
            wasButton = false;
        } else if (!wasButton && BlockCache.getBlockState(new BlockPos(309, 123, 291)).getBlock() == Blocks.stone_button) {
            orderclick.addAll(orderbuild);
            wasButton = true;
        }


        if (!wasButton) {
            for (BlockPos allInBox : BlockPos.getAllInBox(new BlockPos(310, 123, 291), new BlockPos(310, 120, 294))) {
                if (BlockCache.getBlockState(allInBox).getBlock() == Blocks.sea_lantern && !orderbuild.contains(allInBox)) {
                    orderbuild.add(VectorUtils.BlockPosToVec3i(allInBox));
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent e) {
        if (!SkyblockStatus.isOnSkyblock()) return;
        if (!DgOneCongifConfig.simonySaysSolver) return;
        DungeonContext dc = DungeonFacade.context;
        if (dc == null) {
            return;
        }
        if (!(dc.bossfightProcessor instanceof BossfightProcessorNecron)) return;
        if (Minecraft.getMinecraft().thePlayer.getPosition().distanceSq(309, 123, 291) > 400) return;


        if (orderclick.size() >= 1)
            RenderUtils.highlightBlock(orderclick.get(0), new Color(0, 255, 255, 100), e.partialTicks, false);
        if (orderclick.size() >= 2)
            RenderUtils.highlightBlock(orderclick.get(1), new Color(255, 170, 0, 100), e.partialTicks, false);
    }

}
