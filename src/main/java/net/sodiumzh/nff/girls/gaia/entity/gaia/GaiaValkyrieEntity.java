package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Valkyrie;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
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
import net.sodiumzh.nfu.exception.ReflectionFailedException;
import net.sodiumzh.nfu.math.RandomSelection;
import net.sodiumzh.nfu.util.NFUMathStatics;
import net.sodiumzh.nfu.util.NFUReflectionStatics;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GaiaValkyrieEntity extends Valkyrie implements INFFGirlsTamed, RangedAttackMob, IBlocksGaiaDynamicGoals {

    private static final RandomSelection<Function<Mob, NFUItemProjectileEntity>> PROJECTILE_SUPPLIER =
        new RandomSelection<>(NFFGirlsGaiaProjectileProviders.VALKYRIE_COMMON_PROJECTILE)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_THUNDER_PROJECTILE_FRIENDED, 0.1667d)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_ICE_PROJECTILE_FRIENDED, 0.1667d)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_EXPLOSIVE_PROJECTILE_FRIENDED, 0.1667d);
    private static final BiConsumer<NFUItemProjectileEntity, Mob> SHOOT_PROJECTILE_ACTION = (proj, m) -> {
        if (m.getTarget() != null) {
            float speed = new ResourceLocation("nffgirlgaia:valkyrie_common_projectile").equals(proj.getIdentifier()) ? 1.2f : 0.8f;
            proj.shootTo(m.getTarget().getBoundingBox().getCenter(), speed, 2f);
            proj.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (m.getRandom().nextFloat() * 0.5F + 1.0F));
        } else proj.discard();
    };
    // Reflects Valkyrie#aggressive
    private static final Field FIELD_SUPER_AGGRESSIVE = NFUReflectionStatics.findFieldIfDeclared(Valkyrie.class, "aggressive").orElseThrow();
    // Reflects Valkyrie#aggression
    private static final Field FIELD_SUPER_AGGRESSION = NFUReflectionStatics.findFieldIfDeclared(Valkyrie.class, "aggression").orElseThrow();

    public GaiaValkyrieEntity(EntityType<? extends GaiaValkyrieEntity> entityType, Level level) {
        super(entityType, level);
        // Fix targetPlayerGoal getting somehow null and producing a NullPointerException
        this.targetPlayerGoal = new Goal() {
            @Override
            public boolean canUse() {
                return false;
            }
        };
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
        int amount = (this.getXpLevel() >= 40 || this.getHealth() < this.getMaxHealth() / 2d) ? 5 : 3;
        int shootingDeltaTime = amount == 5 ? 8 : 15;
        List<NFUItemProjectileEntity> e = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            e.add(PROJECTILE_SUPPLIER.select(this.getRandom()).apply(this).setLifetime(6 * 20 + 15 + shootingDeltaTime * i));
        }
        Vec3 forward = Optional.ofNullable(this.getTarget()).map(t -> t.position().subtract(this.position())).orElse(this.getForward());
        forward = new Vec3(forward.x(), 0d, forward.z()).normalize();
        e.get(0).setPos(this.getEyePosition().add(0d, 3d, 0d));
        e.get(1).setPos(this.getEyePosition()
            .add(NFUMathStatics.rotateVectorY(forward, -90).normalize().scale(1.5d)).add(0d, 1.5d, 0d));
        e.get(2).setPos(this.getEyePosition()
            .add(NFUMathStatics.rotateVectorY(forward, 90).normalize().scale(1.5d)).add(0d, 1.5d, 0d));
        if (amount == 5) {
            e.get(3).setPos(this.getEyePosition()
                .add(NFUMathStatics.rotateVectorY(forward, 90).normalize().scale(2.5d)));
            e.get(4).setPos(this.getEyePosition()
                .add(NFUMathStatics.rotateVectorY(forward, -90).normalize().scale(2.5d)));
        }
        for (int i = 0; i < amount; ++i) {
            e.get(i).scheduleServerActions(shootingDeltaTime * i + 15, proj -> SHOOT_PROJECTILE_ACTION.accept(proj, this));
            this.level().addFreshEntity(e.get(i));
        }
        this.swing(InteractionHand.MAIN_HAND);
        this.level().playSound(this, this.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE,
            this.getSoundSource(), 1.0F, this.getRandom().nextFloat() * 0.2F + 1.2F);
    }

    /**
     * Prevent super add-shield action
     */
    @Override
    public void aiStep() {
        super.aiStep();
        // super.aiStep() may replace the offhand item with a shield. Correct it here.
        this.getAdditionalInventory().syncToMob(this);
        this.setAnnoyed(this.getTarget() != null);
    }

    // This may cause mob losing target
    @Override
    public void stopBeingAngry() {
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(MeleeAttackGoal.class);
    }

    // This causes endless noise due to original gaia giving shield nonstop
    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        if (pSlot.equals(EquipmentSlot.OFFHAND) && pStack.is(GaiaRegistry.IRON_SHIELD.get())
            && NFUReflectionStatics.isRunningInMethod(Valkyrie.class, "giveShield")) {
            return;
        }
        super.setItemSlot(pSlot, pStack);
    }

    public boolean isAnnoyed() {
        return this.getData().getAttackTarget() != null;
    }
}
