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

package kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.solvers.bombdefuse.chambers.maze;

import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.solvers.bombdefuse.RoomProcessorBombDefuseSolver;
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.solvers.bombdefuse.chambers.BDChamber;
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.solvers.bombdefuse.chambers.GeneralDefuseChamberProcessor;
import kr.syeyoung.dungeonsguide.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import org.joml.Vector3i;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MazeRightProcessor extends GeneralDefuseChamberProcessor {
    private final Vector3i center;
    private final Map<Block, Vector3i> blockToBlockPosMap = new HashMap<Block, Vector3i>();
    private Block latestRequest = null;

    public MazeRightProcessor(RoomProcessorBombDefuseSolver solver, BDChamber chamber) {
        super(solver, chamber);
        center = chamber.getBlockPos(4, 4, 4);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 6; y++) {
                Block b = chamber.getBlock(x, 0, y).getBlock();
                Vector3i pos = chamber.getBlockPos(x, 0, y);
                blockToBlockPosMap.put(b, pos);
            }
        }
    }

    @Override
    public String getName() {
        return "mazeRight";
    }

    @Override
    public void drawWorld(float partialTicks) {
        super.drawWorld(partialTicks);
        RenderUtils.drawTextAtWorld(latestRequest == null ? "Request not received yet" : "Requested received " + latestRequest.getLocalizedName(), center.x + 0.5f, center.y, center.z + 0.5f, 0xFFFFFFFF, 0.03F, false, false, partialTicks);
        Vector3i pos = blockToBlockPosMap.get(latestRequest);
        if (pos == null) return;
        RenderUtils.highlightBlock(pos, new Color(0, 255, 0, 100), partialTicks, false);
    }

    @Override
    public void onDataRecieve(NBTTagCompound compound) {
        if (5 == compound.getByte("a")) {
            int latestRequestid = compound.getInteger("b");
            latestRequest = Block.getBlockById(latestRequestid);
        }
    }
}
