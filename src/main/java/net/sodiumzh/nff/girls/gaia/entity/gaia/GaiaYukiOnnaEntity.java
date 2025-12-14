package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.YukiOnna;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaSounds;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.NFUEffectZoneEntity;
import net.sodiumzh.nfu.mixin.event.entity.LivingStartBaseAiStepEvent;
import net.sodiumzh.nfu.util.NFUReflectionStatics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GaiaYukiOnnaEntity extends YukiOnna implements INFFGirlsTamed, RangedAttackMob, IBlocksGaiaDynamicGoals {

    public GaiaYukiOnnaEntity(EntityType<? extends GaiaYukiOnnaEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new NFFGirlsRangedAttackGoal(this, 1.0D, 3 * 20, 15.0F) {
            @Override
            public boolean checkCanUse() {
                return super.checkCanUse()
                    && this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_FANS);
            }
            @Override
            public boolean checkCanContinueToUse() {
                return super.checkCanContinueToUse() && this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_FANS);
            }
        });
        goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1.0d, true){
            @Override
            public boolean checkCanUse() {
                return super.checkCanUse() && !this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_FANS);
            }
            @Override
            public boolean checkCanContinueToUse() {
                return super.checkCanContinueToUse() && !this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_FANS);
            }
        });
        goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
        targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        NFUEffectZoneEntity proj = NFFGirlsGaiaProjectileProviders.YUKI_ONNA_SNOW_EFFECT_FRIENDED.apply(this);
        proj.setOwner(this);
        proj.alignCenterTo(this.getBoundingBox().getCenter());
        this.level.addFreshEntity(proj);
        Vec3 dir = target.getBoundingBox().getCenter().subtract(this.getEyePosition()).normalize();

        // Calculate direction
        double pitchAbs = Math.abs(Math.asin(dir.y) * 180d / Math.PI);

        dir = new Vec3(dir.x, pitchAbs < 20 ? 0 : dir.y, dir.z).normalize();
        proj.shoot(dir.x, dir.y, dir.z, 0.4f, 2f);
        this.swing(InteractionHand.MAIN_HAND);
        this.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
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

    @Override
    public boolean shouldSitOnWaiting() {
        return false;
    }

    //private static final Optional<Method>
    @SubscribeEvent
    public static void preventDebuffInHotBiomes(PotionEvent.PotionApplicableEvent event) {
        if (event.getEntity() instanceof GaiaYukiOnnaEntity e
            && INFFGirlsBauble.isEnvironmentImmunized(e)
            && event.getPotionEffect().getDuration() == 100
            && (event.getPotionEffect().getEffect().equals(MobEffects.MOVEMENT_SLOWDOWN) || event.getPotionEffect().getEffect().equals(MobEffects.WEAKNESS))
            && event.getPotionEffect().getAmplifier() == 0
            && NFUReflectionStatics.isRunningInMethod(YukiOnna.class, "m_8107_"/* aiStep() */))
        {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void preventSwitchingAi(LivingStartBaseAiStepEvent event) {
        if (event.getEntity() instanceof GaiaYukiOnnaEntity e) {
            e.goalSelector.removeGoal(NFUReflectionStatics.forceGet(e, YukiOnna.class, "avoidPlayerGoal").cast());
            e.goalSelector.removeGoal(NFUReflectionStatics.forceGet(e, YukiOnna.class, "mobAttackGoal").cast());
        }
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(AvoidEntityGoal.class, MobAttackGoal.class, NearestAttackableTargetGoal.class);
    }

    // This may unexpectedly impact the hand item, causing item loss
    @Override
    protected void setHandOrKnockback(ItemStack stack) {

    }

    // This may cause mob losing target
    @Override
    public void stopBeingAngry() {
    }
}
