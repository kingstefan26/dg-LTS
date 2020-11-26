package kr.syeyoung.dungeonsguide.dungeon.roomfinder;

import kr.syeyoung.dungeonsguide.dungeon.data.DungeonRoomInfo;
import kr.syeyoung.dungeonsguide.utils.ArrayUtils;
import kr.syeyoung.dungeonsguide.utils.ShortUtils;
import lombok.Getter;
import net.minecraft.block.Block;

import java.util.List;

public class RoomMatcher {
    private DungeonRoom dungeonRoom;

    @Getter
    private DungeonRoomInfo match;
    @Getter
    private int rotation; // how much the "found room" has to rotate to match the given dungeon room info. !
    private boolean triedMatch = false;

    public RoomMatcher(DungeonRoom dungeonRoom) {
        this.dungeonRoom = dungeonRoom;
    }

    public DungeonRoomInfo match() {
        if (triedMatch) return match;
        triedMatch = true;
        for (int rotation = 0; rotation < 4; rotation++) {
            short shape = dungeonRoom.getShape();
            for (int j = 0; j<rotation; j++)
                shape = ShortUtils.rotateClockwise(shape);
            shape = ShortUtils.topLeftifyInt(shape);

            List<DungeonRoomInfo> roomInfoList = DungeonRoomInfoRegistry.getByShape(shape);
            for (DungeonRoomInfo roomInfo : roomInfoList) {
                if (tryMatching(roomInfo, rotation)) {
                    match = roomInfo;
                    this.rotation = rotation;
                    return match;
                }
            }
        }
        return null;
    }

    private boolean tryMatching(DungeonRoomInfo dungeonRoomInfo, int rotation) {
        if (dungeonRoomInfo.getColor() != dungeonRoom.getColor()) return false;

        int[][] res = dungeonRoomInfo.getBlocks();
        for (int i = 0; i < rotation; i++)
            res = ArrayUtils.rotateCounterClockwise(res);

        System.out.println("Trying to match "+dungeonRoomInfo.getUuid().toString()+" / "+dungeonRoomInfo.getName()+"... at rotation "+rotation);

        for (int z = 0; z < res.length; z ++) {
            for (int x = 0; x < res[0].length; x++) {
                int data = res[z][x];
                if (data == -1) continue;
                Block b = dungeonRoom.getRelativeBlockAt(x,0,z);
                if (b == null || Block.getIdFromBlock(b) != data) {
                    System.out.println("Match failed at offset X"+x+" / Z"+z+". expected "+data+" but found "+b +" ("+Block.getIdFromBlock(b)+")");
                    return false;
                }
            }
        }
        return true;
    }

    private static final int offset = 3;
    public DungeonRoomInfo createNew() {
        DungeonRoomInfo roomInfo = new DungeonRoomInfo(dungeonRoom.getShape(), dungeonRoom.getColor());

        int maxX = dungeonRoom.getMax().getX();
        int maxZ = dungeonRoom.getMax().getZ();
        int minX = dungeonRoom.getMin().getX();
        int minZ = dungeonRoom.getMin().getZ();
        int widthX = maxX - minX + 2;
        int heightZ = maxZ - minZ + 2;
        int[][] data = new int[dungeonRoom.getMax().getZ() - dungeonRoom.getMin().getZ() +2][dungeonRoom.getMax().getX() - dungeonRoom.getMin().getX() + 2];

        for (int z = 0; z < data.length; z++) {
            for (int x = 0; x < data[0].length; x++) {
                if (!(offset <= x && widthX - offset > x && offset <= z && heightZ - offset > z)) {
                    data[z][x] = -1;
                    continue;
                }
                if (!(dungeonRoom.canAccessRelative(x + offset - 1, z + offset - 1) && dungeonRoom.canAccessRelative(x - offset, z - offset))) {
                    data[z][x] = -1;
                    continue;
                }

                Block b = dungeonRoom.getRelativeBlockAt(x,0,z);
                if (b == null) {
                    data[z][x] = -1;
                } else {
                    data[z][x] = Block.getIdFromBlock(b);
                }
            }
        }

        roomInfo.setBlocks(data);
        return roomInfo;
    }
}