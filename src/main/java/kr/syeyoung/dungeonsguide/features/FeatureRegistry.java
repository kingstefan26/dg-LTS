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

package kr.syeyoung.dungeonsguide.features;

import kr.syeyoung.dungeonsguide.features.impl.debug.FeatureDebuggableMap;
import kr.syeyoung.dungeonsguide.features.impl.debug.FeatureRoomCoordDisplay;
import kr.syeyoung.dungeonsguide.features.impl.debug.FeatureRoomDebugInfo;
import kr.syeyoung.dungeonsguide.features.impl.dungeon.FeatureWarningOnPortal;
import kr.syeyoung.dungeonsguide.features.impl.cosmetics.FeatureNicknameColor;
import kr.syeyoung.dungeonsguide.features.impl.cosmetics.FeatureNicknamePrefix;
import kr.syeyoung.dungeonsguide.features.impl.misc.playerpreview.FeatureViewPlayerStatsOnJoin;
import kr.syeyoung.dungeonsguide.features.impl.dungeon.secret.FeatureCreateRefreshLine;
import kr.syeyoung.dungeonsguide.features.impl.dungeon.secret.PathfindLineProperties;
import kr.syeyoung.dungeonsguide.features.impl.dungeon.secret.mechanicbrowser.FeatureMechanicBrowse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureRegistry {
    @Getter
    private static final List<AbstractFeature> featureList = new ArrayList<AbstractFeature>();
    private static final Map<String, AbstractFeature> featureByKey = new HashMap<String, AbstractFeature>();
    @Getter
    private static final Map<String, List<AbstractFeature>> featuresByCategory = new HashMap<String, List<AbstractFeature>>();
    @Getter
    private static final Map<String, String> categoryDescription = new HashMap<>();
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_GLOBAL;
    public static FeatureMechanicBrowse SECRET_BROWSE;
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_SECRET_BROWSER;
    public static FeatureCreateRefreshLine SECRET_CREATE_REFRESH_LINE;
    public static PathfindLineProperties SECRET_BLOOD_RUSH_LINE_PROPERTIES;
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_AUTOPATHFIND;
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_PATHFINDALL_PARENT;
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_PATHFINDALL_BAT;
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_PATHFINDALL_CHEST;
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_PATHFINDALL_ESSENCE;
    public static PathfindLineProperties SECRET_LINE_PROPERTIES_PATHFINDALL_ITEM_DROP;
    public static SimpleFeature DUNGEON_INTERMODCOMM;
    public static FeatureWarningOnPortal BOSSFIGHT_WARNING_ON_PORTAL;
    public static FeatureViewPlayerStatsOnJoin PARTYKICKER_VIEWPLAYER;
    public static FeatureNicknamePrefix COSMETIC_PREFIX;
    public static FeatureNicknameColor COSMETIC_NICKNAMECOLOR;
    public static FeatureRoomDebugInfo ADVANCED_DEBUG_ROOM;
    public static FeatureDebuggableMap ADVANCED_DEBUGGABLE_MAP;
    public static FeatureRoomCoordDisplay ADVANCED_COORDS;

    public static <T extends AbstractFeature> T register(T abstractFeature) {
        if (featureByKey.containsKey(abstractFeature.getKey()))
            throw new IllegalArgumentException("DUPLICATE FEATURE DEFINITION");
        featureList.add(abstractFeature);
        featureByKey.put(abstractFeature.getKey(), abstractFeature);
        List<AbstractFeature> features = featuresByCategory.get(abstractFeature.getCategory());
        if (features == null)
            features = new ArrayList<AbstractFeature>();
        features.add(abstractFeature);
        featuresByCategory.put(abstractFeature.getCategory(), features);

        return abstractFeature;
    }

    public void init() {
        try {
            SECRET_LINE_PROPERTIES_GLOBAL = register(new PathfindLineProperties("Dungeon.Secrets.Preferences", "Global Line Settings", "Global Line Settings", "secret.lineproperties.global", true, null));
            SECRET_CREATE_REFRESH_LINE = register(new FeatureCreateRefreshLine());
            SECRET_LINE_PROPERTIES_SECRET_BROWSER = register(new PathfindLineProperties("Dungeon.Secrets.Secret Browser", "Line Settings", "Line Settings when pathfinding using Secret Browser", "secret.lineproperties.secretbrowser", true, SECRET_LINE_PROPERTIES_GLOBAL));
            SECRET_BROWSE = register(new FeatureMechanicBrowse());
            categoryDescription.put("ROOT.Secrets.Keybinds", "Useful keybinds / Toggle Pathfind lines, Freeze Pathfind lines, Refresh pathfind line or Trigger pathfind (you would want to use it, if you're using Pathfind to All)");
            SECRET_LINE_PROPERTIES_PATHFINDALL_PARENT = register(new PathfindLineProperties("Dungeon.Secrets.Pathfind To All", "Parent Line Settings", "Line Settings to be used by default", "secret.lineproperties.apf.parent", false, SECRET_LINE_PROPERTIES_GLOBAL));
            DUNGEON_INTERMODCOMM = register(new SimpleFeature("Dungeon.Teammates", "Communicate With Other's Dungeons Guide", "Sends total secret in the room to others\nSo that they can use the data to calculate total secret in dungeon run\n\nThis automates player chatting action, (chatting data) Thus it might be against hypixel's rules.\nBut mods like auto-gg which also automate player action and is kinda allowed mod exist so I'm leaving this feature.\nThis option is use-at-your-risk and you'll be responsible for ban if you somehow get banned because of this feature\n(Although it is not likely to happen)\nDefaults to off", "dungeon.intermodcomm", false));
            SECRET_LINE_PROPERTIES_PATHFINDALL_ITEM_DROP = register(new PathfindLineProperties("Dungeon.Secrets.Pathfind To All", "Item Drop Line Settings", "Line Settings when pathfind to Item Drop, when using above feature", "secret.lineproperties.apf.itemdrop", true, SECRET_LINE_PROPERTIES_PATHFINDALL_PARENT));
            SECRET_LINE_PROPERTIES_PATHFINDALL_ESSENCE = register(new PathfindLineProperties("Dungeon.Secrets.Pathfind To All", "Essence Line Settings", "Line Settings when pathfind to Essence, when using above feature", "secret.lineproperties.apf.essence", true, SECRET_LINE_PROPERTIES_PATHFINDALL_PARENT));
            SECRET_LINE_PROPERTIES_PATHFINDALL_CHEST = register(new PathfindLineProperties("Dungeon.Secrets.Pathfind To All", "Chest Line Settings", "Line Settings when pathfind to Chest, when using above feature", "secret.lineproperties.apf.chest", true, SECRET_LINE_PROPERTIES_PATHFINDALL_PARENT));
            SECRET_LINE_PROPERTIES_PATHFINDALL_BAT = register(new PathfindLineProperties("Dungeon.Secrets.Pathfind To All", "Bat Line Settings", "Line Settings when pathfind to Bat, when using above feature", "secret.lineproperties.apf.bat", true, SECRET_LINE_PROPERTIES_PATHFINDALL_PARENT));
            SECRET_LINE_PROPERTIES_AUTOPATHFIND = register(new PathfindLineProperties("Dungeon.Secrets.Legacy AutoPathfind", "Line Settings", "Line Settings when pathfinding using above features", "secret.lineproperties.autopathfind", true, SECRET_LINE_PROPERTIES_GLOBAL));
            SECRET_BLOOD_RUSH_LINE_PROPERTIES = register(new PathfindLineProperties("Dungeon.Secrets.Blood Rush", "Blood Rush Line Settings", "Line Settings to be used", "secret.lineproperties.bloodrush", false, SECRET_LINE_PROPERTIES_GLOBAL));


            BOSSFIGHT_WARNING_ON_PORTAL = register(new FeatureWarningOnPortal());
            PARTYKICKER_VIEWPLAYER = register(new FeatureViewPlayerStatsOnJoin());

            COSMETIC_NICKNAMECOLOR = register(new FeatureNicknameColor());
            COSMETIC_PREFIX = register(new FeatureNicknamePrefix());

            ADVANCED_DEBUG_ROOM = register(new FeatureRoomDebugInfo());
            ADVANCED_DEBUGGABLE_MAP = register(new FeatureDebuggableMap());


            ADVANCED_COORDS = register(new FeatureRoomCoordDisplay());
        } catch (Exception e) {
            System.out.println(e);
        }


        for (AbstractFeature abstractFeature : featureList) {
            if (abstractFeature == null) {
                throw new IllegalStateException("Feature " + abstractFeature.getKey() + " is null, this cannot happen!!!");
            }
        }

    }
}
