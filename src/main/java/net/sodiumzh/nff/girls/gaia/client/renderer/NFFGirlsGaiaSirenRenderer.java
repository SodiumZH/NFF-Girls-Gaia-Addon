package net.sodiumzh.nff.girls.gaia.client.renderer;

import gaia.client.renderer.SirenRenderer;
import gaia.entity.Siren;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaSirenEntity;

public class NFFGirlsGaiaSirenRenderer extends SirenRenderer {

    protected static final ResourceLocation TEXTURE_LOCATION_LADINA =
        new ResourceLocation(NFFGirlsGaia.MOD_ID, "textures/entity/gaia_siren_ladina.png");

    public NFFGirlsGaiaSirenRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Siren mob) {
        if (mob instanceof GaiaSirenEntity tamed && tamed.isRareVariant()) {
            return switch (tamed.getRareVariant().map(IHasRareVariant.RareVariant::name).orElse("")) {
                case "ladina" -> TEXTURE_LOCATION_LADINA;
                default -> super.getTextureLocation(mob);
            };
        }
        return super.getTextureLocation(mob);
    }
}
