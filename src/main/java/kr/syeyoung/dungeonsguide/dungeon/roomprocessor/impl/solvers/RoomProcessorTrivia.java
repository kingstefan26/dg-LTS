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

package kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.solvers;

import kr.syeyoung.dungeonsguide.chat.ChatTransmitter;
import kr.syeyoung.dungeonsguide.config.types.AColor;
import kr.syeyoung.dungeonsguide.dungeon.data.OffsetPoint;
import kr.syeyoung.dungeonsguide.dungeon.DungeonRoom;
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.GeneralRoomProcessor;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import kr.syeyoung.dungeonsguide.oneconfig.solvers.KahootPage;
import kr.syeyoung.dungeonsguide.utils.RenderUtils;
import kr.syeyoung.dungeonsguide.utils.SkyblockUtils;
import kr.syeyoung.dungeonsguide.utils.TextUtils;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.math.NumberUtils;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomProcessorTrivia extends GeneralRoomProcessor {

    public static final Pattern anwerPattern = Pattern.compile("§r§6 . §a(.+)§r");
    private final List<String> questionDialog = new ArrayList<String>();
    String correctAnswer;
    private boolean questionDialogStart = false;
    private boolean parseDialog = false;

    public RoomProcessorTrivia(DungeonRoom dungeonRoom) {
        super(dungeonRoom);
    }

    @Override
    public void chatReceived(IChatComponent chat) {
        super.chatReceived(chat);
        if (!DgOneCongifConfig.kahootSolver) return;
        String ch2 = chat.getUnformattedText();
        if (parseDialog) {
            parseDialog = false;
            parseDialog();
        }
        if (chat.getFormattedText().contains("§r§6§lQuestion ")) {
            questionDialogStart = true;
            questionDialog.clear();
        }
        if (questionDialogStart && (chat.getFormattedText().startsWith("§r§r§r") || chat.getFormattedText().startsWith("§r§r§r") || chat.getFormattedText().trim().startsWith("§r§6 "))) {
            questionDialog.add(chat.getFormattedText());
        }

        if (chat.getFormattedText().contains("§r§6 ⓒ")) {
            questionDialogStart = false;
            parseDialog = true;
        }
    }

    private void parseDialog() {
        String question = TextUtils.stripColor(questionDialog.get(1)).trim();
        String answerA = getAnswer(questionDialog.get(2));
        String answerB = getAnswer(questionDialog.get(3));
        String answerC = getAnswer(questionDialog.get(4));
        match(question, answerA, answerB, answerC);

    }

    private String getAnswer(String answerString) {
        Matcher matcher = anwerPattern.matcher(answerString.trim());
        if (!matcher.matches()) return "";
        return matcher.group(1);
    }

    private void match(String question, String a, String b, String c) {
//        String semi_answers = answersJSON.getString(question.toLowerCase().trim());
//        String theRealAnswer;
//        if (semi_answers == null) theRealAnswer = null;
//        else {
//            semi_answers = takeCareOfPlaceHolders(semi_answers);
//            String[] answers = semi_answers.split(",");
//            if (match(answers, a)) theRealAnswer = "A";
//            else if (match(answers, b)) theRealAnswer = "B";
//            else if (match(answers, c)) theRealAnswer = "C";
//            else theRealAnswer = semi_answers;
//        }
//        if (theRealAnswer == null)
//            ChatTransmitter.addToQueue(new ChatComponentText("§eDungeons Guide §7:: §eTrivia §7:: §cCouldn't determine the answer! (no question found)"));
//        else if (theRealAnswer.length() > 1)
//            ChatTransmitter.addToQueue(new ChatComponentText("§eDungeons Guide §7:: §eTrivia §7:: §cCouldn't determine the answer! (" + theRealAnswer + ")"));
//        else
//            ChatTransmitter.addToQueue(new ChatComponentText("§eDungeons Guide §7:: §eTrivia §7:: " + theRealAnswer + "§f is the correct answer!"));
//        correctAnswer = theRealAnswer;
    }

    private String takeCareOfPlaceHolders(String input) {
        String str = input;
        if (str.contains("$year")) {
            ChatTransmitter.addToQueue(new ChatComponentText("§eDungeons Guide §fuses §eInventiveTalent§7(https://github.com/InventivetalentDev)§e's Skyblock Api §fto fetch current skyblock year!"));
            try {
                str = str.replace("$year", SkyblockUtils.getSkyblockYear() + "");
            } catch (IOException e) {
                str = str.replace("$year", "Couldn't determine current skyblock year :: " + e.getMessage());
            }
        }
        return str;
    }

    private boolean match(String[] match, String match2) {
        for (String s : match) {
            if (NumberUtils.isNumber(s)) {
                if (match2.toLowerCase().contains(s)) return true;
            } else {
                if (match2.equalsIgnoreCase(s)) return true;
            }
        }
        return false;
    }

    @Override
    public void drawWorld(float partialTicks) {
        super.drawWorld(partialTicks);
        if (!DgOneCongifConfig.kahootSolver) return;
        if (correctAnswer == null) return;

        OffsetPoint op = (OffsetPoint) getDungeonRoom().getDungeonRoomInfo().getProperties().get(correctAnswer);
        if (op != null) {
            Vector3i solution = op.getVector3i(getDungeonRoom());
            RenderUtils.highlightBoxAColor(AxisAlignedBB.fromBounds(solution.x, solution.y, solution.z, solution.x + 1, solution.y + 1, solution.z + 1), new AColor(KahootPage.color.getRed(), KahootPage.color.getBlue(), KahootPage.color.getGreen(), KahootPage.color.getAlpha()), partialTicks, false);
        }
    }


    @Override
    public boolean readGlobalChat() {
        return true;
    }
}
