package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.NFUEffectZoneEntity;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

import java.util.List;

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
    public void tamableInit(NFFTamableComponent tamable) {
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
            List<NFUEffectZoneEntity> effectZones = mob.level().getEntitiesOfClass(NFUEffectZoneEntity.class,
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
                mob.level().addFreshEntity(outer);
            } else {
                // Deduplicate
                for (int i = 1; i < outerZones.size(); ++i)
                    outerZones.get(i).discard();
            }
            if (innerZones.isEmpty()) {
                // Absent => create one
                NFUEffectZoneEntity inner = NFFGirlsGaiaProjectileProviders.YUKI_ONNA_SNOW_ZONE_INNER.apply(mob);
                inner.alignCenterTo(mob.getBoundingBox().getCenter());
                mob.level().addFreshEntity(inner);
            } else {
                // Deduplicate
                for (int i = 1; i < innerZones.size(); ++i) {
                    innerZones.get(i).discard();
                }
            }
            NFFTamableComponent.getOptional(mob).ifPresent(c -> c.setAlwaysHostileTo(this.getOngoingPlayer(mob).orElse(null)));
        } else NFFTamableComponent.getOptional(mob).ifPresent(c -> c.setAlwaysHostileTo(null));
    }
}
