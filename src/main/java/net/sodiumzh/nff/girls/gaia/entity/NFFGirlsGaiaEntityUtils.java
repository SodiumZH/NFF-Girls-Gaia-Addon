package net.sodiumzh.nff.girls.gaia.entity;

import gaia.entity.AbstractGaiaEntity;
import net.sodiumzh.nfu.object.CastableObject;
import net.sodiumzh.nfu.object.ICastable;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NFFGirlsGaiaEntityUtils {

    private static final Map<Class<? extends AbstractGaiaEntity>, Optional<Method>> IS_MALE = new HashMap<>();
    private static final Map<Class<? extends AbstractGaiaEntity>, Optional<Method>> SET_MALE = new HashMap<>();

    public static boolean isMale(AbstractGaiaEntity mob) {
        if (!IS_MALE.containsKey(mob.getClass())) {
            IS_MALE.put(mob.getClass(), NFUReflectionStatics.findPublicMethodIfInherited(mob.getClass(), "isMale"));
        }
        return IS_MALE.get(mob.getClass()).map(m -> NFUReflectionStatics.invokeMethod(m, mob).castTo(Boolean.class))
            .orElse(false);
    }

    public static void setMale(AbstractGaiaEntity mob, boolean value) {
        if (!SET_MALE.containsKey(mob.getClass())) {
            SET_MALE.put(mob.getClass(), NFUReflectionStatics.findPublicMethodIfInherited(mob.getClass(), "setMale", boolean.class));
        }
        SET_MALE.get(mob.getClass()).ifPresent(m -> NFUReflectionStatics.invokeMethod(m, mob, value));
    }
}
