package kr.syeyoung.dungeonsguide.dungeon.mechanics;

import kr.syeyoung.dungeonsguide.dungeon.data.OffsetPoint;
import kr.syeyoung.dungeonsguide.dungeon.mechanics.action.*;
import kr.syeyoung.dungeonsguide.dungeon.roomfinder.DungeonRoom;
import kr.syeyoung.dungeonsguide.utils.RenderUtils;
import lombok.Data;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class DungeonPressurePlate implements DungeonMechanic {
    private OffsetPoint platePoint = new OffsetPoint(0,0,0);
    private List<String> preRequisite = new ArrayList<String>();
    private String triggering;

    @Override
    public Set<Action> getAction(String state, DungeonRoom dungeonRoom) {
        if (!("triggered".equalsIgnoreCase(state) || "untriggered".equalsIgnoreCase(state))) throw new IllegalArgumentException(state+" is not valid state for secret");
        Set<Action> base;
        Set<Action> preRequisites = base = new HashSet<Action>();
        if ("triggered".equalsIgnoreCase(state)) {
            ActionDropItem actionClick;
            preRequisites.add(actionClick = new ActionDropItem(platePoint));
            preRequisites = actionClick.getPreRequisite();
        }
        {
            ActionMove actionMove = new ActionMove(platePoint);
            preRequisites.add(actionMove);
            preRequisites = actionMove.getPreRequisite();
        }
        {
            for (String str : preRequisite) {
                ActionChangeState actionChangeState = new ActionChangeState(str.split(":")[0], str.split(":")[1]);
                preRequisites.add(actionChangeState);
            }
        }
        return base;
    }

    @Override
    public void highlight(Color color, String name, DungeonRoom dungeonRoom, float partialTicks) {
        BlockPos pos = getPlatePoint().getBlockPos(dungeonRoom);
        RenderUtils.highlightBlock(pos, color,partialTicks);
        RenderUtils.drawTextAtWorld(name, pos.getX() +0.5f, pos.getY()+0.75f, pos.getZ()+0.5f, 0xFFFFFFFF, 0.03f, false, true, partialTicks);
        RenderUtils.drawTextAtWorld(getCurrentState(dungeonRoom), pos.getX() +0.5f, pos.getY()+0.25f, pos.getZ()+0.5f, 0xFFFFFFFF, 0.03f, false, true, partialTicks);
    }

    public DungeonPressurePlate clone() throws CloneNotSupportedException {
        DungeonPressurePlate dungeonSecret = new DungeonPressurePlate();
        dungeonSecret.platePoint = (OffsetPoint) platePoint.clone();
        dungeonSecret.triggering = triggering;
        dungeonSecret.preRequisite = new ArrayList<String>(preRequisite);
        return dungeonSecret;
    }


    @Override
    public String getCurrentState(DungeonRoom dungeonRoom) {

        DungeonMechanic mechanic = dungeonRoom.getDungeonRoomInfo().getMechanics().get(triggering);
        if (mechanic == null)
        {
            return "undeterminable";
        } else {
            String state = mechanic.getCurrentState(dungeonRoom);
            if ("open".equalsIgnoreCase(state)) {
                return "triggered";
            } else {
                return "untriggered";
            }
        }
    }
}