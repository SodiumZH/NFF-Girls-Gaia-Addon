package net.sodiumzh.nff.girls.gaia.client.renderer;

import gaia.client.renderer.SuccubusRenderer;
import gaia.entity.Succubus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaSuccubusEntity;

public class NFFGirlsGaiaSuccubusRenderer extends SuccubusRenderer {

    private static final ResourceLocation MYGO_TEXTURE_LOCATION = new ResourceLocation(NFFGirlsGaia.MOD_ID, "textures/entity/gaia_succubus_tomori.png");
    public NFFGirlsGaiaSuccubusRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(Succubus mob) {
        if (mob instanceof GaiaSuccubusEntity tamed && tamed.itsMyGO()) {
            return MYGO_TEXTURE_LOCATION;
        }
        else return super.getTextureLocation(mob);
    }
}
