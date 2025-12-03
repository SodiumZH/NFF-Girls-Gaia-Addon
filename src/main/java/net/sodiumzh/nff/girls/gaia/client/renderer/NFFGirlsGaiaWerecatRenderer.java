package net.sodiumzh.nff.girls.gaia.client.renderer;

import gaia.client.renderer.WerecatRenderer;
import gaia.entity.Werecat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaSuccubusEntity;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaWerecatEntity;

public class NFFGirlsGaiaWerecatRenderer extends WerecatRenderer {

    private static final ResourceLocation MYGO_TEXTURE_LOCATION = new ResourceLocation(NFFGirlsGaia.MOD_ID, "textures/entity/gaia_werecat_rana.png");

    public NFFGirlsGaiaWerecatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(Werecat mob) {
        if (mob instanceof GaiaWerecatEntity tamed && tamed.itsMyGO()) {
            return MYGO_TEXTURE_LOCATION;
        }
        else return super.getTextureLocation(mob);
    }
}
