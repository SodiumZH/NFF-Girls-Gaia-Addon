package net.sodiumzh.nff.girls.gaia.entity;

import gaia.entity.AbstractGaiaEntity;
import net.sodiumzh.nfu.object.CastableObject;
import net.sodiumzh.nfu.object.ICastable;
import net.sodiumzh.nfu.reflection.CachedFieldAccessor;
import net.sodiumzh.nfu.reflection.CachedMethodAccessor;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NFFGirlsGaiaEntityUtils {

    private static final CachedMethodAccessor IS_MALE = new CachedMethodAccessor("isMale", false);
    private static final CachedMethodAccessor SET_MALE = new CachedMethodAccessor("setMale", false, boolean.class);

    public static boolean isMale(AbstractGaiaEntity mob) {
        return IS_MALE.invokeIfPresent(mob, mob.getClass()).castOptional(Boolean.class).orElse(false);
    }

    public static void setMale(AbstractGaiaEntity mob, boolean value) {
        SET_MALE.invokeIfPresent(mob, mob.getClass(), value);
    }
}
