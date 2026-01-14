package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.entity.ai.GoalGroup;

public class NFFGirlsGaiaAiGoalGroups {

    public static final GoalGroup<Mob> COMMON_MELEE = new GoalGroup<>()
        .addGoal(1, FloatGoal::new)
        .addGoal(4, mob -> new NFFMeleeAttackGoal(INFFTamed.get(mob).orElseThrow(), 1.0d, true))
        .addGoal(5, mob -> new NFFGirlsFollowOwnerGoal(INFFTamed.get(mob).orElseThrow(), 1.0d, 5.0f, 2.0f, false))
        .addGoal(6, mob -> new NFFWaterAvoidingRandomStrollGoal(INFFTamed.get(mob).orElseThrow(), 1.0d))
        .addGoal(7, mob -> new LookAtPlayerGoal(mob, Player.class, 8.0F))
        .addGoal(8, RandomLookAroundGoal::new)
        .addGoal(1, mob -> new NFFGirlsOwnerHurtByTargetGoal(INFFTamed.get(mob).orElseThrow()))
        .addGoal(2, mob -> new NFFHurtByTargetGoal(INFFTamed.get(mob).orElseThrow()))
        .addGoal(3, mob -> new NFFGirlsOwnerHurtTargetGoal(INFFGirlsTamed.get(mob).orElseThrow()))
        .addGoal(5, mob -> new NFFGirlsNearestHostileToSelfTargetGoal(INFFGirlsTamed.get(mob).orElseThrow()))
        .addGoal(6, mob -> new NFFGirlsNearestHostileToOwnerTargetGoal(INFFGirlsTamed.get(mob).orElseThrow()))
        .addGoal(7, mob -> new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(INFFGirlsTamed.get(mob).orElseThrow()))
        .addGoal(8, mob -> new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(INFFGirlsTamed.get(mob).orElseThrow()))
        .addGoal(9, mob -> new NFFGirlsAttackingStrategyTargetGoal(INFFGirlsTamed.get(mob).orElseThrow()));
}
