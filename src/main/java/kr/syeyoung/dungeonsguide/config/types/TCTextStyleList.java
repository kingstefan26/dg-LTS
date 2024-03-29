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

package kr.syeyoung.dungeonsguide.config.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import kr.syeyoung.dungeonsguide.features.text.TextStyle;

import java.util.ArrayList;
import java.util.List;

public class TCTextStyleList implements TypeConverter<List<TextStyle>> {
    @Override
    public String getTypeString() {
        return "list_textStyle";
    }

    @Override
    public List<TextStyle>  deserialize(JsonElement element) {
        JsonArray arr = element.getAsJsonArray();
        TypeConverter<TextStyle> conv = TypeConverterRegistry.getTypeConverter("textstyle", TextStyle.class);
        List<TextStyle> texts = new ArrayList<TextStyle>();
        for (JsonElement element2:arr) {
            texts.add(conv.deserialize(element2));
        }
        return texts;
    }

    @Override
    public JsonElement serialize(List<TextStyle> element) {
        JsonArray array = new JsonArray();
        TypeConverter<TextStyle> conv = TypeConverterRegistry.getTypeConverter("textstyle", TextStyle.class);
        for (TextStyle st:element) {
            array.add(conv.serialize(st));
        }
        return array;
    }
}
