package com.booleanuk.cohorts.payload.response;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Collection;
import java.util.Map;

public class AnyData<T> extends Data<T> {
    private T data;

    @Override
    public void set(T item) {
        this.data = item;
    }

    @JsonAnyGetter
    public Map<String, Object> getJson() {
        String fieldName = deriveFieldName(data);
        return Map.of(fieldName, data);
    }

    private String deriveFieldName(Object obj) {
        if (obj == null) return "null";

        if (obj instanceof Collection<?> collection && !collection.isEmpty()) {
            Object first = collection.iterator().next();
            return pluralize(simpleName(first.getClass()));
        }

        if (obj.getClass().isArray()) {
            Object first = java.lang.reflect.Array.getLength(obj) > 0
                    ? java.lang.reflect.Array.get(obj, 0)
                    : null;
            return first != null ? pluralize(simpleName(first.getClass())) : "items";
        }

        return deriveFromClass(obj.getClass());
    }

    private String deriveFromClass(Class<?> clazz) {
        JsonRootName ann = clazz.getAnnotation(JsonRootName.class);
        if (ann != null) {
            return ann.value();
        }
        return simpleName(clazz);
    }

    private String simpleName(Class<?> clazz) {
        String simple = clazz.getSimpleName();
        return Character.toLowerCase(simple.charAt(0)) + simple.substring(1);
    }

    private String pluralize(String word) {
        if (word.endsWith("y") && !word.endsWith("ay") && !word.endsWith("ey") && !word.endsWith("oy") && !word.endsWith("uy")) {
            return word.substring(0, word.length() - 1) + "ies";
        }
        if (word.endsWith("s")) {
            return word + "es";
        }
        return word + "s";
    }
}
