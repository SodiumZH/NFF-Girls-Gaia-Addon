package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Valkyrie;
import gaia.registry.GaiaSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.entity.NFUItemProjectileEntity;
import net.sodiumzh.nfu.math.RandomSelection;
import net.sodiumzh.nfu.util.NFUMathStatics;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class GaiaValkyrieEntity extends Valkyrie implements INFFGirlsTamed, RangedAttackMob {

    private static final RandomSelection<Function<Mob, NFUItemProjectileEntity>> PROJECTILE_SUPPLIER =
        new RandomSelection<>(NFFGirlsGaiaProjectileProviders.VALKYRIE_COMMON_PROJECTILE)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_THUNDER_PROJECTILE_FRIENDED, 0.1667d)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_ICE_PROJECTILE_FRIENDED, 0.1667d)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_EXPLOSIVE_PROJECTILE_FRIENDED, 0.1667d);
    private static final BiConsumer<NFUItemProjectileEntity, Mob> SHOOT_PROJECTILE_ACTION = (proj, m) -> {
        if (m.getTarget() != null) {
            proj.shootTo(m.getTarget().getBoundingBox().getCenter(), 0.8f, 2f);
            proj.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (m.getRandom().nextFloat() * 0.5F + 1.0F));
        } else proj.discard();
    };

    public GaiaValkyrieEntity(EntityType<? extends GaiaValkyrieEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new NFFGirlsRangedAttackGoal(this, 1.0D, 5 * 20, 15.0F) {
            @Override
            public boolean checkCanUse() {
                return super.checkCanUse()
                    && this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS);
            }
            @Override
            public boolean checkCanContinueToUse() {
                return super.checkCanContinueToUse()
                    && this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS);
            }
        });
        goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1.0d, true) {
            @Override
            public boolean checkCanUse() {
                return super.checkCanUse()
                    && !this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS);
            }
            @Override
            public boolean checkCanContinueToUse() {
                return super.checkCanContinueToUse()
                    && !this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS);
            }
        });
        goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(6);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsHandItemsFourBaublesDefaultInventoryMenu(i, inventory, container, this);
    }

    @Override
    public MobApplicableItemTable getHealingItems() {
        return NFFGirlsHealingItems.GENERAL_HUMANOID_0.get();
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        if (this.getTarget() == null) return;
        NFUItemProjectileEntity[] e = new NFUItemProjectileEntity[]{null, null, null};
        for (int i = 0; i < 3; ++i) {
            e[i] = PROJECTILE_SUPPLIER.select(this.getRandom()).apply(this).setLifetime(6 * 20 + 15 * i);
            e[i].setHitIgnoresLiving((proj, l) -> NFFTamedStatics.isLivingAlliedToBM(INFFTamed.get(proj.getOwner()).orElseThrow(), l));
            e[i].scheduleServerActions(15 * i + 15, proj -> SHOOT_PROJECTILE_ACTION.accept(proj, this));
        }
        e[0].setPos(this.getEyePosition()
            .add(NFUMathStatics.rotateVectorY(this.getForward(), 90).normalize().scale(1.5d)).add(0d, 1.5d, 0d));
        e[1].setPos(this.getEyePosition()
            .add(NFUMathStatics.rotateVectorY(this.getForward(), -90).normalize().scale(1.5d)).add(0d, 1.5d, 0d));
        e[2].setPos(this.getEyePosition().add(0d, 3d, 0d));
        for (int i = 0; i < 3; ++i) {
            this.level().addFreshEntity(e[i]);
        }
        this.swing(InteractionHand.MAIN_HAND);
        this.level().playSound(this, this.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE,
            this.getSoundSource(), 1.0F, this.getRandom().nextFloat() * 0.2F + 1.2F);
    }
}
