package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Shaman;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.ai.NFFGirlsAIUtils;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IPotionThrower;
import net.sodiumzh.nff.girls.gaia.entity.ai.PotionThrowerGoals;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GaiaShamanEntity extends Shaman implements IPotionThrower, IBlocksGaiaDynamicGoals {

    public static final double MELEE_ATTACK_THRESHOLD = 4.0d;

    public GaiaShamanEntity(EntityType<? extends GaiaShamanEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new PotionThrowerGoals.PotionEmergencySupportGoal(this, 1.75D, 60, 15.0F));
        goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.275d, true)
            .setStartCondition(NFFGirlsAIUtils.predicateTargetCloserThan(MELEE_ATTACK_THRESHOLD)));
        goalSelector.addGoal(4, new PotionThrowerGoals.PotionAttackGoal(this, 1.275D, 60, 8.0F)
            .setStartCondition(NFFGirlsAIUtils.predicateTargetFurtherThan(MELEE_ATTACK_THRESHOLD)));
        goalSelector.addGoal(4, new PotionThrowerGoals.PotionSupportGoal(this, 1.275D, 60, 8.0F));
        goalSelector.addGoal(5, new PotionThrowerGoals.PotionIdleSupportGoal(this, 1.275D, 60, 8.0F));
        goalSelector.addGoal(7, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        goalSelector.addGoal(8, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
        targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));
    }

    public void aiStep() {
        super.aiStep();
        // boolean isPotionAttack = this.goalSelector.getRunningGoals().anyMatch(wg -> wg.getGoal() instanceof PotionThrowerGoals.PotionAttackGoal);
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(RangedAttackGoal.class, MobAttackGoal.class, NearestAttackableTargetGoal.class);
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(6, this);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsHandItemsFourBaublesDefaultInventoryMenu(i, inventory, container, this);
    }

    // Disable this method to block gaia animation change. Use setAnimationStateOverride() instead.
    @Override
    public void setAnimationState(int state) {
    }

    public void setAnimationStateOverride(int state) {
        super.setAnimationState(state);
    }
}
