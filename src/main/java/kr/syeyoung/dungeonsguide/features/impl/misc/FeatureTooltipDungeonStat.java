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

package kr.syeyoung.dungeonsguide.features.impl.misc;

import kr.syeyoung.dungeonsguide.utils.SkyblockStatus;
import kr.syeyoung.dungeonsguide.features.SimpleFeatureV2;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FeatureTooltipDungeonStat extends SimpleFeatureV2 {
    public FeatureTooltipDungeonStat() {
        super("tooltip.dungeonitem");
    }


    @SubscribeEvent
    public void dungeonTooltip(ItemTooltipEvent event) {
        if (!SkyblockStatus.isOnSkyblock()) return;
        if (!DgOneCongifConfig.dungeonStat) return;

        ItemStack hoveredItem = event.itemStack;
        NBTTagCompound compound = hoveredItem.getTagCompound();
        if (compound == null) {
            return;
        }
        if (!compound.hasKey("ExtraAttributes")) {
            return;
        }
        NBTTagCompound nbtTagCompound = compound.getCompoundTag("ExtraAttributes");

        int floor = nbtTagCompound.getInteger("item_tier");
        int percentage = nbtTagCompound.getInteger("baseStatBoostPercentage");

        if (nbtTagCompound.hasKey("item_tier")) {
            event.toolTip.add("§7Obtained in: §c"+(floor == 0 ? "Entrance" : "Floor "+floor));
        }
        if (nbtTagCompound.hasKey("baseStatBoostPercentage")) {
            event.toolTip.add("§7Stat Percentage: §"+(percentage == 50 ? "6§l":"c")+(percentage * 2)+"%");
        }
    }

}
