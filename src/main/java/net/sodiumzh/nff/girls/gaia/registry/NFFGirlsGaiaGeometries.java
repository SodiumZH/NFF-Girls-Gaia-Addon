package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nfu.math.IFieldPattern3D;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;
import net.sodiumzh.nfu.util.NFUMathStatics;

public class NFFGirlsGaiaGeometries {

    public static final NFURegistryEntryCollection<IFieldPattern3D> FIELD_PATTERNS
        = NFURegistryEntryCollection.create(NFURegistries.FIELD_PATTERNS, NFFGirlsGaia.MOD_ID);

    public static final NFURegistry.Accessor<IFieldPattern3D> VORTEX = FIELD_PATTERNS.register("vortex", () ->
        v -> NFUMathStatics.rotateVectorY(new Vec3(v.x, 0, v.z), 150d).normalize());

}
