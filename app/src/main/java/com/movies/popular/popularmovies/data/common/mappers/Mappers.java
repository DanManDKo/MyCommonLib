package com.movies.popular.popularmovies.data.common.mappers;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/4/17
 * Time: 7:50 PM
 */

public final class Mappers {
    public Mappers() {
    }

    public static <F, T> List<T> mapCollection(@Nullable List<F> list, Mapper<F, T> mapper) {
        if(list == null) {
            return Collections.emptyList();
        } else {
            int size = list.size();
            ArrayList result = new ArrayList(size);

            for(int i = 0; i < size; ++i) {
                T map = mapper.map(list.get(i));
                if (map != null) {
                    result.add(map);
                }
            }

            return result;
        }
    }
}
