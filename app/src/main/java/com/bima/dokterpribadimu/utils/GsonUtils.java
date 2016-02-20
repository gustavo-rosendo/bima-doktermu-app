package com.bima.dokterpribadimu.utils;

import com.google.gson.Gson;

/**
 * Created by apradanas on 2/21/16.
 */
public class GsonUtils {

    private static Gson gson;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * This method serializes the specified object into its equivalent Json representation.
     * @param object the object for which Json representation is to be created setting for Gson
     * @return Json representation of {@code object}.
     */
    public static String toJson(Object object) {
        checkGson();
        return gson.toJson(object);
    }

    /**
     * This method deserializes the specified Json into an object of the specified class.
     * @param <T> the type of the desired object
     * @param json the string from which the object is to be deserialized
     * @param clazz the class of T
     * @return an object of type T from the string. Returns {@code null} if {@code json} is {@code null}.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        checkGson();
        return gson.fromJson(json, clazz);
    }

    private static void checkGson() {
        if (gson == null) {
            gson = new Gson();
        }
    }
}
