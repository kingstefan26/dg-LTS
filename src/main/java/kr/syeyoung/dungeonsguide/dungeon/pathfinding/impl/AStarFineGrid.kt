package kr.syeyoung.dungeonsguide.dungeon.pathfinding.impl

import kr.syeyoung.dungeonsguide.dungeon.pathfinding.AStarUtil
import kr.syeyoung.dungeonsguide.dungeon.pathfinding.AStarUtil.Node
import kr.syeyoung.dungeonsguide.dungeon.pathfinding.DungeonRoomAccessor
import kr.syeyoung.dungeonsguide.dungeon.pathfinding.PathfinderStrategy
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper
import org.joml.Vector3d
import java.util.*

class AStarFineGrid(private val room: DungeonRoomAccessor) : PathfinderStrategy(room) {
    var dx: Int = 0
    var dy: Int = 0
    var dz: Int = 0


    private lateinit var destinationBB: AxisAlignedBB
    private val nodeMap: MutableMap<AStarUtil.Coordinate, Node> = HashMap()

    private val open = PriorityQueue(
        Comparator.comparing { a: Node? -> a?.f ?: Float.MAX_VALUE }
            .thenComparing { _, a: Node? -> a?.coordinate?.x ?: Int.MAX_VALUE }
            .thenComparing { _, a: Node? -> a?.coordinate?.y ?: Int.MAX_VALUE }
            .thenComparing { _, a: Node? -> a?.coordinate?.z ?: Int.MAX_VALUE }
    )

    var lastSx = 0
    var lastSy = 0
    var lastSz = 0


    override fun pathfind(from: Vector3d, to: Vector3d, timeout: Float): Boolean {
        pfindIdx++

        dx = (to.x * 2).toInt()
        dy = (to.y * 2).toInt()
        dz = (to.z * 2).toInt()
        destinationBB = AxisAlignedBB.fromBounds(
            (dx - 2).toDouble(),
            (dy - 2).toDouble(),
            (dz - 2).toDouble(),
            (dx + 2).toDouble(),
            (dy + 2).toDouble(),
            (dz + 2).toDouble()
        )

        if (lastSx != Math.round(from.x * 2).toInt() || lastSy != Math.round(from.y * 2)
                .toInt() || lastSz != Math.round(from.z * 2).toInt()
        ) open.clear()
        lastSx = Math.round(from.x * 2).toInt()
        lastSy = Math.round(from.y * 2).toInt()
        lastSz = Math.round(from.z * 2).toInt()
        val startNode = openNode(dx, dy, dz)
        val goalNode = openNode(lastSx, lastSy, lastSz)
        startNode.g = 0f
        startNode.f = 0f
        goalNode.g = Int.MAX_VALUE.toFloat()
        goalNode.f = Int.MAX_VALUE.toFloat()
        if (goalNode.parent != null) {
            val route = LinkedList<Vector3d>()
            var curr: Node? = goalNode
            while (curr!!.parent != null) {
                route.addLast(
                    Vector3d(
                        curr.coordinate.x / 2.0,
                        curr.coordinate.y / 2.0 + 0.1,
                        curr.coordinate.z / 2.0
                    )
                )
                curr = curr.parent
            }
            route.addLast(
                Vector3d(
                    curr.coordinate.x / 2.0,
                    curr.coordinate.y / 2.0 + 0.1,
                    curr.coordinate.z / 2.0
                )
            )
            this.route = route
            return true
        }
        open.add(startNode)
        val end = System.currentTimeMillis() + timeout
        while (!open.isEmpty()) {
            if (System.currentTimeMillis() > end) {
                return false
            }
            val n = open.poll()
            if (n != null) {
                if (n.lastVisited == pfindIdx) continue
                n.lastVisited = pfindIdx
                if (n === goalNode) {
                    // route = reconstructPath(startNode)
                    val route = LinkedList<Vector3d>()
                    var curr: Node? = goalNode
                    while (curr!!.parent != null) {
                        route.addLast(
                            Vector3d(
                                curr.coordinate.x / 2.0,
                                curr.coordinate.y / 2.0 + 0.1,
                                curr.coordinate.z / 2.0
                            )
                        )
                        curr = curr.parent
                    }
                    route.addLast(
                        Vector3d(
                            curr.coordinate.x / 2.0,
                            curr.coordinate.y / 2.0 + 0.1,
                            curr.coordinate.z / 2.0
                        )
                    )
                    this.route = route
                    return true
                }
            }
            for (value in EnumFacing.VALUES) {
                val neighbor = openNode(
                    n?.coordinate!!.x + value.frontOffsetX,
                    n.coordinate.y + value.frontOffsetY,
                    n.coordinate.z + value.frontOffsetZ
                )

                // check blocked.
                if (!(destinationBB.minX <= neighbor.coordinate.x && neighbor.coordinate.x <= destinationBB.maxX && destinationBB.minY <= neighbor.coordinate.y && neighbor.coordinate.y <= destinationBB.maxY && destinationBB.minZ <= neighbor.coordinate.z && neighbor.coordinate.z <= destinationBB.maxZ // near destination
                            || !roomAccessor.isBlocked(
                        neighbor.coordinate.x,
                        neighbor.coordinate.y,
                        neighbor.coordinate.z
                    ))
                ) { // not blocked
                    continue
                }
                val gScore = n.g + 1 // altho it's sq, it should be fine
                if (gScore < neighbor.g) {
                    neighbor.parent = n
                    neighbor.g = gScore
                    neighbor.f = gScore + distSq(
                        (goalNode.coordinate.x - neighbor.coordinate.x).toFloat(),
                        (goalNode.coordinate.y - neighbor.coordinate.y).toFloat(),
                        (goalNode.coordinate.z - neighbor.coordinate.z).toFloat()
                    )
                    open.add(neighbor)
                } else if (neighbor.lastVisited != pfindIdx) {
                    neighbor.f = gScore + distSq(
                        (goalNode.coordinate.x - neighbor.coordinate.x).toFloat(),
                        (goalNode.coordinate.y - neighbor.coordinate.y).toFloat(),
                        (goalNode.coordinate.z - neighbor.coordinate.z).toFloat()
                    )
                    open.add(neighbor)
                }
            }
        }
        return true
    }

    private var pfindIdx = 0


    private fun openNode(x: Int, y: Int, z: Int): Node {
        val coordinate = AStarUtil.Coordinate(x, y, z)
        var node = nodeMap[coordinate]
        if (node == null) {
            node = Node(coordinate)
            nodeMap[coordinate] = node
        }
        return node
    }


    private fun distSq(x: Float, y: Float, z: Float): Float {
        return MathHelper.sqrt_float(x * x + y * y + z * z)
    }

}