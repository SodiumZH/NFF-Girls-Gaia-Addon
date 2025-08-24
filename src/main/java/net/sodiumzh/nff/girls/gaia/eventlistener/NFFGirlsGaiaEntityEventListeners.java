package net.sodiumzh.nff.girls.gaia.eventlistener;

import gaia.GrimoireOfGaia;
import gaia.capability.CapabilityHandler;
import gaia.entity.AbstractGaiaEntity;
import gaia.item.edible.MonsterFeedItem;
import gaia.registry.GaiaRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IHasMyGOVariant;
import net.sodiumzh.nff.girls.gaia.entity.NFFGirlsGaiaEntityUtils;
import net.sodiumzh.nff.girls.gaia.entity.NFFGirlsGaiaEntityUtils;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaMummyEntity;
import net.sodiumzh.nff.girls.gaia.event.GaiaMobFinalizeSpawnEvent;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaConfigs;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEntityTypes;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaItems;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.event.entity.NFFMobTamedEvent;
import net.sodiumzh.nfu.mixin.event.entity.ItemEntityHurtEvent;
import net.sodiumzh.nfu.mixin.event.entity.LivingStartBaseAiStepEvent;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.util.List;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsGaiaEntityEventListeners
{
	// BM events
	@SubscribeEvent
	public static void onTamed(NFFMobTamedEvent event)
	{
		if (event.mobBefore instanceof AbstractGaiaEntity before 
				&& INFFGirlsTamed.get(event.mobBefriended).isPresent()
				&& event.mobBefriended instanceof AbstractGaiaEntity after)
		{
			after.setBaby(before.isBaby());
			after.setVariant(before.getVariant());
			NFFGirlsGaiaEntityUtils.setMale(after, NFFGirlsGaiaEntityUtils.isMale(before));
			if (!NFFGirlsGaiaEntityUtils.isMale(before) && after instanceof IHasMyGOVariant mygo) {
				mygo.setMyGO(after.getRandom().nextDouble() < 0.05d);
				after.setCustomName(mygo.getMyGOName());
			}
			INFFGirlsTamed.get(after).ifPresent(tamed -> {
				after.getCapability(CapabilityHandler.CAPABILITY_FRIENDED).ifPresent((cap) -> {
					cap.setFriendly(true);
					cap.setFriendedBy(tamed.getOwnerUUID());
					after.setPersistenceRequired();
				});
			});
		}
	}

	// TODO this event listener is merged from HmagBansheeTamingProcess as the listener in that class only handle HMaG banshee. Merge this to NFFGirls and remove in the next version.
	/*@Deprecated
	@SubscribeEvent
	public static void preventWitherInProcess(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect().equals(MobEffects.WITHER)) {
			LivingEntity var2 = event.getEntity();
			if (var2 instanceof Mob e) {
				if (CNFFTamable.getOptional(e).map((tamable) -> {
					return tamable.getTamingProcess() instanceof HmagBansheeTamingProcess;
				}).orElse(false) && CNFFTamable.get(e).getTamingProcess().isInAnyProcess(e)) {
					event.setResult(Event.Result.DENY);
				}
			}
		}
	}*/

	// TODO: Move to NFU
	public static boolean isOnNaturalSpawn() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		return StackWalker.getInstance().walk((frames) -> (frames.anyMatch((frame) ->
				frame.getClassName().equals(NaturalSpawner.class.getName())
					|| frame.getClassName().equals(BaseSpawner.class.getName()))));
	}

	@SubscribeEvent
	public static void onJoinLevel(EntityJoinWorldEvent event) {
		// Prevent Gravemite spawn from friended Mummy
		if (event.getEntity().getType().equals(GaiaRegistry.GRAVEMITE.getEntityType())) {
			// Search by stacktrace
			if (StackWalker.getInstance().walk(frames ->
				frames.anyMatch(frame -> frame.getClassName().equals(GaiaMummyEntity.class.getName()))))
				event.setCanceled(true);
		}
		// Handle disabling male
		if (event.getEntity() instanceof AbstractGaiaEntity gaiaEntity
			&& !NFFGirlsGaiaConfigs.ValueCache.Tweak.SPAWNS_MALE_MOBS
			&& NFFGirlsGaiaEntityUtils.isMale(gaiaEntity)
			&& StackWalker.getInstance().walk(frames ->
				frames.anyMatch(frame -> frame.getClassName().equals(NaturalSpawner.class.getName()) || frame.getClassName().equals(BaseSpawner.class.getName())))) {
			NFFGirlsGaiaEntityUtils.setMale(gaiaEntity, false);
		}
	}

	@SubscribeEvent
	public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getEntity() instanceof AbstractGaiaEntity e && event.getSpawnReason().equals(MobSpawnType.NATURAL)) {
		if (event.getEntity() instanceof AbstractGaiaEntity e &&
			event.getSpawnType().equals(MobSpawnType.NATURAL)) {
			if (event.getEntity().getType().equals(GaiaRegistry.CECAELIA.getEntityType())
				&& ((AbstractGaiaEntity) event.getEntity()).getRandom().nextDouble() > NFFGirlsGaiaConfigs.ValueCache.Tweak.CECAELIA_SPAWN_RATE) {
				event.setResult(Event.Result.DENY);
				return;
			} else if (event.getEntity().getType().is(NFFGirlsGaiaTags.CAN_DISABLE_DAY_SPAWN)
				&& !NFFGirlsGaiaConfigs.ValueCache.Tweak.ALLOWS_DAY_HOSTILE_MOB_SPAWN_ON_GROUND) {
				event.setResult(Event.Result.DENY);
				return;
			}
		}
	}

	public static void onFinalizeSpawn(LivingSpawnEvent.SpecialSpawn event) {

	}

	@SubscribeEvent
	public static void onMobInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getItemStack().getItem() instanceof MonsterFeedItem) {
			INFFGirlsTamed.get(event.getTarget()).ifPresentOrElse(t -> {
				int amount = 0;
				if (event.getItemStack().is(GaiaRegistry.MONSTER_FEED.get()))
					amount = 10;
				else if (event.getItemStack().is(GaiaRegistry.PREMIUM_MONSTER_FEED.get()))
					amount = 100;
				if (amount > 0) {
					if (!event.getEntity().level.isClientSide) {
						t.getLevelHandler().addExp(amount);
						NFUParticleStatics.sendGlintParticlesToEntityDefault(t.asMob());
						event.getEntityLiving().getItemInHand(event.getHand()).shrink(1);
					}
				}
				event.setCanceled(true);
				event.setCancellationResult(amount == 0 ? InteractionResult.PASS : InteractionResult.sidedSuccess(event.getEntity().level.isClientSide));
			}, () -> {
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.PASS);
			});
		}
		// Allow removing mygo variant
		if (event.getItemStack().is(NFFGirlsGaiaItems.EVIL_GRINDSTONE.get())
			&& event.getTarget() instanceof Mob mob
			&& event.getTarget() instanceof IHasMyGOVariant v
			&& event.getEntity().isShiftKeyDown())
		{
			boolean done = false;
			if (v.itsMyGO()) {
				v.setMyGO(false);
				done = true;
			}
			if (mob.getCustomName() != null && mob.getCustomName().getString().equals(v.getMyGOName().getString())) {
				mob.setCustomName(null);
				done = true;
			}
			if (done) {
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(event.getEntity().level.isClientSide));
			}
		}
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		// Cancel explosion damages
		if ((event.getSource().isExplosion())
			&& INFFTamed.get(event.getSource().getEntity()).filter(t -> NFFTamedStatics.isLivingAlliedToBM(t, event.getEntityLiving())).isPresent())
		{
			if (event.getSource().getEntity().getType().equals(NFFGirlsGaiaEntityTypes.GAIA_VALKYRIE.get()))
				event.setCanceled(true);
		}
	}

	// NFU Mixin events

	@SubscribeEvent
	public static void preventExplosiveProjectilesBreakingItems(ItemEntityHurtEvent event) {
		if (!NFFGirlsGaiaConfigs.ValueCache.Tweak.EXPLOSIVE_PROJECTILE_DESTROYS_ITEMS
			&& (event.damageSource.isExplosion())
			&& event.damageSource.getDirectEntity() instanceof Projectile)
		{
			ResourceLocation typeKey = ForgeRegistries.ENTITIES.getKey(event.damageSource.getDirectEntity().getType());
			if (typeKey != null && typeKey.getNamespace().equals(GrimoireOfGaia.MOD_ID)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void removeGaiaDynamicGoals(LivingStartBaseAiStepEvent event) {
		if (event.getEntity() instanceof Mob mob
			&& event.getEntity() instanceof IBlocksGaiaDynamicGoals fix
			&& !event.getEntity().level.isClientSide) {
			var toRemove = fix.getGoalsToRemove();
			List<Goal> toRemoveGoals = mob.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal)
				.filter(g -> toRemove.contains(g.getClass())).toList();
			toRemoveGoals.forEach(mob.goalSelector::removeGoal);
		}
	}

	// GAIA Mixin events
	@SubscribeEvent
	public static void onGaiaFinalizeSpawn(GaiaMobFinalizeSpawnEvent event)
	{
		if (INFFGirlsTamed.get(event.getEntity()).isPresent())
			event.setCanceled(true);
	}




}
