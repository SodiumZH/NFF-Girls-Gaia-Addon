package net.sodiumzh.nff.girls.gaia.client.model;

import gaia.client.model.HarpyModel;
import gaia.entity.Harpy;
import net.minecraft.client.model.geom.ModelPart;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

public class NFFGirlsGaiaHarpyModel extends HarpyModel {

    public NFFGirlsGaiaHarpyModel(ModelPart root) {
        super(root);
    }

    public void setupAnim(Harpy harpy, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(harpy, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.riding) {
            ModelPart leftLeg = NFUReflectionStatics.forceGet(this, HarpyModel.class, "leftleg").cast();
            ModelPart rightLeg = NFUReflectionStatics.forceGet(this, HarpyModel.class, "rightleg").cast();
            leftLeg.xRot -= 0.3f;
            rightLeg.xRot -= 0.3f;
        }
    }
}
