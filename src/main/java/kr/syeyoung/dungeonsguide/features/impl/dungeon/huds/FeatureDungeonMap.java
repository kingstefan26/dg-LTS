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

package kr.syeyoung.dungeonsguide.features.impl.dungeon.huds;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import kr.syeyoung.dungeonsguide.DungeonsGuide;
import kr.syeyoung.dungeonsguide.chat.ChatTransmitter;
import kr.syeyoung.dungeonsguide.dungeon.*;
import kr.syeyoung.dungeonsguide.events.impl.BossroomEnterEvent;
import kr.syeyoung.dungeonsguide.events.impl.DungeonLeftEvent;
import kr.syeyoung.dungeonsguide.events.impl.DungeonStartedEvent;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import kr.syeyoung.dungeonsguide.utils.RenderUtils;
import kr.syeyoung.dungeonsguide.utils.SkyblockStatus;
import kr.syeyoung.dungeonsguide.utils.TabListUtil;
import lombok.val;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec4b;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.util.List;

public class FeatureDungeonMap extends BasicHud {

    transient public static final Ordering<NetworkPlayerInfo> sorter = Ordering.from((compare1, compare2) -> {
        ScorePlayerTeam scoreplayerteam = compare1.getPlayerTeam();
        ScorePlayerTeam scoreplayerteam1 = compare2.getPlayerTeam();
        return ComparisonChain.start().compareTrueFirst(compare1.getGameType() != WorldSettings.GameType.SPECTATOR, compare2.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(compare1.getGameProfile().getName(), compare2.getGameProfile().getName()).result();
    });
    transient private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
    transient private final DynamicTexture mapTexture = new DynamicTexture(128, 128);
    transient private final ResourceLocation generatedMapTexture = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("dungeonmap/map", mapTexture);
    transient private final int[] mapTextureData = mapTexture.getTextureData();
    transient int[] lastRoomColors = new int[50];
    transient int[] lastRoomSecrets = new int[50];
    transient long nextRefresh;
    transient Tuple<String[], List<NetworkPlayerInfo>> playerListCached;

    @Color(name = "Color of the player border")
    public static OneColor playerColor = new OneColor(255, 255, 255, 0);
    @Checkbox(name = "Should cache map data")
    public static boolean shouldCacheMap = true;
    @Slider(name = "Player head scale", description = "Scale factor of player heads, defaults to 1", min = 1, max = 10)
    public static float playerHeadScale = 1;
    @Checkbox(name = "Show other players", description = "Option to show other players in map")
    public static boolean shouldShowOtherPlayers = true;
    @Slider(name = "Text scale", description = "Scale factor of texts on map, defaults to 1", min = 1, max = 10)
    public static float textScale = 1;
    @Checkbox(name = "Show Total secrets in the room", description = "Option to overlay total secrets in the specific room")
    public static boolean showSecretCount = true;
    @Checkbox(name = "Use player heads instead of arrows")
    public static boolean showPlayerHeads = false;
    @Checkbox(name = "Rotate map centered at player")
    public static boolean shouldRotateWithPlayer = true;
    @Checkbox(name = "shouldScale")
    public static boolean shouldScale = false;
    @Slider(name = "Scale factor of map", min = 1, max = 5)
    public static float postscaleOfMap = 1;
    @Checkbox(name = "Center map at player")
    public static boolean centerMapOnPlayer = true;
    private boolean on = false;

    public FeatureDungeonMap() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDungeonInitialize(BossroomEnterEvent enterEvent) {
        on = false;
    }

    @SubscribeEvent
    public void onDungeonLeft(DungeonLeftEvent leftEvent) {
        on = false;
    }

    @SubscribeEvent
    public void onDungeonStart(DungeonStartedEvent leftEvent) {
        on = true;
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        return scale < 1 ? 128 : 128 * scale;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        return scale < 1 ? 128 : 128 * scale;
    }

    FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }

    @Override
    protected void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {

        if (example) {
            DungeonContext context = DungeonsGuide.getDungeonsGuide().getDungeonFacade().context;
            if (SkyblockStatus.isOnDungeon() && context != null && context.mapProcessor.isInitialized() && on) {

                GlStateManager.pushMatrix();


                int height = ((int) (getHeight(scale, false) + y) * 3);
                int width = (int) ((getWidth(scale, false) + x) * 3);
                GL11.glScissor((int) x, (int) (Minecraft.getMinecraft().displayHeight - y - height), width, height);

                GlStateManager.translate(position.getX(), position.getY(), 0);
//                GlStateManager.scale(scale, scale, 1);
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                drawHUD(RenderUtils.getPartialTicks());
                GL11.glDisable(GL11.GL_SCISSOR_TEST);


                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

                val text = "Please join a dungeon to see preview";

                GlStateManager.translate(position.getX(), position.getY(), 0);
                GlStateManager.scale(scale, scale, 1);
                getFontRenderer().drawString(text, (int) position.getWidth() / 2 - getFontRenderer().getStringWidth(text) / 2, (int) position.getHeight() / 2 - getFontRenderer().FONT_HEIGHT / 2, 0xFFFFFFFF);

                GlStateManager.popMatrix();
            }

        } else {
            try {
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, 1);
                GlStateManager.translate(position.getX(), position.getY(), 0);

                int height = (int) (getHeight(this.scale, false) + (int) y) * 3;
                GL11.glScissor((int) x, Minecraft.getMinecraft().displayHeight - (int) y - height, (int) ((getWidth(this.scale, false) + (int) x) * 3), height);

                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                drawHUD(RenderUtils.getPartialTicks());
                GlStateManager.enableBlend();
                GL11.glDisable(GL11.GL_SCISSOR_TEST);


                GlStateManager.popMatrix();
            } catch (Exception t) {
                t.printStackTrace();
            }
        }
    }

    void drawHUD(float partialTicks) {
        DungeonContext context = DungeonFacade.context;
        if (context == null || !context.mapProcessor.isInitialized()) return;
        GlStateManager.pushMatrix();

        MapProcessor mapProcessor = context.mapProcessor;
        MapData mapData = mapProcessor.getLatestMapData();
        if (mapData != null) {
            renderMap(partialTicks, mapProcessor, mapData, context);
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected boolean shouldShow() {
        if (!on) return false;
        if (!isEnabled()) return false;
        if (!SkyblockStatus.isOnSkyblock()) return false;
        if (!SkyblockStatus.isOnDungeon()) return false;
        return super.shouldShow();
    }


    public void renderMap(float partialTicks, MapProcessor mapProcessor, MapData mapData, DungeonContext context) {
        float postScale = centerMapOnPlayer ? postscaleOfMap : 1;

        float scale = shouldScale ? position.getWidth() / 128.0f : 1;

        GlStateManager.translate(position.getWidth() / 2d, position.getWidth() / 2d, 0);
        GlStateManager.scale(scale, scale, 0);
        GlStateManager.scale(postScale, postScale, 0);

        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
        org.joml.Vector2d pt = mapProcessor.worldPointToMapPointFLOAT(p.getPositionEyes(partialTicks));
        if (centerMapOnPlayer) {
            if (shouldRotateWithPlayer) {
                GlStateManager.rotate((float) (180.0 - p.rotationYaw), 0, 0, 1);
            }
            GlStateManager.translate(-pt.x, -pt.y, 0);
        } else {
            GlStateManager.translate(-64, -64, 0);
        }

        updateMapTexture(mapData.colors, mapProcessor, context.dungeonRoomList);

        drawMap();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);

        if (showPlayerHeads) {
            renderHeads(mapProcessor, mapData, scale, postScale, partialTicks);
        } else {
            renderArrows(mapData, scale, postScale);
        }


        if (showSecretCount) {
            FontRenderer fr = getFontRenderer();
            for (DungeonRoom dungeonRoom : context.dungeonRoomList) {
                GlStateManager.pushMatrix();

                Vector2i mapPt = mapProcessor.roomPointToMapPoint(dungeonRoom.getUnitPoints().get(0));
                GlStateManager.translate(mapPt.x + mapProcessor.getUnitRoomDimension().width / 2d, mapPt.y + mapProcessor.getUnitRoomDimension().height / 2d, 0);

                if (centerMapOnPlayer && shouldRotateWithPlayer) {
                    GlStateManager.rotate((float) ((double) p.rotationYaw - 180), 0, 0, 1);
                }
                GlStateManager.scale(1 / scale, 1 / scale, 0);
                GlStateManager.scale(1 / postScale, 1 / postScale, 0);
                GlStateManager.scale(textScale, textScale, 0);
                String str = "";
                if (dungeonRoom.getTotalSecrets() == -1) {
                    str += "? ";
                } else if (dungeonRoom.getTotalSecrets() != 0) {
                    str += dungeonRoom.getTotalSecrets() + " ";
                }

                RoomState currentState = dungeonRoom.getCurrentState();
                switch (currentState) {
                    case FINISHED:
                    case COMPLETE_WITHOUT_SECRETS:
                        str += "✔";
                        break;
                    case DISCOVERED:
                        str += "☐";
                        break;
                    case FAILED:
                        str += "❌";
                        break;
                }


                GlStateManager.enableBlend();
                GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
                if (currentState == RoomState.FINISHED) {
                    fr.drawString(str, -(fr.getStringWidth(str) / 2), -(fr.FONT_HEIGHT / 2), 0xFF00FF00);
                } else {
                    if (dungeonRoom.getColor() == 74) {
                        fr.drawString(str, -(fr.getStringWidth(str) / 2), -(fr.FONT_HEIGHT / 2), 0xff000000);
                    } else {
                        fr.drawString(str, -(fr.getStringWidth(str) / 2), -(fr.FONT_HEIGHT / 2), 0xFFFFFFFF);
                    }
                }

                GlStateManager.popMatrix();
            }
        }

    }

    private void drawMap() {
        int i = 0;
        int j = 0;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f = 0.0F;
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.generatedMapTexture);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
        GlStateManager.disableAlpha();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((i) + f, (j + 128) - f, -0.009999999776482582D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((i + 128) - f, (j + 128) - f, -0.009999999776482582D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((i + 128) - f, (j) + f, -0.009999999776482582D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos((i) + f, (j) + f, -0.009999999776482582D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, -0.04F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private void updateMapTexture(byte[] colors, MapProcessor mapProcessor, List<DungeonRoom> dungeonRooms) {

        if (shouldCacheMap && !didMapChange(dungeonRooms)) return;


        for (int i = 0; i < 16384; ++i) {
            int j = colors[i] & 255;

            if (j / 4 == 0) {
                this.mapTextureData[i] = 0x00000000;
            } else {
                this.mapTextureData[i] = MapColor.mapColorArray[j / 4].getMapColor(j & 3);
            }
        }

        if (this.showSecretCount) {
            for (DungeonRoom dungeonRoom : dungeonRooms) {
                for (Vector2i pt : dungeonRoom.getUnitPoints()) {
                    for (int y1 = 0; y1 < mapProcessor.getUnitRoomDimension().height; y1++) {
                        for (int x1 = 0; x1 < mapProcessor.getUnitRoomDimension().width; x1++) {
                            int x = MathHelper.clamp_int(pt.x * (mapProcessor.getUnitRoomDimension().width + mapProcessor.getDoorDimensions().height) + x1 + mapProcessor.getTopLeftMapPoint().x, 0, 128);
                            int y = MathHelper.clamp_int(pt.y * (mapProcessor.getUnitRoomDimension().height + mapProcessor.getDoorDimensions().height) + y1 + mapProcessor.getTopLeftMapPoint().y, 0, 128);
                            int i = y * 128 + x;
                            int j = dungeonRoom.getColor();

                            if (j / 4 == 0) {
                                this.mapTextureData[i] = 0x00000000;
                            } else {
                                this.mapTextureData[i] = MapColor.mapColorArray[j / 4].getMapColor(j & 3);
                            }
                        }
                    }
                }
            }
        }


        this.mapTexture.updateDynamicTexture();
    }

    private boolean didMapChange(List<DungeonRoom> dungeonRooms) {
        int[] currentRoomColors = new int[50];
        int[] currentRoomSecrets = new int[50];

        for (int i = 0; i < dungeonRooms.size(); i++) {
            DungeonRoom dungeonRoom = dungeonRooms.get(i);

            currentRoomSecrets[i] = dungeonRoom.getTotalSecrets();
            currentRoomColors[i] = dungeonRoom.getColor();

        }

        for (int i = 0; i < 50; i++) {
            if (lastRoomColors[i] != currentRoomColors[i] || lastRoomSecrets[i] != currentRoomSecrets[i]) {
                lastRoomColors = currentRoomColors;
                lastRoomSecrets = currentRoomSecrets;
                return true;
            }
        }

        lastRoomColors = currentRoomColors;
        lastRoomSecrets = currentRoomSecrets;
        return false;
    }

    @Nullable
    public Tuple<String[], List<NetworkPlayerInfo>> loadPlayerList() {
        String[] names = new String[21];

        List<NetworkPlayerInfo> networkList = sorter.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
        if (networkList.size() < 40) return null;

        for (int i = 0; i < 20; i++) {
            names[i] = TabListUtil.getPlayerNameWithChecks(networkList.get(i));
        }

        return new Tuple<>(names, networkList);
    }

    public Tuple<String[], List<NetworkPlayerInfo>> getPlayerListCached() {
        if (playerListCached == null || nextRefresh <= System.currentTimeMillis()) {
            ChatTransmitter.sendDebugChat("Refreshing players on map");
            playerListCached = loadPlayerList();
            nextRefresh = System.currentTimeMillis() + 10000;
        }
        return playerListCached;
    }

    private void renderHeads(MapProcessor mapProcessor, MapData mapData, float scale, float postScale, float partialTicks) {
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;

        Tuple<String[], List<NetworkPlayerInfo>> playerList = getPlayerListCached();

        List<NetworkPlayerInfo> networkList = playerList.getSecond();
        String[] names = playerList.getFirst();


        // 21 iterations bc we only want to scan the player part of tab list
        for (int i = 1; i < 20; i++) {
            NetworkPlayerInfo networkPlayerInfo = networkList.get(i);

            String name = names[i];
            if (name == null) continue;


            EntityPlayer entityplayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(name);

            org.joml.Vector2d pt2 = null;
            double yaw2 = 0;

            if (entityplayer != null && (!entityplayer.isInvisible() || entityplayer == thePlayer)) {
                // getting location from player entity
                pt2 = mapProcessor.worldPointToMapPointFLOAT(entityplayer.getPositionEyes(partialTicks));
                yaw2 = entityplayer.prevRotationYawHead + (entityplayer.rotationYawHead - entityplayer.prevRotationYawHead) * partialTicks;
                if (DungeonsGuide.getDungeonsGuide().verbose) System.out.println("Got player location from entity");
            }
//            else {
//                // getting player location from map
//                String iconName = mapProcessor.getMapIconToPlayerMap().get(name);
//                if (iconName != null) {
//                    Vec4b vec = mapData.mapDecorations.get(iconName);
//                    if (vec != null) {
//                        System.out.println("Got player location from map");
//                        pt2 = new Vector2d(vec.func_176112_b() / 2d + 64, vec.func_176113_c() / 2d + 64);
//                        yaw2 = vec.func_176111_d() * 360 / 16.0f;
//                    } else {
//                        System.out.println("Failed getting location from map, vec is null");
//                    }
//                } else {
//                    System.out.println("Failed getting location from map, icon name is null");
//                }
//            }

            if (pt2 == null) return;

            if (entityplayer == thePlayer || shouldShowOtherPlayers) {
                drawHead(scale, postScale, networkPlayerInfo, entityplayer, pt2, (float) yaw2);
            }

        }
    }

    /**
     * @param scale             Scale of the map
     * @param postScale
     * @param networkPlayerInfo
     * @param entityplayer
     * @param pt2
     * @param yaw2
     */
    private void drawHead(float scale, float postScale, NetworkPlayerInfo networkPlayerInfo, EntityPlayer entityplayer, org.joml.Vector2d pt2, float yaw2) {
        GlStateManager.pushMatrix();
        boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE);
        GlStateManager.enableTexture2D();
        Minecraft.getMinecraft().getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
        int l2 = 8 + (flag1 ? 8 : 0);
        int i3 = 8 * (flag1 ? -1 : 1);

        GlStateManager.translate(pt2.x, pt2.y, 0);
        GlStateManager.rotate(yaw2, 0, 0, 1);

        GlStateManager.scale(1 / scale, 1 / scale, 0);
        GlStateManager.scale(1 / postScale, 1 / postScale, 0);

        GlStateManager.scale(playerHeadScale, playerHeadScale, 0);

        // cutting out the player head out of the skin texture
        Gui.drawScaledCustomSizeModalRect(-4, -4, 8.0F, l2, 8, i3, 8, 8, 64.0F, 64.0F);
        GL11.glLineWidth(1);
        RenderUtils.drawUnfilledBox(-4, -4, 4, 4, DgOneCongifConfig.oneconftodgcolor(playerColor));
        GlStateManager.popMatrix();
    }

    private void renderArrows(MapData mapData, float scale, float postScale) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int k = 0;
        Minecraft.getMinecraft().getTextureManager().bindTexture(mapIcons);
        for (Vec4b vec4b : mapData.mapDecorations.values()) {
            if (vec4b.func_176110_a() == 1 || shouldShowOtherPlayers) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(vec4b.func_176112_b() / 2.0F + 64.0F, vec4b.func_176113_c() / 2.0F + 64.0F, -0.02F);
                GlStateManager.rotate((vec4b.func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);

                GlStateManager.scale(1 / scale, 1 / scale, 0);
                GlStateManager.scale(1 / postScale, 1 / postScale, 0);
                GlStateManager.scale(playerHeadScale * 5, playerHeadScale * 5, 0);

                GlStateManager.translate(-0.125F, 0.125F, 0.0F);
                byte b0 = vec4b.func_176110_a();
                float f1 = (b0 % 4) / 4.0F;
                float f2 = (b0 / 4f) / 4.0F;
                float f3 = (b0 % 4 + 1) / 4.0F;
                float f4 = (b0 / 4f + 1) / 4.0F;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos(-1.0D, 1.0D, k * -0.001F).tex(f1, f2).endVertex();
                worldrenderer.pos(1.0D, 1.0D, k * -0.001F).tex(f3, f2).endVertex();
                worldrenderer.pos(1.0D, -1.0D, k * -0.001F).tex(f3, f4).endVertex();
                worldrenderer.pos(-1.0D, -1.0D, k * -0.001F).tex(f1, f4).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
                ++k;
            }
        }
    }

}
