package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

public class GaiaWitchProcess extends TamingProcessItemGivingProgress {
    @Override
    public boolean additionalConditions(Player player, Mob mob) {
        return mob.getActiveEffects().stream().filter(i -> i.getEffect().getCategory().equals(MobEffectCategory.BENEFICIAL))
            .count() >= 5;
    }

    @Override
    public int getItemGivingCooldownTicks() {
        return NFFGirlsTamingRules.COOLDOWN_MIDDLE;
    }

    @Override
    public void tamableInit(CNFFTamable cnffTamable) {

    }

    @Override
    public MobAngerRules getAngerRules() {
        return NFFGirlsAngerRules.ATTACKER_AND_MINOR_HIT.get();
    }
}
