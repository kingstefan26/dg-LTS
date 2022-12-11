package kr.syeyoung.dungeonsguide.dungeon.roomprocessor.secretfinderstrategies

import kr.syeyoung.dungeonsguide.dungeon.actions.ActionState
import kr.syeyoung.dungeonsguide.dungeon.actions.impl.ActionComplete
import kr.syeyoung.dungeonsguide.dungeon.actions.ActionPlan
import kr.syeyoung.dungeonsguide.dungeon.actions.ActionPlanProperties
import kr.syeyoung.dungeonsguide.dungeon.mechanics.impl.DungeonSecret
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.GeneralRoomProcessor
import kr.syeyoung.dungeonsguide.oneconfig.DgOneCongifConfig
import java.util.*

fun buildSecretStrategy(e: Int, parent: GeneralRoomProcessor): SecretGuideStrategy {
    return when (e) {
        0 -> PathfindToAllStrategy(parent)
        1 -> BloodRushStrategy(parent)
        2 -> AutoFinderStrategy(parent)
        else -> throw IllegalArgumentException("Invalid Strategy")
    }

}

abstract class SecretGuideStrategy(val parent: GeneralRoomProcessor) {
    open val actionPath: MutableMap<String, ActionPlan> = HashMap()
    fun createActionRoute(mechanic: String, state: ActionState, actionPlanProperties: ActionPlanProperties): String {
        val str = UUID.randomUUID().toString()
        createActionRoute(str, mechanic, state, actionPlanProperties)
        return str
    }
    fun createActionRoute(id: String, mechanic: String, state: ActionState, actionPlanProperties: ActionPlanProperties) {
        if(DgOneCongifConfig.debugMode){
            println("Creating action route,  mechanic: $mechanic , state: $state")
        }
        actionPath[id] = ActionPlan(parent.dungeonRoom, mechanic, state, actionPlanProperties)
    }
    fun cancel(id: String) {
        actionPath.remove(id)
    }
    fun getPath(id: String): ActionPlan? {
        return actionPath[id]
    }
    open fun update(){

        val pathIterator = actionPath.iterator()
        while (pathIterator.hasNext()) {
            val message = pathIterator.next()
            message.value.onTick()
            if(message.value.currentAction is ActionComplete){
                pathIterator.remove()
            }
        }

        parent.dungeonRoom.mechanics.values.forEach { value ->
            if (value is DungeonSecret) {
                value.tick(parent.dungeonRoom)
            }
        }


    }
    abstract fun init()
    open fun draw(partialTick:Float) {}
    fun cancelAll() {
        actionPath.clear();
    }
}