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

package kr.syeyoung.dungeonsguide.features.impl.debug;

import kr.syeyoung.dungeonsguide.DungeonsGuide;
import kr.syeyoung.dungeonsguide.dungeon.DungeonContext;
import kr.syeyoung.dungeonsguide.dungeon.DungeonFacade;
import kr.syeyoung.dungeonsguide.dungeon.DungeonRoom;
import kr.syeyoung.dungeonsguide.dungeon.data.OffsetPoint;
import kr.syeyoung.dungeonsguide.features.GuiFeature;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import kr.syeyoung.dungeonsguide.utils.SkyblockStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class FeatureRoomCoordDisplay extends GuiFeature {
    public FeatureRoomCoordDisplay() {
        super("Debug", "Display Coordinate Relative to the Dungeon Room and room's rotation", "X: 0 Y: 3 Z: 5 Facing: Z+" , "advanced.coords", false, getFontRenderer().getStringWidth("X: 48 Y: 100 Z: 48 Facing: Z+"), 10);
//        this.setEnabled(false);
//        addParameter("color", new FeatureParameter<>("color", "Color", "Color of text", Color.yellow, "color", nval -> color = nval.getRGB()));
    }

    @Override
    public boolean isEnabled() {
        return DgOneCongifConfig.debugDungeonCords;
    }


    SkyblockStatus skyblockStatus = DungeonsGuide.getDungeonsGuide().getSkyblockStatus();
    private static final String[] facing = {"Z+", "X-", "Z-", "X+"};
    @Override
    public void drawHUD(float partialTicks) {
        if (!SkyblockStatus.isOnDungeon()) return;
        DungeonContext context = DungeonFacade.context;
        if (context == null) return;

        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        DungeonRoom dungeonRoom = context.getCurrentRoom();
        if (dungeonRoom == null) {
            return;
        }

        int facing = (int) (thePlayer.rotationYaw + 45) % 360;
        if (facing < 0) facing += 360;
        int real = (facing / 90 + dungeonRoom.getRoomMatcher().getRotation()) % 4;

        OffsetPoint offsetPoint = new OffsetPoint(dungeonRoom, new Vector3i((int)thePlayer.posX, (int)thePlayer.posY, (int)thePlayer.posZ));

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        asd(fontRenderer);
        fontRenderer.drawString("X: "+offsetPoint.getX()+" Y: "+offsetPoint.getY()+" Z: "+offsetPoint.getZ()+" Facing: "+ FeatureRoomCoordDisplay.facing[real], 0, 0, DgOneCongifConfig.dungeonCordsColor.getRGB());
    }

    private void asd(FontRenderer fontRenderer) {
        double scale = getFeatureRect().getRectangle().getHeight() / fontRenderer.FONT_HEIGHT;
        GlStateManager.scale(scale, scale, 0);

        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void drawDemo(float partialTicks) {
        FontRenderer fr = getFontRenderer();
        int facing = (int) (Minecraft.getMinecraft().thePlayer.rotationYaw + 45) % 360;
        if (facing < 0) facing += 360;
        asd(fr);
        fr.drawString("X: 0 Y: 3 Z: 5 Facing: "+FeatureRoomCoordDisplay.facing[(facing / 90) % 4], 0,0, DgOneCongifConfig.dungeonCordsColor.getRGB());
    }

}
