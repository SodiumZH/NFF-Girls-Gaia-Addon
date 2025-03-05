package net.sodiumzh.nff.girls.gaia.mixins;

import net.sodiumzh.nautils.mixin.NaUtilsMixin;
import net.sodiumzh.nff.girls.gaia.events.GaiaMobFinalizeSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import gaia.entity.AbstractGaiaEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.MinecraftForge;

@Mixin(AbstractGaiaEntity.class)
public class NFFGirlsAbstractGaiaEntityMixin implements NaUtilsMixin<AbstractGaiaEntity>
{
	@Inject(method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;",
			at = @At(value = "INVOKE", target = "Lgaia/entity/AbstractGaiaEntity;finalizeAttributes()V", remap = false), cancellable = true, expect = -1)
	private void onFinalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance,
			MobSpawnType spawnType, SpawnGroupData groupData, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> callback, @Local(ordinal = 0) SpawnGroupData result)
	{
		if (MinecraftForge.EVENT_BUS.post(new GaiaMobFinalizeSpawnEvent(this.caller())))
			callback.setReturnValue(result);
	}
}
