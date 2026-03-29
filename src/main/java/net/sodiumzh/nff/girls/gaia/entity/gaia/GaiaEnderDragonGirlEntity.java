package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.EnderDragonGirl;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFNearestAttackableTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.util.NFUReflectionStatics;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.List;

public class GaiaEnderDragonGirlEntity extends EnderDragonGirl implements INFFGirlsTamed {

    public GaiaEnderDragonGirlEntity(EntityType<? extends GaiaEnderDragonGirlEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        this.goalSelector.addGoal(7, new NFFWaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NFFNearestAttackableTargetGoal<>(this, Endermite.class, true, false).allowAllStates().asGoal());
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
        return new NFFTamedMobInventory(4);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsFourBaublesInventoryMenu(i, inventory, container, this);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }

    @Override
    @Nonnull
    public Component getTypeName() {
        EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
        return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
    }

   /* @Override
    protected boolean teleportRandomly() {
        return false;
    }*/

    public static final Method SUPER_HURT_WITH_CLEAN_WATER =
        NFUReflectionStatics.findMethodIfDeclared(EnderDragonGirl.class, "hurtWithCleanWater",
            DamageSource.class, ThrownPotion.class, float.class).orElseThrow();

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source instanceof IndirectEntityDamageSource) {
            Entity entity = source.getDirectEntity();
            boolean flag1;
            if (entity instanceof ThrownPotion) {
                flag1 = this.hurtWithCleanWater(source, (ThrownPotion)entity, input);
            } else {
                flag1 = false;
            }
            if (flag1) {
                for (int i = 0; i < 64; ++i) {
                    if (this.teleportRandomly()) {
                        return true;
                    }
                }
            }

            return flag1;
        } else {
            return super.hurt(source, damage);
        }
    }

    protected boolean hurtWithCleanWater(DamageSource damageSource, ThrownPotion thrownPotion, float damage) {
        if (this.isSensitiveToWater())
            return NFUReflectionStatics.invokeMethod(SUPER_HURT_WITH_CLEAN_WATER, this, damageSource, thrownPotion, damage).castTo(Boolean.class);
        else return false;
    }

}
