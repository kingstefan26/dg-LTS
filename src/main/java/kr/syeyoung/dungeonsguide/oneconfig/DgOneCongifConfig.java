package kr.syeyoung.dungeonsguide.oneconfig;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.*;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import kr.syeyoung.dungeonsguide.DungeonsGuide;
import kr.syeyoung.dungeonsguide.Main;
import kr.syeyoung.dungeonsguide.chat.ChatTransmitter;
import kr.syeyoung.dungeonsguide.config.types.AColor;
import kr.syeyoung.dungeonsguide.dungeon.DungeonContext;
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.impl.GeneralRoomProcessor;
import kr.syeyoung.dungeonsguide.dungeon.roomprocessor.RoomProcessor;
import kr.syeyoung.dungeonsguide.features.impl.debug.TestMap;
import kr.syeyoung.dungeonsguide.features.impl.dungeon.huds.FeatureDungeonMap;
import kr.syeyoung.dungeonsguide.oneconfig.dungeon.DungeonMapPage;
import kr.syeyoung.dungeonsguide.oneconfig.dungeon.HideAnimalPage;
import kr.syeyoung.dungeonsguide.oneconfig.dungeon.huds.*;
import kr.syeyoung.dungeonsguide.oneconfig.huds.*;
import kr.syeyoung.dungeonsguide.oneconfig.misc.DisableMessagePage;
import kr.syeyoung.dungeonsguide.oneconfig.misc.HighlightMobsPage;
import kr.syeyoung.dungeonsguide.oneconfig.secrets.AutoPathfindPage;
import kr.syeyoung.dungeonsguide.oneconfig.secrets.BloodRushPage;
import kr.syeyoung.dungeonsguide.oneconfig.secrets.FairySoulWarningPage;
import kr.syeyoung.dungeonsguide.oneconfig.secrets.PathfindToALlPage;
import kr.syeyoung.dungeonsguide.oneconfig.solvers.*;

import java.util.function.Supplier;

public class DgOneCongifConfig extends Config {




    @Switch(
            name = "enabled",
            description = "Box Real Livid in bossfight",
            category = "Solvers",
            subcategory = "Box Real Livid"
    )
    public static boolean boxRealLivid = true;

    @Color(
            name = "Box color",
            category = "Solvers",
            subcategory = "Box Real Livid"
    )
    public static OneColor realLividColor = new OneColor(0, 255, 0, 150);

    @Switch(
            name = "Simon Says Solver",
            description = "Solver for Simon says puzzle",
            category = "Solvers"
    )
    public static boolean simonySaysSolver = true;

    @Switch(
            name = "F7 GUI Terminal Solver",
            description = "Solve f7 gui terminals. (color, startswith, order, navigate, correct panes)",
            category = "Solvers"
    )
    public static boolean terminalSolver = true;

    @Switch(
            name = "Creeper",
            description = "Draws line between prismarine lamps in creeper room",
            category = "Solvers"
    )
    public static boolean creeperSolver = true;

    @Switch(
            name = "Waterboard (Advanced)",
            description = "Calculates solution for waterboard puzzle and displays it to user",
            category = "Solvers"
    )
    public static boolean waterboardSolver = true;
    @Switch(
            name = "Blaze Solver",
            size = 2,
            description = "Highlights the blaze that needs to be killed in an blaze room",
            category = "Solvers",
            subcategory = "Blaze Solver"
    )
    public static boolean blazeSolver = true;

    @Color(
            name = "Normal Blaze Color",
            category = "Solvers",
            subcategory = "Blaze Solver"
    )
    public static OneColor blazeSolverColorNormal = new OneColor(255,255,255, 255);

    @Color(
            name = "Next Blaze Color",
            category = "Solvers",
            subcategory = "Blaze Solver"
    )
    public static OneColor blazeSolverColorNext = new OneColor(0,255,0, 255);

    @Color(
            name = "Next up Blaze Color",
            category = "Solvers",
            subcategory = "Blaze Solver"
    )
    public static OneColor blazeSolverColorNextUp = new OneColor(255,255,0, 255);

    @Color(
            name = "Blaze Border Color",
            category = "Solvers",
            subcategory = "Blaze Solver"
    )
    public static OneColor blazeSolverBorder = new OneColor(255,255,255, 0);








    @Switch(
            name = "Bomb Defuse",
            size = 2,
            description = "Communicates with others dg using key 'F' for solutions and displays it",
            category = "Solvers",
            subcategory = "Bomb Defuse Solver"
    )
    public static boolean bombDefuseSolver = false;

    @KeyBind(
            category = "Solvers",
            name = "keybind",
            description = "Press to send solution in chat",
            subcategory = "Bomb Defuse Solver"
    )
    public static OneKeyBind bombDefuseSolverKeybind = new OneKeyBind(UKeyboard.KEY_NONE);





    @Switch(
            name = "enabled",
            size = 2,
            description = "Calculates solution for box puzzle room, and displays it to user",
            category = "Solvers",
            subcategory = "Box (Advanced)"
    )
    public static boolean solverBox = true;


    @Switch(
            name = "disableText",
            description = "Box Puzzle Solver Disable text\", \"Disable 'Type recalc to recalculate solution' showing up on top left.\\nYou can still type recalc to recalc solution after disabling this feature",
            category = "Solvers",
            subcategory = "Box (Advanced)"
    )
    public static boolean solverBoxDisableText = false;
    @Slider(
            name = "Line Thickness",
            min = 0,
            max = 10,
            description = "Thickness of the solution line",
            category = "Solvers",
            subcategory = "Box (Advanced)"
    )
    public static float solverBoxLinewidth = 1.0F;
    @Color(
            name = "Line Color",
            description = "Color of the solution line",
            category = "Solvers",
            subcategory = "Box (Advanced)"
    )
    public static OneColor solverBoxLineColor = new OneColor(0xFF00FF00);
    @Color(
            name = "Target Color",
            description = "Color of the target button",
            category = "Solvers",
            subcategory = "Box (Advanced)"
    )
    public static OneColor solverBoxTargetColor = new OneColor(0x5500FFFF);
    @Color(
            name = "Text Color Next Step",
            description = "Color of the text (next step)",
            category = "Solvers",
            subcategory = "Box (Advanced)"
    )
    public static OneColor solverBoxTextColorNextStep = new OneColor(0xFF00FF00);
    @Color(
            name = "Text Color Next Step",
            description = "Color of the text (others)",
            category = "Solvers",
            subcategory = "Box (Advanced)"
    )
    public static OneColor solverBoxTextColor = new OneColor(0xFF000000);





    @Switch(
            name = "enabled",
            size = 2,
            description = "Calculates solution for icepath puzzle and displays it to user",
            category = "Solvers",
            subcategory = "Icepath (Advanced)"
    )
    public static boolean iceFill = true;

    @Page(
            name = "options",
            location = PageLocation.BOTTOM,
            category = "Solvers",
            subcategory = "Icepath (Advanced)"
    )
    public IceFillPage testPage2 = new IceFillPage();


    @Switch(
            name = "enabled",
            size = 2,
            description = "Highlights the correct solution for trivia puzzle",
            category = "Solvers",
            subcategory = "Quiz"
    )
    public static boolean kahootSolver = true;

    @Page(
            name = "options",
            location = PageLocation.BOTTOM,
            category = "Solvers",
            subcategory = "Quiz"
    )
    public KahootPage a = new KahootPage();

    @Switch(
            name = "enabled",
            size = 2,
            description = "Highlights the correct box after clicking on all 3 weirdos",
            category = "Solvers",
            subcategory = "3 weirdos Solver"
    )
    public static boolean riddleSolver = true;
    @Page(
            name = "options",
            location = PageLocation.BOTTOM,
            category = "Solvers",
            subcategory = "3 weirdos Solver"
    )
    public ThreeWeirdosSolverPage b = new ThreeWeirdosSolverPage();


    @Switch(
            name = "enabled",
            size = 2,
            description = "Actively calculates solution for silverfish puzzle and displays it to user",
            category = "Solvers",
            subcategory = "Silverfish (Advanced)"
    )
    public static boolean silverFishSolver = true;
    @Page(
            name = "options",
            location = PageLocation.BOTTOM,
            category = "Solvers",
            subcategory = "Silverfish (Advanced)"
    )
    public SilverfishPage c = new SilverfishPage();


    @Switch(
            name = "enabled",
            size = 2,
            description = "Shows teleport pads you've visited in a teleport maze room",
            category = "Solvers",
            subcategory = "Teleport"
    )
    public static boolean teleportSolver = true;
    @Page(
            name = "options",
            location = PageLocation.BOTTOM,
            category = "Solvers",
            subcategory = "Teleport"
    )
    public TeleportSolverPage d = new TeleportSolverPage();

    @Switch(
            name = "enabled",
            size = 2,
            description = "Shows the best move that could be taken by player in the tictactoe room",
            category = "Solvers",
            subcategory = "Tictactoe"
    )
    public static boolean ticktaktoeSolver = true;
    @Page(
            name = "options",
            location = PageLocation.BOTTOM,
            category = "Solvers",
            subcategory = "Tictactoe"
    )
    public TicktackToeSolverPage e = new TicktackToeSolverPage();


    @Switch(
            name = "enabled",
            size = 2,
            description = "See players through walls",
            category = "Dungeon",
            subcategory = "Player ESP"
    )
    public static boolean playerEps = true;

    @Checkbox(
            name = "Hide Mob nametags",
            size = 2,
            description = "Hide mob nametags in dungeon",
            category = "Dungeon",
            subcategory = "Mobs"
    )
    public static boolean hideMobNametags = true;

    @Checkbox(
            name = "Highlight bats",
            category = "Dungeon",
            subcategory = "Mobs"
    )
    public static boolean highlightBats = true;

    @Checkbox(
            name = "Highlight Starred mobs",
            category = "Dungeon",
            subcategory = "Mobs"
    )
    public static boolean highlightStaredMobs = true;

    @Checkbox(
            name = "Highlight Skeleton Masters",
            category = "Dungeon",
            subcategory = "Mobs"
    )
    public static boolean highlightSkeletonMasters = true;

    @Page(
            name = "prefrences",
            location = PageLocation.BOTTOM,
            category = "Dungeon",
            subcategory = "Mobs"
    )
    static HighlightMobsPage hb = new HighlightMobsPage();

    @Page(
            name = "Hide Animals",
            description = "Hide Spirit Animals on F4. Click on Edit for precise setting",
            location = PageLocation.BOTTOM,
            category = "Dungeon"
    )
    static HideAnimalPage ha = new HideAnimalPage();


    @Page(
            name = "Ping",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public PingPage ss = new PingPage();

    @Page(
            name = "Boss Health",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public BossHealthPage aa = new BossHealthPage();

    @Page(
            name = "Dungeon Deaths Display",
            description = "Display names of player and death count in dungeon run",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public DeathDisplayPage dd = new DeathDisplayPage();

    @Page(
            name = "Display Total # of Secrets",
            description = "Display how much total secrets have been found in a dungeon run.\\n+ sign means DG does not know the correct number, but it's somewhere above that number.",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public DungeonSecretsPage ds = new DungeonSecretsPage();

    @Page(
            name = "Display # of Crypts",
            description = "Display how much total crypts have been blown up in a dungeon run",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public CryptsDisplayPage fdt = new CryptsDisplayPage();

    @Page(
            name = "Display Real Time-Dungeon Time",
            description = "Display how much real time has passed since dungeon run started",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public DungeonRealTimePage drt = new DungeonRealTimePage();

    @Page(
            name = "Display # Secrets in current room",
            description = "Display what your actionbar says",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public CurrentRoomDungeonSecretsPage crds = new CurrentRoomDungeonSecretsPage();

    @Page(
            name = "Display Current Class Milestone",
            description = "Display current class milestone of yourself",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public DungeonMilestonePage dm = new DungeonMilestonePage();

    @Page(
            name = "Low Health Warning",
            description = "Warn if someone is on low health",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public WarnLowHealthPage wlh = new WarnLowHealthPage();

    @Page(
            name = "Display Ingame Dungeon Time",
            description = "Display how much time skyblock thinks has passed since dungeon run started",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public DungeonSBTimePage dsbt = new DungeonSBTimePage();

    @Page(
            name = "Watcher Spawn Alert",
            description = "Alert when watcher says 'That will be enough for now'",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public WacherWarningPage ww = new WacherWarningPage();

    @Page(
            name = "Party List",
            description = "Party List as GUI",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public PartyListPage plp = new PartyListPage();


    @Page(
            name = "Display Current Score",
            description = "Calculate and Display current score. This data is from pure calculation and can be different from actual score.",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public DungeonScorePage dsc = new DungeonScorePage();

    @Page(
            name = "Action Viewer",
            description = "View List of actions that needs to be taken",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public ActionsViewerPage avp = new ActionsViewerPage();

    @Page(
            name = "Party Ready List",
            description = "Check if your party member have said r or not",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    public PartyReadyPage pr = new PartyReadyPage();

    @Switch(
            name = "Show Profit of Dungeon Reward Chests",
            description = "Show Profit of Dungeon Chests",
            size = 2,
            category = "Misc"
    )
    public static boolean chestProfit = true;






    @Switch(
            name = "enabled",
            size = 2,
            description = "Automatically accept reparty",
            category = "Misc",
            subcategory = "Auto accept reparty"
    )
    public static boolean autoRp = true;


    @Switch(
            name = "Spirit Boots Fixer",
            description = "Fix Spirit boots messing up with inventory",
            category = "Misc"
    )
    public static boolean spiritBootsFix = true;

    @Switch(
            name = "Collect Speed Score",
            description = "Collect Speed score, run time, and floor and send that to developer's server for speed formula. This data is completely anonymous, opt out of the feature by disabling this feature",
            category = "Misc"
    )
    public static boolean collectSpeedScore = true;

    @Switch(
            name = "Custom Party Finder",
            description = "Custom Party Finder",
            category = "Misc"
    )
    public static boolean customPartyfinder = true;
    @Switch(
            name = "Highlight parties in party viewer",
            description = "Highlight parties you can't join with red",
            category = "Misc"
    )
    public static boolean featureGoodParties = true;
    @Switch(
            name = "Copy Chat Messages",
            description = "Click on copy to copy",
            category = "Misc"
    )
    public static boolean copyChatMesseges = false;

    @Text(
            name = "Hypixel API key",
            secure = true,
            category = "Misc"
    )
    public static String apikey = "";

    @Switch(
            name = "Make everyone a penguin !!",
            description = "Awwww",
            category = "Misc"
    )
    public static boolean penguins = false;




    @Switch(
            name = "Enabled",
            size = 2,
            category = "Misc",
            subcategory = "Press Any Mouse Button or Key to close Secret Chest"
    )
    public static boolean closeChestHelper = true;
    @Switch(
            name = "enabled",
            size = 2,
            description = "Decreases volume of explosions while on skyblock",
            category = "Misc",
            subcategory = "Decrease Explosion sound effect"
    )
    public static boolean decreseExplosionSound = true;
    @Slider(
            name = "Sound Multiplier %",
            min = 1F,
            max = 100F,
            description = "The volume of explosion effect will be multiplied by this value. 0~100",
            category = "Misc",
            subcategory = "Decrease Explosion sound effect"
    )
    public static float explosionDecreseMultiplyer = 10F;


    @Page(
            name = "Settings",
            location = PageLocation.BOTTOM,
            description = "Do not let ability messages show up in chatbox\nclick on Edit for more precise settings",
            category = "Misc",
            subcategory = "Disable ability messages"
    )
    public DisableMessagePage aaa = new DisableMessagePage();


    @Page(
            name = "Epic Dungeon Start Countdown",
            location = PageLocation.BOTTOM,
            description = "Shows a cool dungeon start instead of the chat messages",
            category = "HUD"
    )
    private static DungeonMapPage mp = new DungeonMapPage();



    @Switch(
            name = "enabled",
            category = "Misc",
            subcategory = "Reparty Command From DG"
    )
    public static boolean repartyCommand = false;
    @Info(text = "if you disable, /dg reparty will still work, Auto reparty will still work\nRequires Restart to get applied", type = InfoType.INFO, category = "Misc", subcategory = "Reparty Command From DG")
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @Text(
            name = "The Command",
            placeholder = "reparty",
            category = "Misc",
            subcategory = "Reparty Command From DG"
    )
    public static String reparty = "reparty";



    @Switch(
            name = "enabled",
            description = "Shows quality of dungeon items (floor, percentage)",
            size = 2,
            category = "Misc",
            subcategory = "Dungeon Item Stats"
    )
    public static boolean dungeonStat = false;

    @Switch(
            name = "Require Shift",
            description = "If shift needs to be pressed in order for this feature to be activated",
            category = "Misc",
            subcategory = "Dungeon Item Stats"
    )
    public static boolean tooltipPrice = false;



    @Page(
            name = "Dungeon Map",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static DungeonMapPage map = new DungeonMapPage();

    @Page(
            name = "CooldownCounter",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static CooldownCounterPage cc = new CooldownCounterPage();

    @Page(
            name = "Terracota timer",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static TerracotaTimerPage tt = new TerracotaTimerPage();

    @Page(
            name = "Display Spirit Bear Summon Percentage",
            description = "Displays spirit bear summon percentage in hud",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static SpiritBearPrctPage sbp = new SpiritBearPrctPage();

    @Page(
            name = "Display name of the room you are in",
            description = "Display name of the room you are in",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static CurrentRoomNamePage crn = new CurrentRoomNamePage();


    @Page(
            name = "Secret Soul Alert",
            description = "Alert if there is an fairy soul in the room",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static FairySoulWarningPage fsrw = new FairySoulWarningPage();

    @Page(
            name = "Display Spirit bow timer",
            description = "Displays how long until spirit bow gets destroyed",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static SpiritBowTimerPage sbt = new SpiritBowTimerPage();

    @Page(
            name = "View Ability Cooldowns",
            description = "A handy hud for viewing cooldown abilities",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static AboliityCooldownPage acd = new AboliityCooldownPage();

    @Page(
            name = "Display Current Phase",
            description = "Displays the current phase of bossfight",
            location = PageLocation.BOTTOM,
            category = "HUD"
    )
    private static CurrentBossPhasePage cbp = new CurrentBossPhasePage();




    @Checkbox(
            name = "Render beacons",
            category = "Secrets",
            subcategory = "Preferences"
    )
    public static boolean renderSecretBeacons = true;

    @Checkbox(
            name = "Render Destination text",
            category = "Secrets",
            subcategory = "Preferences"
    )
    public static boolean renderSecretDestText = true;


//    THETA_STAR("The default pathfinding algorithm. It will generate sub-optimal path quickly."),
//    A_STAR_DIAGONAL("New pathfinding algorithm. It will generate path that looks like the one JPS generates"),
//    A_STAR_FINE_GRID("New pathfinding algorithm. It will generate path that kind of looks like stair"),
//    JPS_LEGACY("The improved pathfinding algorithm. Not suggested for usage. It will have problems on diagonal movements, thus giving wrong routes")
    @Dropdown(
            description = "Select pathfinding algorithm used by paths",
            name = "Pathfinding Algorithm",
            options = {"THETA* (recommended)", "A* Diagonal", "A* Fine-Grid", "Jump Point Search"},
            category = "Secrets",
            subcategory = "Preferences"
    )
    public static int secretPathfindStrategy = 0;


    @KeyBind(
            name = "Toggle Pathfind Lines",
            description = "A key for toggling pathfound line visibility.\nPress settings to edit the key",
            category = "Secrets",
            subcategory = "Preferences"
    )
    public static OneKeyBind togglePathfindKeybind = new OneKeyBind(UKeyboard.KEY_NONE);

    public transient static boolean togglePathfindStatus = false;

    @KeyBind(
            name = "Freeze Pathfind",
            description = "Freeze Pathfind, meaning the pathfind lines won't change when you move.\nPress settings to edit the key",
            category = "Secrets",
            subcategory = "Preferences"
    )
    public static OneKeyBind freezePathfindingKeybinding = new OneKeyBind(UKeyboard.KEY_NONE);
    public transient static boolean freezePathfindingStatus = false;


    @Dropdown(
            name = "Mode",
            options = {"PathFind to All", "Blood Rush", "Auto pathfind"},
            category = "Secrets"
    )
    public static int secretFindMode = 2;



    @Page(
            name = "PathFind to All Mode Settings",
            location = PageLocation.BOTTOM,
            category = "Secrets"
    )
    public PathfindToALlPage pftap = new PathfindToALlPage();

    /**
     * This field is in addition to the selector, the user might want to disable the lines temporally and
     * changing the mode would not solve that
     */
    public static boolean bloodRush = false;

    @Page(
            name = "Blood Rush Mode Settings",
            location = PageLocation.BOTTOM,
            category = "Secrets"
    )
    public BloodRushPage brp = new BloodRushPage();


    @Page(
            name = "Auto pathfind Mode Settings",
            location = PageLocation.BOTTOM,
            category = "Secrets"
    )
    public AutoPathfindPage app = new AutoPathfindPage();


    void addDependences(Supplier<Boolean> condition, String... deps){
        for (String dep : deps) {
            addDependency(dep, condition);
        }
    }

    void hideMultipleIf(Supplier<Boolean> condition, String... deps){
        for (String dep : deps) {
            hideIf(dep, condition);
        }
    }

    public static AColor oneconftodgcolor(OneColor c){
        return new AColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public DgOneCongifConfig() {
        super(new Mod(Main.NAME, ModType.SKYBLOCK, "/dglts_logo_lightx512.png"), Main.MOD_ID+".json");
        initialize();

//        hideMultipleIf(() -> !DEBUG_MODE,
//                "DEBUGABLE_MAP",
//                "DEBUG_BLOCKCACHING",
//                "DEBUG_DUNGEON_CORDS",
//                "DEBUG_ROOMEDIT_KEYBIND",
//                "DEBUG_ROOM_EDIT",
//                "DEBUG_ROOM_INFO",
//                "DEBUG_TEST_PEPOLE",
//                "DEBUG_TEST_PEPOLE_SCALE",
//                "DUNGEON_CORDS_COLOR",
//                "DUNGEON_ROOMINFO_COLOR",
//                "TEST_MAP");

        hideMultipleIf(() -> !FeatureDungeonMap.centerMapOnPlayer, "shouldRotateWithPlayer");

        registerKeyBind(BloodRushPage.keybind, () -> {
            DgOneCongifConfig.bloodRush = !DgOneCongifConfig.bloodRush;
            ChatTransmitter.addToQueue( ChatTransmitter.PREFIX + "§fToggled Blood Rush to §e "+(DgOneCongifConfig.bloodRush ? "on":"off"));
        });

        registerKeyBind(togglePathfindKeybind, () -> {
            togglePathfindStatus = !togglePathfindStatus;
            try {
                ChatTransmitter.addToQueue( ChatTransmitter.PREFIX + "§fToggled Pathfind Line visibility to §e"+(togglePathfindStatus ? "on":"off"));
            } catch (Exception ignored) {

            }
        });

        registerKeyBind(freezePathfindingKeybinding, () -> {
            freezePathfindingStatus = !freezePathfindingStatus;
            ChatTransmitter.addToQueue(ChatTransmitter.PREFIX + "§fToggled Pathfind Freeze to §e"+(DgOneCongifConfig.freezePathfindingStatus ? "on":"off"));
        });

        addListener("secretFindMode", () -> {
            DungeonContext context = DungeonsGuide.getDungeonsGuide().getDungeonFacade().context;
            if(context != null){
                RoomProcessor roomProcessor = context.getCurrentRoomProcessor();
                if(roomProcessor instanceof GeneralRoomProcessor){
                    GeneralRoomProcessor smartCastMyAss = (GeneralRoomProcessor) roomProcessor;
                    smartCastMyAss.updateStrategy();
                }
            }
        });

    }





    @Switch(
            name = "Debug mode",
            category = "Debug"
    )
    public static boolean debugMode = false;
    @HUD(
            name = "Debug Map",
            category = "Debug"
    )
    public static TestMap testMap = new TestMap();
    @Switch(
            name = "Debuggable Map",
            category = "Debug"
    )
    public static boolean debugableMap = false;
    @Switch(
            name = "Block Caching",
            category = "Debug",
            description = "Cache all world.getBlockState calls"
    )
    public static boolean debugBlockcaching = true;

    @Checkbox(
            name = "Experimental autopathfind",
            category = "Debug",
            description = "Use pathfinding to calculate secret cost"
    )
    public static boolean usePathfindCostCacls = false;

    @Switch(
            description = "Display Coordinate Relative to the Dungeon Room and room's rotation",
            name = "Enabled",
            size = OptionSize.DUAL,
            category = "Debug",
            subcategory = "Dungeon cords"
    )
    public static boolean debugDungeonCords = false;

    @Color(
            name = "Color",
            category = "Debug",
            subcategory = "Dungeon cords"
    )
    public static OneColor dungeonCordsColor = new OneColor(255, 191, 0);


    @Switch(
            name = "Enabled",
            description = "Display Coordinate Relative to the Dungeon Room and room's rotation",
            category = "Debug",
            subcategory = "Dungeon Room Info"
    )
    public static boolean debugRoomInfo = false;
    @Color(
            name = "Color",
            category = "Debug",
            subcategory = "Dungeon Room Info"
    )
    public static OneColor dungeonRoominfoColor = new OneColor(255, 255, 255);


    @Switch(
            name = "Enabled",
            category = "Debug",
            subcategory = "Test People"
    )
    public static boolean debugTestPepole = false;

    @Slider(
            name = "Scale",
            min = 1F,
            max = 500F,
            category = "Debug",
            subcategory = "Test People"
    )
    public static float debugTestPepoleScale = 2F;


    @Switch(
            name = "Enabled",
            category = "Debug",
            description = "Allow editing dungeon rooms\n\nWarning: using this feature can break or freeze your Minecraft\nThis is only for advanced users only",
            subcategory = "Room Edit"
    )
    public static boolean debugRoomEdit = false;

    @Switch(
            name = "Visualise block get calls",
            category = "Debug"
    )
    public static boolean visualiseBlockGetCalls = false;

    @KeyBind(
            category = "Debug",
            name = "Gui keybind",
            subcategory = "Room Edit"
    )
    public static OneKeyBind debugRoomeditKeybind = new OneKeyBind(UKeyboard.KEY_R);
}
