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

package kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.solvers.icefill;

import kr.syeyoung.dungeonsguide.chat.ChatTransmitter;
import kr.syeyoung.dungeonsguide.config.types.AColor;
import kr.syeyoung.dungeonsguide.dungeon.data.OffsetPointSet;
import kr.syeyoung.dungeonsguide.dungeon.DungeonRoom;
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.GeneralRoomProcessor;
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig;
import kr.syeyoung.dungeonsguide.oneconfig.solvers.IceFillPage;
import kr.syeyoung.dungeonsguide.utils.RenderUtils;
import net.minecraft.init.Blocks;
import org.joml.Vector3i;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomProcessorIcePath2 extends GeneralRoomProcessor {
    private static final List<Point> directions = Arrays.asList(new Point(0, -1), new Point(-1, 0), new Point(1, 0), new Point(0, 1));
    private final List<List<Vector3i>> solution = new CopyOnWriteArrayList<List<Vector3i>>();

    public RoomProcessorIcePath2(DungeonRoom dungeonRoom) {

        super(dungeonRoom);

        String levels = (String) dungeonRoom.getDungeonRoomInfo().getProperties().get("levels");
        if (levels == null) {
            return;
        }

        for (final String s : levels.split(",")) {
            try {
                OffsetPointSet level = (OffsetPointSet) dungeonRoom.getDungeonRoomInfo().getProperties().get(s + "-board");
                String data = (String) dungeonRoom.getDungeonRoomInfo().getProperties().get(s + "-level");
                final int width = Integer.parseInt(data.split(":")[0]);
                final int height = Integer.parseInt(data.split(":")[1]);
                final int startX = Integer.parseInt(data.split(":")[2]);
                final int startY = Integer.parseInt(data.split(":")[3]);
                final int endX = Integer.parseInt(data.split(":")[4]);
                final int endY = Integer.parseInt(data.split(":")[5]);

                final int[][] map = new int[height][width];
                final Vector3i[][] map2 = new Vector3i[height][width];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        map2[y][x] = level.getOffsetPointList().get(y * width + x).getVector3i(dungeonRoom);
                        map[y][x] = level.getOffsetPointList().get(y * width + x).getBlock(dungeonRoom) == Blocks.air ? 0 : 1;
                    }
                }

                new Thread() {
                    public void run() {
                        List<Point> hamiltonianPath = findFirstHamiltonianPath(map, startX, startY, endX, endY);
                        if (hamiltonianPath == null) {
                            ChatTransmitter.addToQueue("§eDungeons Guide §7:: §eIcePath §7:: §cCouldn't find solution for floor " + s);
                            return;
                        }
                        hamiltonianPath.add(0, new Point(startX, startY));
                        List<Vector3i> poses = new LinkedList<Vector3i>();
                        for (int i = 0; i < hamiltonianPath.size(); i++) {
                            Point p = hamiltonianPath.get(i);
                            poses.add(map2[p.y][p.x]);
                        }
                        solution.add(poses);
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Point> findFirstHamiltonianPath(int[][] map, int startX, int startY, int endX, int endY) {
        int emptySpace = 0;
        for (int y = 0; y < map.length; y++)
            for (int x = 0; x < map[y].length; x++)
                if (map[y][x] == 0) emptySpace++;

        map[startY][startX] = 2;

        return findHamiltonianPath(map, startX, startY, endX, endY, 0, emptySpace - 1);
    }

    private static LinkedList<Point> findHamiltonianPath(int[][] map, int startX, int startY, int endX, int endY, int depth, int reqDepth) {
        if (endX == startX && endY == startY) {
            if (depth != reqDepth) return null;
            LinkedList<Point> path = new LinkedList<Point>();
            path.add(new Point(startX, startY));
            return path;
        }

        for (Point p : directions) {
            int y = p.y + startY, x = p.x + startX;
            if (y < 0 || y >= map.length || x < 0 || x >= map[0].length || map[y][x] != 0) continue;

            int[][] copiedMap = new int[map.length][map[0].length];
            for (int y2 = 0; y2 < copiedMap.length; y2++)
                copiedMap[y2] = map[y2].clone();
            copiedMap[y][x] = 2;

            LinkedList<Point> potentialRoute = findHamiltonianPath(copiedMap, x, y, endX, endY, depth + 1, reqDepth);
            if (potentialRoute != null) {
                potentialRoute.addFirst(new Point(x, y));
                return potentialRoute;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void drawWorld(float partialTicks) {
        if (!DgOneCongifConfig.iceFill) return;
        for (List<Vector3i> solution : this.solution)
            RenderUtils.drawLines(solution, new AColor(IceFillPage.ICE_FILL_LINECOLOR.getRed(), IceFillPage.ICE_FILL_LINECOLOR.getBlue(), IceFillPage.ICE_FILL_LINECOLOR.getGreen(), IceFillPage.ICE_FILL_LINECOLOR.getAlpha()), IceFillPage.ICE_FILL_LINEWIDTH, partialTicks, true);
    }
}
