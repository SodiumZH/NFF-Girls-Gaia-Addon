package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import com.github.mechalopa.hmag.registry.ModItems;
import gaia.entity.Valkyrie;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nfu.entity.AttachedItemDisplayerEntity;
import net.sodiumzh.nfu.entity.ConditionalAttributeModifier;
import net.sodiumzh.nfu.entity.NFUItemProjectileEntity;
import net.sodiumzh.nfu.entity.ServerEntityMotion;
import net.sodiumzh.nfu.entity.ai.NFURangedAttackGoal;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.exception.DuplicateRegistryEntryException;
import net.sodiumzh.nfu.math.RandomSelection;
import net.sodiumzh.nfu.math.WeightedRandomSelector;
import net.sodiumzh.nfu.mixin.event.entity.ProjectileHitEvent;
import net.sodiumzh.nfu.registry.NFUEntityTypes;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import net.sodiumzh.nfu.util.NFUMathStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GaiaValkyrieTamingProcess extends NFFTamingProcess {

    public static final ConditionalAttributeModifier STEP_HEIGHT_ADDITION =
        new ConditionalAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), 2.5d,
            AttributeModifier.Operation.ADDITION, e ->
            e instanceof Valkyrie v
                && NFFTamingMapping.getProcess(v) instanceof GaiaValkyrieTamingProcess proc
                && proc.isInAnyProcess(v));

    private static final RandomSelection<Function<Mob, NFUItemProjectileEntity>> PROJECTILE_SUPPLIER =
        new RandomSelection<>(NFFGirlsGaiaProjectileProviders.VALKYRIE_COMMON_PROJECTILE)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_THUNDER_PROJECTILE, 0.1667d)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_ICE_PROJECTILE, 0.1667d)
            .add(NFFGirlsGaiaProjectileProviders.VALKYRIE_EXPLOSIVE_PROJECTILE, 0.1667d);


    private static final BiConsumer<NFUItemProjectileEntity, Mob> SHOOT_PROJECTILE_ACTION = (proj, m) -> {
        if (m.getTarget() != null) {
            float speed = proj.getIdentifier().equals(new ResourceLocation("nffgirlgaia:valkyrie_common_projectile")) ? 1.2f : 0.8f;
            proj.shootTo(m.getTarget().getBoundingBox().getCenter(), speed, 2f);
            proj.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (m.getRandom().nextFloat() * 0.5F + 1.0F));
        } else proj.discard();
    };

    public Map<Mob, AttachedItemDisplayerEntity> displayers = new HashMap<>();


    private static Optional<GaiaValkyrieTamingProcess> isInThisProcess(Mob mob) {
        return NFFTamingMapping.getProcess(mob) instanceof GaiaValkyrieTamingProcess process
            && process.isInAnyProcess(mob) ? Optional.of(process) : Optional.empty();
    }


    @Override
    public void tamableInit(NFFTamableComponent tamable) {
        STEP_HEIGHT_ADDITION.apply(tamable.getEntity());
        if (tamable.getEntity() instanceof Valkyrie v)
          v.goalSelector.addGoal(0,
              new ShootingGoal(v, 1.0, 5 * 20, 15f)
                  .minAttackDistance(4d)
                  .setShootingAction((m, l, f) -> rangedAttack(m)));
    }

    @Override
    public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand interactionHand) {
        return TamingInteractionResult.unhandled(mob);
    }

    @Override
    public void serverTick(Mob mob) {
        NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);

        cacheMainHandItem(mob);
        if (this.isInAnyProcess(mob)) {
            // Handle force persistent and hostility
            this.getOngoingPlayer(mob).filter(p -> p.distanceToSqr(mob) >= 32 * 32)
                .ifPresentOrElse(tamable::setAlwaysHostileTo, () -> tamable.setAlwaysHostileTo(null));
            tamable.setForcePersistent(true);
            if (mob.getHealth() < this.getMinHP(mob))
                mob.setHealth(this.getMinHP(mob));
            // Handle action hint
            if (this.requiresActionNow(mob)
                && (!displayers.containsKey(mob) || displayers.get(mob).level() != mob.level() || !displayers.get(mob).getItem().is(this.getRequiredAction(mob).hint.getItem()))) {
                if (displayers.containsKey(mob)) {
                     displayers.get(mob).discard();
                    displayers.remove(mob);
                }
                AttachedItemDisplayerEntity displayer = new AttachedItemDisplayerEntity(
                    NFUEntityTypes.ATTACHED_ITEM_DISPLAYER.get(), mob.level())
                    .setItem(this.getRequiredAction(mob).hint)
                    .sineHovering(new Vec3(0, 1d, 0), 0.5d, 3 * 20)
                    .setAttachedEntity(mob);
                this.displayers.put(mob, displayer);
                mob.level().addFreshEntity(displayer);
            } else if (!this.requiresActionNow(mob)) {
                if (displayers.containsKey(mob)) {
                    displayers.get(mob).discard();
                    displayers.remove(mob);
                }
            }
        }
        else {
            tamable.setForcePersistent(false);
            tamable.setAlwaysHostileTo(null);
        }
    }

    @Override
    public void interrupt(Player player, Mob mob, boolean b) {
        NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);
        tamable.getGeneralNBT().remove("ongoingPlayer");
        tamable.getGeneralNBT().remove("progress");
        tamable.getGeneralNBT().remove("requiredAction");
        tamable.getTimerComponent().removeGeneralTimer("timeLimit", false);
        removeCachedMainHandItem(mob);
    }

    @Override
    public boolean interruptAll(Mob mob, boolean b) {
        boolean res = this.isInAnyProcess(mob);
        this.interrupt(null, mob, b);
        return res;
    }

    @Override
    public boolean isInProcess(Player player, Mob mob) {
        return NFFTamableComponent.getOrDefault(mob).getGeneralNBT().hasUUID("ongoingPlayer")
            && NFFTamableComponent.getOrDefault(mob).getGeneralNBT().getUUID("ongoingPlayer").equals(player.getUUID());
    }

    @Override
    public boolean isInAnyProcess(Mob mob) {
        return NFFTamableComponent.getOrDefault(mob).getGeneralNBT().hasUUID("ongoingPlayer") &&
            ! NFFTamableComponent.getOrDefault(mob).getGeneralNBT().getUUID("ongoingPlayer").equals(new UUID(0L, 0L));
    }

    @Override
    public MobAngerRules getAngerRules() {
        return MobAngerRules.NO_ANGER.get();
    }

    // Utilities about the process

    private int getProgress(Mob mob) {
        if (!this.isInAnyProcess(mob)) return -1;
        else return NFFTamableComponent.getOrDefault(mob).getGeneralNBT().getInt("progress");
    }

    private RequiredAction getRequiredAction(Mob mob) {
        return RequiredAction.byId( NFFTamableComponent.getOrDefault(mob).getGeneralNBT().getInt("requiredAction"));
    }

    private Optional<Player> getOngoingPlayer(Mob mob) {
        return isInAnyProcess(mob) ?
            Optional.ofNullable(mob.level().getPlayerByUUID( NFFTamableComponent.getOrDefault(mob).getGeneralNBT().getUUID("ongoingPlayer")))
            : Optional.empty();
    }

    private float getMinHP(Mob mob) {
        if (!isInAnyProcess(mob)) return 0f;
        float res = mob.getMaxHealth() * (1f - (this.getProgress(mob) + 1f) / 8f);
        if (Math.abs(res) < 0.001f) return 0.01f;    // Preventing float calculation error
        else return res;
    }

    private boolean requiresActionNow(Mob mob) {
        return this.isInAnyProcess(mob) && mob.getHealth() == getMinHP(mob);
    }

    // Generate a new required action, and reset the timer
    private void refreshRequirement(Mob mob) {
        NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);
        tamable.getGeneralNBT().putInt("requiredAction", RequiredAction.pick().id);
        tamable.getTimerComponent().addTimer("timeLimit", 90 * 20, true);
        if (mob.getHealth() < this.getMinHP(mob)) mob.setHealth(this.getMinHP(mob));
    }

    // Invoked when the progress is boosted by 1
    private void progressUp(Mob mob) {
        NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);
        int oldProgress = tamable.getGeneralNBT().getInt("progress");
        // Case completed
        if (this.getProgress(mob) >= 7) {
            mob.setHealth(1f);
            resumeCachedMainHandItem(mob);
            NFUParticleStatics.sendHeartParticlesToEntityDefault(mob);
            Mob tamed = doTaming(this.getOngoingPlayer(mob).orElseThrow(), mob);
            tamed.setHealth(tamed.getMaxHealth() / 2f);
        } else {
            tamable.getGeneralNBT().putInt("progress", oldProgress + 1);
            NFUParticleStatics.sendGlintParticlesToEntityDefault(mob);
            this.refreshRequirement(mob);
            NFUParticleStatics.sendParticlesToEntity(mob, ParticleTypes.EXPLOSION, -1d, 1d, 4, 0d);
            mob.playSound(SoundEvents.GENERIC_EXPLODE, 4f, 1f);
            Vec3 v = mob.getBoundingBox().getCenter().subtract(this.getOngoingPlayer(mob).orElseThrow().getBoundingBox().getCenter());
            Vec3 knockbackDir = new Vec3(v.x, 0, v.z).normalize();
            this.getOngoingPlayer(mob).ifPresent(p -> NFUEntityStatics.knockbackOnServer(p, 2d, knockbackDir.x(), knockbackDir.z()));
            ServerEntityMotion.accel(knockbackDir.add(0d, 0.2d, 0d).normalize().scale(4d)).apply(mob);
        }
    }

    private void progressDown(Mob mob) {
        NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);
        int oldProgress = tamable.getGeneralNBT().getInt("progress");
        if (oldProgress <= 0) {
            this.interruptAll(mob, true);
        }
        else {
            tamable.getGeneralNBT().putInt("progress", oldProgress - 1);
            this.refreshRequirement(mob);
        }
        NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
        this.getOngoingPlayer(mob).ifPresent(p -> this.thunderPunishment(p, mob));
    }

    private void thunderPunishment(Player player, Mob mob) {
        if (player == null || mob == null || !player.level().equals(mob.level())) return;
        if (player.distanceToSqr(mob) > 32d * 32d) return;
        mob.swing(InteractionHand.MAIN_HAND);
        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, mob.level());
        lightning.setDamage(30f);
        lightning.setPos(Vec3.atBottomCenterOf(player.blockPosition()));
        player.level().addFreshEntity(lightning);
        mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5 * 20));
    }

    private void rangedAttack(Mob mob) {
        if (mob.getTarget() == null) return;
        int amount = this.getProgress(mob) >= 4 ? 5 : 3;
        int shootingDeltaTime = amount == 5 ? 8 : 15;
        List<NFUItemProjectileEntity> e = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            e.add(PROJECTILE_SUPPLIER.select(mob.getRandom()).apply(mob).setLifetime(6 * 20 + 15 + shootingDeltaTime * i));
        }
        Vec3 forward = Optional.ofNullable(mob.getTarget()).map(t -> t.position().subtract(mob.position())).orElse(mob.getForward());
        forward = new Vec3(forward.x(), 0d, forward.z()).normalize();
        e.get(0).setPos(mob.getEyePosition().add(0d, 3d, 0d));
        e.get(1).setPos(mob.getEyePosition()
            .add(NFUMathStatics.rotateVectorY(forward, -90).normalize().scale(1.5d)).add(0d, 1.5d, 0d));
        e.get(2).setPos(mob.getEyePosition()
            .add(NFUMathStatics.rotateVectorY(forward, 90).normalize().scale(1.5d)).add(0d, 1.5d, 0d));
        if (amount == 5) {
            e.get(3).setPos(mob.getEyePosition()
                .add(NFUMathStatics.rotateVectorY(forward, 90).normalize().scale(2.5d)));
            e.get(4).setPos(mob.getEyePosition()
                .add(NFUMathStatics.rotateVectorY(forward, -90).normalize().scale(2.5d)));
        }
        for (int i = 0; i < amount; ++i) {
            e.get(i).scheduleServerActions(shootingDeltaTime * i + 15, proj -> SHOOT_PROJECTILE_ACTION.accept(proj, mob));
            mob.level().addFreshEntity(e.get(i));
        }
        mob.swing(InteractionHand.MAIN_HAND);
        mob.level().playSound(mob, mob.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE,
            mob.getSoundSource(), 1.0F, mob.getRandom().nextFloat() * 0.2F + 1.2F);
    }

    @Override
    public void onGeneralTimerExpire(Mob mob, String key) {
        super.onGeneralTimerExpire(mob, key);
        NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);
        if (key.equals("timeLimit")) {
            this.progressDown(mob);
            if (this.isInAnyProcess(mob))
                tamable.getTimerComponent().addTimer("timeLimit", 90 * 20, true);
        }
    }

    private static void cacheMainHandItem(Mob mob) {
        NFFTamableComponent.getOptional(mob).ifPresent(t -> {
            if (!t.getGeneralNBT().contains("mainHandItem", Tag.TAG_COMPOUND))
            {
                CompoundTag nbt = new CompoundTag();
                mob.getItemInHand(InteractionHand.MAIN_HAND).save(nbt);
                t.getGeneralNBT().put("mainHandItem", nbt);
            }
        });
    }

    private static void resumeCachedMainHandItem(Mob mob) {
        NFFTamableComponent.getOptional(mob).ifPresent(t -> {
            if (t.getGeneralNBT().contains("mainHandItem", Tag.TAG_COMPOUND))
            {
                mob.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.of(
                    t.getGeneralNBT().getCompound("mainHandItem")));
            } else {
                mob.setItemInHand(InteractionHand.MAIN_HAND, Items.IRON_SWORD.getDefaultInstance());
            }
        });
    }

    private static void removeCachedMainHandItem(Mob mob) {
        NFFTamableComponent.getOptional(mob).ifPresent(t -> {
            t.getGeneralNBT().remove("mainHandItem");
        });
    }


    public static class RequiredAction {
        private static final Map<Integer, RequiredAction> ALL_ACTIONS = new HashMap<>();
        public static RequiredAction LIGHTNING = new RequiredAction(0, 1.0d, ModItems.LIGHTNING_PARTICLE.get().getDefaultInstance());
        public static RequiredAction SWORD_ATTACK = new RequiredAction(1, 1.0d, Items.IRON_SWORD.getDefaultInstance());
        public static RequiredAction AXE_ATTACK = new RequiredAction(2, 1.0d, Items.IRON_AXE.getDefaultInstance());
        public static RequiredAction BOW_SHOOTING = new RequiredAction(3, 1.0d, Items.BOW.getDefaultInstance());
        public static RequiredAction TRIDENT_THROWING = new RequiredAction(4, 1.0d, Items.TRIDENT.getDefaultInstance());
        //public static RequiredAction SHIELD_BLOCKING = new RequiredAction(5, 1.0d, Items.SHIELD.getDefaultInstance());
        
        public static RequiredAction pick() {
            WeightedRandomSelector<RequiredAction> selector = new WeightedRandomSelector<>();
            ALL_ACTIONS.forEach((k, v) -> selector.add(v, v.weight));
            return selector.select();
        }
        
        public static RequiredAction byId(int id) {
            return Optional.ofNullable(ALL_ACTIONS.get(id)).orElseThrow(() -> new IllegalArgumentException("Invalid required action ID."));
        }

        public final int id;
        public final double weight;
        public final ItemStack hint;
        private RequiredAction(int id, double weight, ItemStack hint) {
            if (ALL_ACTIONS.containsKey(id)) throw new DuplicateRegistryEntryException("Duplicate valkyrie required action: " + id);
            this.id = id;
            this.weight = weight;
            this.hint = hint;
            ALL_ACTIONS.put(id, this);
        }
    }

    public static class ShootingGoal extends NFURangedAttackGoal<Valkyrie> {

        public ShootingGoal(Valkyrie mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
            super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && isInThisProcess(this.getMob()).isPresent();
        }
        @Override
        public void start() {
            super.start();
            NFFTamableComponent.getOptional(this.getMob()).ifPresent(tamable -> {
                cacheMainHandItem(this.getMob());
                this.getMob().setItemInHand(InteractionHand.MAIN_HAND, GaiaRegistry.MAGIC_STAFF.get().getDefaultInstance());
            });
        }
        public void stop() {
            super.start();
            NFFTamableComponent.getOptional(this.getMob()).ifPresent(t -> {
                if (t.getEntity().getMainHandItem().is(GaiaRegistry.MAGIC_STAFF.get())) {
                    resumeCachedMainHandItem(this.getMob());
                }
            });
        }

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NFFGirlsGaia.MOD_ID)
    public static class EventHandlers {
        @SubscribeEvent
        public static void handleThunderStrike(EntityStruckByLightningEvent event) {
            // When the mob is hit by a player-caused lightning
            if (event.getEntity().getType().equals(GaiaRegistry.VALKYRIE.getEntityType())
                && event.getEntity() instanceof Valkyrie mob
                && event.getLightning().getCause() != null
                && NFFTamingMapping.contains(mob) && NFFTamingMapping.getProcess(mob) instanceof GaiaValkyrieTamingProcess proc
            ) {
                NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);
                ServerPlayer player = event.getLightning().getCause();
                // Lightning strike first to initiate the process. Handle first lightning
                if (!proc.isInAnyProcess(mob)) {
                    tamable.getGeneralNBT().putUUID("ongoingPlayer", event.getLightning().getCause().getUUID());
                    tamable.getGeneralNBT().putInt("progress", 0);
                    CompoundTag offhandAsNBT = new CompoundTag();
                    mob.getItemInHand(InteractionHand.OFF_HAND).save(offhandAsNBT);
                    mob.setHealth(mob.getMaxHealth());
                    proc.refreshRequirement(mob);
                    NFUParticleStatics.sendParticlesToEntity(mob, ParticleTypes.EXPLOSION, -0.5d, 1.5d, 7, 0d);
                    mob.playSound(SoundEvents.GENERIC_EXPLODE, 4f, 1f);
                }
                // Case when it's required to do a lightning strike
                // Required Action #0 - thunder strike
                else if (proc.requiresActionNow(mob)) {
                    if (proc.getRequiredAction(mob).equals(RequiredAction.LIGHTNING)) {
                        proc.progressUp(mob);
                    }
                }
            }
        }


        @SubscribeEvent
        public static void onHurt(LivingDamageEvent event) {
            if (event.isCanceled()) return;
            if (event.getEntity().getType().equals(GaiaRegistry.VALKYRIE.getEntityType())
                && event.getEntity() instanceof Valkyrie mob
                && event.getSource().getEntity() instanceof Player player
                && NFFTamingMapping.contains(mob) && NFFTamingMapping.getProcess(mob) instanceof GaiaValkyrieTamingProcess proc
                && proc.isInAnyProcess(mob)) {
                if (mob.getHealth() - event.getAmount() < proc.getMinHP(mob)) {
                    event.setAmount(Math.max(0f, mob.getHealth() - proc.getMinHP(mob)));
                }
                if (proc.getOngoingPlayer(mob).filter(p -> p.equals(player)).isPresent()
                    && proc.requiresActionNow(mob))
                {
                    if (event.getSource().is(DamageTypes.PLAYER_ATTACK)) {
                        // Required Action #1 - sword attack
                        if (proc.getRequiredAction(mob).equals(RequiredAction.SWORD_ATTACK)
                            && player.getMainHandItem().getItem() instanceof SwordItem) {
                            proc.progressUp(mob);
                            event.setAmount(0f);
                            return;
                        }
                        // Required Action #2 - axe attack
                        else if (proc.getRequiredAction(mob).equals(RequiredAction.AXE_ATTACK)
                            && player.getMainHandItem().getItem() instanceof AxeItem) {
                            proc.progressUp(mob);
                            event.setAmount(0f);
                            return;
                        }
                    }
                    if (proc.getRequiredAction(mob).equals(RequiredAction.BOW_SHOOTING)
                        && event.getSource().getDirectEntity() instanceof Arrow) {
                        proc.progressUp(mob);
                        event.setAmount(0f);
                        return;
                    }
                    if (proc.getRequiredAction(mob).equals(RequiredAction.TRIDENT_THROWING)
                        && event.getSource().getDirectEntity() instanceof ThrownTrident) {
                        proc.progressUp(mob);
                        event.setAmount(0f);
                        return;
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onDeath(LivingDeathEvent event) {
            if (event.isCanceled()) return;
            if (event.getEntity().getType().equals(GaiaRegistry.VALKYRIE.getEntityType())
                && event.getEntity() instanceof Valkyrie mob
                && event.getSource().getEntity() instanceof Player player
                && NFFTamingMapping.contains(mob) && NFFTamingMapping.getProcess(mob) instanceof GaiaValkyrieTamingProcess proc
                && proc.getOngoingPlayer(mob).filter(p -> p.equals(player)).isPresent()
                && proc.isInAnyProcess(mob)
                && proc.getMinHP(mob) > 0)
            {
                mob.setHealth(proc.getMinHP(mob));
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onProjectileHit(ProjectileHitEvent event) {
            if (event.getEntityHitResult() != null
                && event.getEntityHitResult().getEntity() instanceof Valkyrie mob
                && mob.getType().equals(GaiaRegistry.VALKYRIE.getEntityType())
                && event.getEntity().getOwner() instanceof Player player
                && NFFTamingMapping.contains(mob) && NFFTamingMapping.getProcess(mob) instanceof GaiaValkyrieTamingProcess proc
                && proc.getOngoingPlayer(mob).filter(p -> p.equals(player)).isPresent()
                && proc.requiresActionNow(mob))
            {
                if (event.getEntity() instanceof AbstractArrow arrow
                    && proc.getRequiredAction(mob).equals(RequiredAction.BOW_SHOOTING)) {
                    proc.progressUp(mob);
                    arrow.setBaseDamage(0d);
                    return;
                } else if (event.getEntity() instanceof ThrownTrident trident
                    && proc.getRequiredAction(mob).equals(RequiredAction.TRIDENT_THROWING)) {
                    proc.progressUp(mob);
                    trident.setBaseDamage(0d);
                    return;
                }
            }
        }

    }
}
