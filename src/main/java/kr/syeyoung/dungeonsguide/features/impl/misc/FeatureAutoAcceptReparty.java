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
import kr.syeyoung.dungeonsguide.chat.ChatProcessor;
import kr.syeyoung.dungeonsguide.features.SimpleFeatureV2;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import kr.syeyoung.dungeonsguide.utils.TextUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FeatureAutoAcceptReparty extends SimpleFeatureV2 {
    public FeatureAutoAcceptReparty() {
        super("qol.autoacceptreparty");
    }

    private String lastDisband;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent ev) {
        if (!SkyblockStatus.isOnSkyblock()) return;

        if (ev.message.getFormattedText().endsWith("§ehas disbanded the party!§r")) {
            lastDisband = null;
            String[] texts = TextUtils.stripColor(ev.message.getFormattedText()).split(" ");
            for (String s : texts) {
                if (s.isEmpty()) continue;
                if (s.startsWith("[")) continue;
                if (s.equalsIgnoreCase("has")) break;
                lastDisband = s;
                break;
            }
        } else if (ev.message.getFormattedText().contains("§ehas invited you to join their party!")) {
            String[] texts = TextUtils.stripColor(ev.message.getFormattedText()).split(" ");
            boolean equals = false;
            for (String s : texts) {
                if (s.isEmpty()) continue;
                if (s.startsWith("[")) continue;
                if (s.equalsIgnoreCase("has")) continue;
                if (s.equalsIgnoreCase(lastDisband)) {
                    equals = true;
                    break;
                }
            }

            if (equals && DgOneCongifConfig.autoRp) {
                ChatProcessor.INSTANCE.addToChatQueue("/p accept " + lastDisband, () -> {}, true);
                lastDisband = null;
            }
        }
    }

}
