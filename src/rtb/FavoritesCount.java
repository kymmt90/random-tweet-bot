/*
 * Copyright 2014 Kohei Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rtb;

class FavoritesCount {
    private int favoritesCount;

    FavoritesCount(int favoritesCount) {
        if (favoritesCount < 0) throw new IllegalArgumentException();
        this.favoritesCount = favoritesCount;
    }

    void addTo(StringBuilder builder) {
        if (builder == null) throw new NullPointerException();
        builder.append("[")
               .append(this.toString())
               .append(" ")
               .append(getUnit())
               .append("]");
    }

    int toValue() {
        return favoritesCount;
    }

    @Override
    public String toString() {
        return String.valueOf(favoritesCount);
    }

    private String getUnit() {
        return favoritesCount > 1 ? "likes" : "like";
    }
}
