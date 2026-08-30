package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Witch;
import gaia.registry.GaiaRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IPotionThrower;
import net.sodiumzh.nff.girls.gaia.entity.ai.PotionThrowerGoals;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingLandGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingRandomMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import net.sodiumzh.nfu.mixin.event.entity.MobRegisterGoalsEvent;
import net.sodiumzh.nfu.util.NFUReflectionStatics;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GaiaWitchEntity extends Witch implements INFFGirlsTamed, IPotionThrower, IBlocksGaiaDynamicGoals {

    public GaiaWitchEntity(EntityType<? extends GaiaWitchEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new PotionThrowerGoals.PotionEmergencySupportGoal(this, 1.8D, 60, 8.0F));
        goalSelector.addGoal(4, new PotionThrowerGoals.PotionAttackGoal(this, 1.2D, 60, 8.0F).setInterruptChance(0.2d));
        goalSelector.addGoal(4, new PotionThrowerGoals.PotionSupportGoal(this, 1.2D, 60, 8.0F).setInterruptChance(0.2d));
        goalSelector.addGoal(5, new PotionThrowerGoals.PotionIdleSupportGoal(this, 1.2D, 60, 8.0F));
        this.goalSelector.addGoal(6, new NFFFlyingLandGoal(this) {
            public boolean checkCanUse() {return super.checkCanUse() && isRidingBroom();}
        });
        this.goalSelector.addGoal(7, new NFFGirlsFlyingFollowOwnerGoal(this, 1.5d){
            public boolean checkCanUse() {return super.checkCanUse() && isRidingBroom();}
        }.setHoveringHeightOffset(-0.5d));
        this.goalSelector.addGoal(8, new NFFFlyingRandomMoveGoal(this){
            public boolean checkCanUse() {return super.checkCanUse() && isRidingBroom();}
        }.heightLimit(7));
        goalSelector.addGoal(7, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false){
            public boolean checkCanUse() {return super.checkCanUse() && !isRidingBroom();}
        });
        goalSelector.addGoal(8, new NFFWaterAvoidingRandomStrollGoal(this, 1.2d){
            public boolean checkCanUse() {return super.checkCanUse() && !isRidingBroom();}
        });
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

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.throwPotion(target, distanceFactor, true);
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(NearestAttackableTargetGoal.class);
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

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack stack) {
        super.setItemSlot(equipmentSlot, stack);
        // Reset to enable broom tag
        if (equipmentSlot == EquipmentSlot.OFFHAND) {
            boolean isRidingBroom = stack.is(GaiaRegistry.BROOM.get()) || stack.is(NFFGirlsGaiaTags.WEAPON_BROOMS);
            this.setRidingBroom(isRidingBroom);
            if (!isRidingBroom && this.isNoGravity()) {
                this.setNoGravity(false);
            }
            this.moveControl = isRidingBroom ? this.flyingControl : this.normalControl;
            EntityComponentAPI.getDataComponent(this)
                .getVariable(this.isRidingBroom() ? "flyingNavigation" : "groundNavigation", PathNavigation.class)
                .ifPresent(nav -> this.navigation = nav);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep(); // Make GaiaWitchEntity.aiStep present in stacktrace, so zombie spawning can be blocked by stack walking
    }

    public void setDeltaMovement(Vec3 v) {
        super.setDeltaMovement(v);
    }

    @Override
    protected void beaconMonster(int range, Consumer<LivingEntity> action) {
        super.beaconMonster(range, living -> {
            if (this.isAllyTo(living))
                action.accept(living);
        });
    }

    /* Witch AI Fix */
    /* Remove after Gaia fixes this issue */

    private static final Field MOB_NAVIGATION = NFUReflectionStatics.findFieldIfDeclared(
        Mob.class, "f_21344_").orElseThrow();

    @SubscribeEvent
    public static void fixOriginalGaiaWitchAi_InitNav(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Witch witch && !witch.level.isClientSide) {
            EntityComponentAPI.getDataComponent(witch).putTransientVariable("flyingNavigation",
                new FlyingPathNavigation(witch, witch.level));
            EntityComponentAPI.getDataComponent(witch).putTransientVariable("groundNavigation",
                new GroundPathNavigation(witch, witch.level));
        }
    }

    @SubscribeEvent
    public static void fixOriginalGaiaWitchAi_InitGoals(MobRegisterGoalsEvent event) {
        if (event.getEntity().getType().equals(GaiaRegistry.WITCH.getEntityType())
            && event.getEntity() instanceof Witch witch && !witch.level.isClientSide)
        {
            event.getGoalSelector().getAvailableGoals().stream().filter(wg -> wg.getPriority() == 2)
                .toList().forEach(wg -> event.getGoalSelector().removeGoal(wg.getGoal()));
            event.getGoalSelector().addGoal(2, new RandomStrollGoal(witch, 1.0D) {
                @Override
                public boolean canUse() {
                    return super.canUse() && !witch.isRidingBroom();
                }
            });
            event.getGoalSelector().addGoal(2, new WaterAvoidingRandomFlyingGoal(witch, 1.0d) {
                @Override
                public boolean canUse() {
                    return super.canUse() && witch.isRidingBroom();
                }
            });
        }
    }

    @SubscribeEvent
    public static void fixOriginalGaiaWitchAi_UpdateFlying(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof Witch witch && !witch.level.isClientSide) {
            EntityComponentAPI.getDataComponent(witch).getVariable(
                witch.isRidingBroom() ? "flyingNavigation" : "groundNavigation", PathNavigation.class
            ).ifPresent(nav -> NFUReflectionStatics.setValue(MOB_NAVIGATION, witch, nav));
        }
    }



}
