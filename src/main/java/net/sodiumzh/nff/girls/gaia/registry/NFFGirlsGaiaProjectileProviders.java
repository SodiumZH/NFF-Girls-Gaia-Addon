package net.sodiumzh.nff.girls.gaia.registry;

import com.github.mechalopa.hmag.registry.ModItems;
import gaia.entity.YukiOnna;
import gaia.registry.GaiaRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.GaiaYukiOnnaTamingProcess;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nfu.entity.NFUEffectZoneEntity;
import net.sodiumzh.nfu.entity.NFUItemProjectileEntity;
import net.sodiumzh.nfu.entity.ServerEntityMotion;
import net.sodiumzh.nfu.math.Field3D;
import net.sodiumzh.nfu.math.IInequalityPattern3D;
import net.sodiumzh.nfu.math.Inequality3D;

import java.util.function.Function;

public class NFFGirlsGaiaProjectileProviders {

    public static final Function<Mob, NFUEffectZoneEntity> YUKI_ONNA_SNOW_ZONE_INNER = owner ->
        NFUEffectZoneEntity.create(owner).setScale(8d, 8d)
            .setLifetime(-1)
            .setGravity(0)
            .particle(ParticleTypes.SNOWFLAKE, 300)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setOnServerLivingOverlap((z, e) -> {
                if (!(e instanceof YukiOnna) && e.getBoundingBox().getCenter().distanceToSqr(z.getBoundingBox().getCenter()) <= 64d) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 3));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5, 1));
                    if (e.tickCount % 10 == 0)
                        e.hurt(DamageSource.FREEZE, 2f);
                }
            })
            .setOnServerTick(z -> {
                if (!owner.isAlive())
                    z.discard();
                else if (!(NFFTamingMapping.getProcess(owner) instanceof GaiaYukiOnnaTamingProcess proc && proc.isInAnyProcess(owner))) {
                    z.discard();
                } else {
                    z.alignCenterTo(owner.getBoundingBox().getCenter(), true);
                }
            })
            .setIdentifier(new ResourceLocation("nffgirlsgaia:yuki_onna_snow_zone_inner"));

    public static final Function<Mob, NFUEffectZoneEntity> YUKI_ONNA_SNOW_ZONE_OUTER = owner ->
        NFUEffectZoneEntity.create(owner).setScale(16d, 16d)
            .setLifetime(-1)
            .setGravity(0)
            .particle(ParticleTypes.SNOWFLAKE, 200)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setOnServerLivingOverlap((z, e) -> {
                double distSqr = e.getBoundingBox().getCenter().distanceToSqr(z.getBoundingBox().getCenter());
                if (!(e instanceof YukiOnna) && distSqr >= 64d && distSqr <= 256d) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 1));
                    if (owner.tickCount % 30 == 3)
                        e.hurt(DamageSource.FREEZE, 2f);
                }
            })
            .setOnServerTick(z -> {
                if (!owner.isAlive())
                    z.discard();
                else if (!(NFFTamingMapping.getProcess(owner) instanceof GaiaYukiOnnaTamingProcess proc && proc.isInAnyProcess(owner))) {
                    z.discard();
                } else {
                    z.alignCenterTo(owner.getBoundingBox().getCenter());
                }
            })
            .setIdentifier(new ResourceLocation("nffgirlsgaia:yuki_onna_snow_zone_outer"));


    /**
     * Storm magic effect of friended Yuki-Onna.
     */
    public static final Function<LivingEntity, NFUEffectZoneEntity> YUKI_ONNA_SNOW_EFFECT_FRIENDED = owner ->
        NFUEffectZoneEntity.create(owner).setScale(6d, 6d)
            .particle(ParticleTypes.SNOWFLAKE, 100)
            .setLifetime(10 * 20)
            .setGravity(0f)
            .setOnServerLivingOverlap((z, l) -> {
                if (!l.equals(z.getOwner()) && !NFFTamedStatics.isLivingAlliedToBM(INFFTamed.get(z.getOwner()).orElseThrow(), l)) {
                    if (l.tickCount % 5 == 0) {
                        l.hurt(DamageSource.FREEZE, (float) (((LivingEntity) (z.getOwner())).getAttributeValue(Attributes.ATTACK_DAMAGE) / 4d));
                    }
                    l.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15 * 20, 3));
                }
            });


    /** Vortex force field dragging to the center horizontally and downward vertically. */
    private static final Field3D VORTEX_FORCE_FIELD = NFFGirlsGaiaGeometries.VORTEX.get().field()
        .scaleValue(new Vec3(0.05d, 0.05d, 0.05d))
        .putValueAddition(new Vec3(0.0, -0.04, 0.0))
        .setBaseDefDomain(Inequality3D.limitedInOne());

    /**
     * Vortex field summoned by Cecaelia and Mermaid during the friending process.
     */
    public static final Function<LivingEntity, NFUEffectZoneEntity> VORTEX = owner ->
        NFUEffectZoneEntity.create(owner).setScale(8d, 8d)
            .particle(ParticleTypes.BUBBLE, 200)
            .particleAreaShape(IInequalityPattern3D.CONE_SLIM.get().inequality()
                .scale(new Vec3(1.4d, 1.6d, 1.4d)))
            .particleAreaBoundingBox(new AABB(-1.4, -1.4, -1.4, 1.4, 1.4, 1.4))
            /*.particleVelocityFunction(NFFGirlsGaiaGeometries.VORTEX.get().field()
                .scaleValue(new Vec3(0.1d, 0.1d, 0.1d))
                .putValueAddition(new Vec3(0.0, -0.05, 0.0)))*/
            .setLifetime(10 * 20)
            .setGravity(0f)
            .setOnServerGenericEntityOverlap((z, e) -> {
                if (!e.equals(z.getOwner())
                    && (e instanceof LivingEntity || e instanceof ItemEntity)
                    && !e.getType().is(NFFGirlsTags.AQUATIC_MOB)
                    && !(e instanceof Player p && (p.isCreative() || p.isSpectator()))
                    && e.isInWaterOrBubble()) {
                    ServerEntityMotion.accel(VORTEX_FORCE_FIELD.relToAbs(z.getBoundingBox()).apply(e.getBoundingBox().getCenter()))
                        .apply(e);
                }
            });

    /**
     * Explosive projectile of friended Cecaelia.
     */
    public static final Function<LivingEntity, NFUItemProjectileEntity> BUBBLE_BOMB_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner).particle(ParticleTypes.BUBBLE, 3)
            .setLifetime(6 * 20)
            .setFireImmune(true)
            .setItem(GaiaRegistry.PROJECTILE_BUBBLE.get().getDefaultInstance())
            .particle(ParticleTypes.BUBBLE, 3)
            .setLiquidResistanceFactor(0.01f)
            .setAirResistanceFactor(0.01f)
            .setGravity(0.01f)
            .setOnHitLiving((e, h) -> {
                if (h.getEntity() instanceof LivingEntity l
                    && INFFTamed.get(owner).filter(t -> NFFTamedStatics.isLivingAlliedToBM(t, l)).isEmpty())
                {
                    l.hurt(DamageSource.indirectMagic(e, owner), 3f + (float)(owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) * 0.5f);
                    Vec3 center = e.getBoundingBox().getCenter();
                    e.level.explode(e,
                        DamageSource.indirectMagic(e, owner),
                        null,
                        center.x, center.y, center.z,
                        1f + 0.05f * (float) owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue(),
                        false,
                        Explosion.BlockInteraction.NONE);
                    e.discard();
                }
            })
            .setOnTick(e -> {
                if (!e.isInWaterOrBubble()) e.discard();
            });

    /**
     * Bubble magic of friendly Cecaelia.
     */
    public static final Function<Mob, NFUEffectZoneEntity> BUBBLE_SPHERE = owner ->
        NFUEffectZoneEntity.create(owner).setScale(3d, 3d)
            .setLifetime(6 * 20)
            .particle(ParticleTypes.BUBBLE, 100)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setOnServerLivingOverlap((z, l) -> {
                if (INFFTamed.get(owner).filter(t -> NFFTamedStatics.isLivingAlliedToBM(t, l)).isEmpty()
                    && z.getBoundingBox().getCenter().distanceToSqr(l.getEyePosition()) <= 4d)
                {
                    if (z.tickCount % 5 == 1) {
                        l.hurt(DamageSource.indirectMagic(z, owner),
                            3f + (float)(owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) * 0.25f);
                    }
                    if (l.equals(owner.getTarget()) && l.getBoundingBox().getCenter().distanceToSqr(l.getBoundingBox().getCenter()) <= 1d)
                        ServerEntityMotion.zero()
                            .addMovement(l.getBoundingBox().getCenter().subtract(z.getBoundingBox().getCenter()))
                            .addAccel(z.getDeltaMovement().reverse())
                            .apply(z);
                }
            })
            .setOnServerTick(e -> {
                if (!e.isInWaterOrBubble()) e.discard();
            });

    // Valkyrie projectiles on friending

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_THUNDER_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.LIGHTNING_PARTICLE.get().getDefaultInstance())
            .particle(ParticleTypes.SMOKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, proj.level);
                lightningBolt.setPos(proj.position());
                lightningBolt.setDamage((float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                proj.level.addFreshEntity(lightningBolt);
                proj.discard();
            })
            .setOnTick(proj -> {
                if (proj.level.getBlockState(proj.blockPosition()).getMaterial().isLiquid())
                    proj.discard();
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_EXPLOSIVE_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.BURNING_CORE.get().getDefaultInstance())
            .particle(ParticleTypes.FLAME, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                Vec3 center = proj.getBoundingBox().getCenter();
                proj.level.explode(proj,
                    DamageSource.indirectMagic(proj, owner),
                    null,
                    center.x, center.y, center.z,
                    (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.05f + 1.5f,
                    false,
                    proj.level.getRandom().nextDouble() < 0.25d ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE);
                proj.discard();
            });

    public static final Function<Mob, NFUEffectZoneEntity> VALKYRIE_ICE_ZONE = owner ->
        NFUEffectZoneEntity.create(owner).setScale(6d, 6d)
            .setLifetime(10 * 20)
            .setGravity(0)
            .particle(ParticleTypes.SNOWFLAKE, 200)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setBlockOverlapFilter((z, pos, bs) -> bs.is(Blocks.FIRE))
            .setOnServerLivingOverlap((z, e) -> {
                if (!e.equals(owner)
                    && e.getBoundingBox().getCenter().distanceToSqr(z.getBoundingBox().getCenter()) <= 36d) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 2));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 3));
                    if (e.tickCount % 10 == 0)
                        e.hurt(DamageSource.indirectMagic(z, e),
                            (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.2f);
                }
            })
            .setServerBlockOverlap((z, pos, bs) -> {
                z.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                z.level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (z.level.random.nextFloat() - z.level.random.nextFloat()) * 0.8F);
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_ICE_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(Items.SNOWBALL.getDefaultInstance())
            .particle(ParticleTypes.SNOWFLAKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                var iceZone = VALKYRIE_ICE_ZONE.apply(owner);
                iceZone.alignCenterTo(proj.position());
                proj.level.addFreshEntity(iceZone);
                proj.discard();
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_COMMON_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(Items.NETHER_STAR.getDefaultInstance())
            .particle(ParticleTypes.CRIT, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setHitIgnoresOwner(true)
            .setIdentifier(new ResourceLocation("nffgirlgaia:valkyrie_common_projectile"))
            .setOnHitLiving((proj, h) -> {
                h.getEntity().hurt(DamageSource.indirectMagic(proj, owner),
                    (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                proj.discard();
            });

    // Friended Valkyrie projectiles

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_THUNDER_PROJECTILE_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.LIGHTNING_PARTICLE.get().getDefaultInstance())
            .particle(ParticleTypes.SMOKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setHitIgnoresLiving((proj, l) -> NFFTamedStatics.isLivingAlliedToBM(INFFTamed.get(proj.getOwner()).orElseThrow(), l))
            .setOnHitBlockOrLiving((proj, h) -> {
                // Prevent lightning if an ally is within 3 blocks
                if (proj.level.getEntitiesOfClass(LivingEntity.class,
                    proj.getBoundingBox().inflate(3d),
                    l -> NFFTamedStatics.isLivingAlliedToBM(INFFTamed.get(proj.getOwner()).orElseThrow(), l)).isEmpty())
                {
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, proj.level);
                    lightningBolt.setPos(proj.position());
                    lightningBolt.setDamage((float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    proj.level.addFreshEntity(lightningBolt);
                    proj.discard();
                }
            })
            .setOnTick(proj -> {
                if (proj.level.getBlockState(proj.blockPosition()).getMaterial().isLiquid())
                    proj.discard();
            });


    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_ICE_PROJECTILE_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(Items.SNOWBALL.getDefaultInstance())
            .particle(ParticleTypes.SNOWFLAKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                var iceZone = VALKYRIE_ICE_ZONE.apply(owner);
                iceZone.alignCenterTo(proj.position());
                iceZone.setLivingOverlapFilter((z, l) -> NFFTamedStatics.isLivingAlliedToBM(INFFTamed.get(z.getOwner()).orElseThrow(), l));
                proj.level.addFreshEntity(iceZone);
                proj.discard();
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_EXPLOSIVE_PROJECTILE_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.BURNING_CORE.get().getDefaultInstance())
            .particle(ParticleTypes.FLAME, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                Vec3 center = proj.getBoundingBox().getCenter();
                proj.level.explode(proj,
                    DamageSource.indirectMagic(proj, owner),
                    null,
                    center.x, center.y, center.z,
                    (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.05f + 1.5f,
                    false,
                    Explosion.BlockInteraction.NONE);
                proj.discard();
            });

}
