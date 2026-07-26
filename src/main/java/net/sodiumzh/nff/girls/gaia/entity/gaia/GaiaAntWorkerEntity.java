package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.AntWorker;
import gaia.registry.GaiaSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaAiGoalGroups;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEffects;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class GaiaAntWorkerEntity extends AntWorker implements INFFGirlsTamed {

    public GaiaAntWorkerEntity(EntityType<? extends GaiaAntWorkerEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        NFFGirlsGaiaAiGoalGroups.COMMON_MELEE.addTo(this, 0);
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(4);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsHandItemsTwoBaublesInventoryMenu(i, inventory, container, this);
    }

    /*public void aiStep() {
        super.aiStep();
        Player owner = this.getOwnerInDimension();
        if (owner != null && this.hasLineOfSight(owner) && owner.distanceToSqr(this) <= 64d
            && !EntityComponentAPI.getDefaultTimer(this).hasGeneralTimer("shoot_pheromone_bullet"))
        {
            var projectile = NFFGirlsGaiaProjectileProviders.ANT_PHEROMONE_PROJECTILE.apply(this);
            EntityComponentAPI.getDataComponent(projectile).putTransientVariable("target", owner);
            projectile.setPos(this.getEyePosition());
            projectile.shootTo(owner.getBoundingBox().getCenter(), 0.2f, 0f);
            this.level.addFreshEntity(projectile);
            this.swing(InteractionHand.MAIN_HAND);
            this.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            EntityComponentAPI.getDefaultTimer(this).addTimer("shoot_pheromone_bullet", 2 * 60 * 20, true);
        }
    }*/

    @Override
    @Nonnull
    public Component getTypeName() {
        EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
        return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
    }
}
