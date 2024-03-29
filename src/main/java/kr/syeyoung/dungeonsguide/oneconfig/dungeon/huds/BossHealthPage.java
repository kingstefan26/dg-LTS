package kr.syeyoung.dungeonsguide.oneconfig.dungeon.huds;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import kr.syeyoung.dungeonsguide.features.impl.dungeon.boss.FeatureBossHealth;

public class BossHealthPage {
    @Checkbox(
            description = "Show total health along with current health",
            name = "show total health"
    )
    public static boolean showTotalHealth = false;

    @Checkbox(
            description = "1234568 -> 1m",
            name = "format health"
    )
    public static boolean formatHealth = true;

    @Checkbox(
            description = "For example, do not show guardians health when they're not attackable",
            name = "Don't show health of in-attackable enemy"
    )
    public static boolean ignoreInattackable = false;


    @HUD(
            name = "settings"
    )
    public FeatureBossHealth bossHealth = new FeatureBossHealth();
}
