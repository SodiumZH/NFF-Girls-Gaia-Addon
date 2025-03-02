package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class GaiaAnimalTamingProcess extends TamingProcessItemGivingProgress {

    protected static final String NBT_KEY_STRENGTH = "strength";

    @Override
    public void tamableInit(CNFFTamable cap)
    {
        cap.getGeneralNBT().putDouble(NBT_KEY_STRENGTH, 0d);
    }

    @Override
    public void serverTick(Mob mob)
    {
        CNFFTamable.getOptional(mob).ifPresent(tamable -> {
            super.serverTick(mob);
            if (tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) >= 1e-5d)
                NaUtilsEntityStatics.addEffectSafe(mob, new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST, 10, (int)(tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) / 0.2)));
            tamable.getGeneralNBT().putDouble(NBT_KEY_STRENGTH, Math.max(tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) - 5e-5d, 0d));	// decrease by 0.001 per second
        });

    }

    @Override
    public MobAngerRules getAngerRules() {
        return NFFGirlsAngerRules.DEFAULT.get();
    }

    @Override
    public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter)
    {
        CNFFTamable.getOptional(mob).ifPresent(tamable -> {
            tamable.getGeneralNBT().putDouble(NBT_KEY_STRENGTH, tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) + procAfter - procBefore);
        });
    }

    @Override
    public boolean additionalConditions(Player player, Mob mob) {
        return true;
    }

    @Override
    public int getItemGivingCooldownTicks() {
        return NFFGirlsTamingRules.COOLDOWN_MIDDLE;
    }
}
