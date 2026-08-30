package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.reflection.CachedFieldSearchers;

import java.util.List;
import java.util.Objects;

public class GaiaSirenFriendingProcess extends TamingProcessItemGivingProgress {
    @Override
    public boolean additionalConditions(Player player, Mob mob) {
        return hasRunningJukebox(mob);
    }

    @Override
    public int getItemGivingCooldownTicks() {
        return NFFGirlsTamingRules.COOLDOWN_MIDDLE;
    }

    @Override
    public void tamableInit(NFFTamableComponent cnffTamable) {

    }

    @Override
    public MobAngerRules getAngerRules() {
        return NFFGirlsAngerRules.ATTACKER_AND_MINOR_HIT.get();
    }

    @Override
    public void serverTick(Mob mob) {
        super.serverTick(mob);
        Player ongoing = this.getOngoingPlayerInLevel(mob).orElse(null);
        if (ongoing != null) {
            if (ongoing.distanceToSqr(mob) > 32d * 32d)
                this.interrupt(ongoing, mob, true);
        }
        if (!hasRunningJukebox(mob) && this.isInAnyProcess(mob)) {
            NFFGirlsTamingRules.tickContinuousProgressLoss(this, mob);
        }
        NFFTamableComponent.getOrDefault(mob).setAlwaysHostileTo(ongoing);
    }

    protected static boolean isJukeboxPlaying(JukeboxBlockEntity jukebox) {
        return jukebox.getRecord() != null && !jukebox.getRecord().isEmpty();
    }

    protected boolean hasRunningJukebox(Mob mob) {
        Level level = mob.getLevel();
        return BlockPos.betweenClosedStream(mob.getBoundingBox().inflate(8d, 6d, 8d))
                .filter(pos -> level.getBlockState(pos).is(Blocks.JUKEBOX))
                .map(level::getBlockEntity)
                .filter(be -> be instanceof JukeboxBlockEntity j && (Boolean) CachedFieldSearchers.getFieldValue(j, JukeboxBlockEntity.class, "f_238637_").orElseThrow())
                .count() == 1;
    }
}
