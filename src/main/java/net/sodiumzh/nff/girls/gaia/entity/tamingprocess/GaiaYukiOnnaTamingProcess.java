package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import gaia.entity.YukiOnna;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.ConditionalAttributeModifier;
import net.sodiumzh.nfu.entity.NFUEffectZoneEntity;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.util.NFUParticleStatics;

import java.util.List;
import java.util.Objects;

public class GaiaYukiOnnaTamingProcess extends TamingProcessItemGivingProgress {

    @Override
    public boolean additionalConditions(Player player, Mob mob) {
        return true;
    }

    @Override
    public int getItemGivingCooldownTicks() {
        return NFFGirlsTamingRules.COOLDOWN_LONG;
    }

    @Override
    public void tamableInit(CNFFTamable tamable) {
    }

    @Override
    public MobAngerRules getAngerRules() {
        return MobAngerRules.ATTACKER_DAMAGED.get();
    }

    @Override
    public void serverTick(Mob mob) {
        super.serverTick(mob);
        if (this.isInAnyProcess(mob)) {
            // Add snowstorm field if absent
            List<NFUEffectZoneEntity> effectZones = mob.level.getEntitiesOfClass(NFUEffectZoneEntity.class,
                mob.getBoundingBox().inflate(12d, 12d, 12d), z -> mob.equals(z.getOwner()));
            List<NFUEffectZoneEntity> outerZones = effectZones.stream()
                .filter(e -> e.getIdentifier().equals(new ResourceLocation("nffgirlsgaia:yuki_onna_snow_zone_outer")))
                .toList();
            List<NFUEffectZoneEntity> innerZones = effectZones.stream()
                .filter(e -> e.getIdentifier().equals(new ResourceLocation("nffgirlsgaia:yuki_onna_snow_zone_inner")))
                .toList();
            if (outerZones.isEmpty()) {
                // Absent => create one
                NFUEffectZoneEntity outer = NFFGirlsGaiaProjectileProviders.YUKI_ONNA_SNOW_ZONE_OUTER.apply(mob);
                outer.alignCenterTo(mob.getBoundingBox().getCenter());
                mob.level.addFreshEntity(outer);
            } else {
                // Deduplicate
                for (int i = 1; i < outerZones.size(); ++i)
                    outerZones.get(i).discard();
            }
            if (innerZones.isEmpty()) {
                // Absent => create one
                NFUEffectZoneEntity inner = NFFGirlsGaiaProjectileProviders.YUKI_ONNA_SNOW_ZONE_INNER.apply(mob);
                inner.alignCenterTo(mob.getBoundingBox().getCenter());
                mob.level.addFreshEntity(inner);
            } else {
                // Deduplicate
                for (int i = 1; i < innerZones.size(); ++i) {
                    innerZones.get(i).discard();
                }
            }
            CNFFTamable.get(mob).setAlwaysHostileTo(this.getOngoingPlayer(mob).orElse(null));
        } else CNFFTamable.get(mob).setAlwaysHostileTo(null);
    }

}
