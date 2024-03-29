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

import com.google.common.collect.ImmutableMap;
import kr.syeyoung.dungeonsguide.features.SimpleFeatureV2;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;


public class FeaturePenguins extends SimpleFeatureV2 {
    private OBJModel objModel;
    private IBakedModel model;

    public FeaturePenguins() {
        super("ftrPenguins");
        OBJLoader.instance.addDomain("dungeonsguide");
    }

    @SubscribeEvent
    public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        objModel = null;
        ResourceLocation modelResourceLocation = new ResourceLocation("dg-lts:models/penguin.obj");
        try {
            objModel = (OBJModel) OBJLoader.instance.loadModel(modelResourceLocation);
            objModel = (OBJModel) objModel.process(new ImmutableMap.Builder<String, String>().put("flip-v", "true").build());
            for (String obj : objModel.getMatLib().getMaterialNames()) {
                ResourceLocation resourceLocation = objModel.getMatLib().getMaterial(obj).getTexture().getTextureLocation();
                event.map.registerSprite(resourceLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onTextureStitchEvent(TextureStitchEvent.Post event) {
        if (objModel != null && event != null) {
            model = objModel.bake(objModel.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
        }
    }

    @SubscribeEvent
    public void onPlyrRender(RenderPlayerEvent.Pre pre) {
        if(true) return;
        if (!DgOneCongifConfig.penguins) return;
        if (pre.entityPlayer.isInvisible()) return;
        pre.setCanceled(true);
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.translate(pre.x, pre.y, pre.z);
        if (pre.entityPlayer.isSneaking()) {
            GlStateManager.translate(0.0F, -0.203125F, 0.0F);
        }
        float f1 = pre.entityPlayer.prevRotationYawHead + (pre.entityPlayer.rotationYawHead - pre.entityPlayer.prevRotationYawHead) * pre.partialRenderTick;
        GlStateManager.rotate(f1 + 180, 0, -1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(
                model, 1, 1, 1, 1
        );
        GlStateManager.popMatrix();


        EntityPlayer entitylivingbaseIn = pre.entityPlayer;
        {
            ItemStack itemstack = entitylivingbaseIn.getHeldItem();

            if (itemstack != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(pre.x, pre.y, pre.z);
                if (pre.entityPlayer.isSneaking()) {
                    GlStateManager.translate(0.0F, -0.203125F, 0.0F);
                }
                GlStateManager.rotate(f1 + 180, 0.0f, -1.0f, 0.0f);
                GlStateManager.translate(0, 1.30, -0.5);


                if (entitylivingbaseIn.fishEntity != null) {
                    itemstack = new ItemStack(Items.fishing_rod, 0);
                }

                Item item = itemstack.getItem();
                Minecraft minecraft = Minecraft.getMinecraft();

                GlStateManager.rotate(180, 0.0f, 0.0f, 1.0f);
                if (item.isFull3D()) {
                    GlStateManager.translate(0.05, 0, 0);
                    GlStateManager.rotate(90, 0.0f, 0.0f, 1.0f);
                    GlStateManager.rotate(-45, 1.0f, 0.0f, 0.0f);
                } else if (item instanceof ItemBow) {
                    GlStateManager.translate(0, 0.1, -0);
                    GlStateManager.rotate(90, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-90, 0.0f, 0.0f, 1.0f);
                } else if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                    GlStateManager.translate(0, -0.20, 0.1);
                    GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                    GlStateManager.rotate(-25.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                    f1 = 0.375F;
                    GlStateManager.scale(-f1, -f1, f1);
                } else if (item instanceof ItemBlock) {
                    GlStateManager.translate(0.0F, 0.05, 0.1);
                    GlStateManager.rotate(-25.0F, 1.0F, 0.0F, 0.0F);
                } else {
                    GlStateManager.translate(0, -0.1, 0.1);
                }

                GlStateManager.scale(0.8, 0.8, 0.8);

                minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
                GlStateManager.popMatrix();
            }
        }

        pre.renderer.renderName((AbstractClientPlayer) pre.entityPlayer, pre.x, pre.y, pre.z);

    }

}
