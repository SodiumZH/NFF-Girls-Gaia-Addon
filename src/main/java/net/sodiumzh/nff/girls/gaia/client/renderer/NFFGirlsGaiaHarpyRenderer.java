package net.sodiumzh.nff.girls.gaia.client.renderer;

import gaia.client.ClientHandler;
import gaia.client.renderer.GaiaBabyMobRenderer;
import gaia.entity.Harpy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.gaia.client.model.NFFGirlsGaiaHarpyModel;

import javax.annotation.Nonnull;

public class NFFGirlsGaiaHarpyRenderer extends GaiaBabyMobRenderer<Harpy, NFFGirlsGaiaHarpyModel> {
    public static final ResourceLocation[] HARPY_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/harpy/harpy01.png"), new ResourceLocation("grimoireofgaia", "textures/entity/harpy/harpy02.png"), new ResourceLocation("grimoireofgaia", "textures/entity/harpy/harpy03.png")};

    public NFFGirlsGaiaHarpyRenderer(EntityRendererProvider.Context context) {
        super(context, new NFFGirlsGaiaHarpyModel(context.bakeLayer(ClientHandler.HARPY)), 0.4F);
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    }

    @Nonnull
    public ResourceLocation getTextureLocation(Harpy harpy) {
        return HARPY_LOCATIONS[harpy.getVariant()];
    }
}
