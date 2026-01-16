package net.sodiumzh.nff.girls.gaia.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEffects;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
/*import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import net.sodiumzh.nfu.registry.NFUEntityComponents;*/

import java.util.Comparator;
import java.util.Optional;

public class AntPheromoneEffect extends MobEffect {

    public AntPheromoneEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventListeners {

        // Ant-like mobs (defined by tag) are neutral to players with this effect
        /*@SubscribeEvent
        public static void onChangeTarget(LivingChangeTargetEvent event) {
            if (event.getEntity().hasEffect(NFFGirlsGaiaEffects.ANT_PHEROMONE.get())
                && event.getNewTarget() != null
                && event.getNewTarget().getType().is(NFFGirlsGaiaTags.ANT_PHEROMONE_AFFECTED))
            {
                if (EntityComponentAPI.getComponentManager(event.getNewTarget())
                    .getSubComponent("default_anger_handler", NFUEntityComponents.DEFAULT_ANGER_HANDLER.get())
                    .filter(c -> c.isAngryAt(event.getEntity())).isEmpty()) {
                    event.setCanceled(true);
                }
            }
        }*/

        // Ant-like mobs attack the player's target when idle
       /* @SubscribeEvent
        public static void onTick(LivingEvent.LivingTickEvent event) {
            if (event.getEntity() instanceof Mob mob    // For mobs
                && mob.getType().is(NFFGirlsGaiaTags.ANT_PHEROMONE_AFFECTED)    // with tag
                && mob.getTarget() == null)  // when idle
            {
                Optional<LivingEntity> e = mob.level().getEntitiesOfClass(LivingEntity.class, event.getEntity().getBoundingBox().inflate(8d, 8d, 8d))
                    .stream()
                    .filter(l -> l.hasEffect(NFFGirlsGaiaEffects.ANT_PHEROMONE.get())
                        && EntityComponentAPI.getComponentManager(mob).getSubComponent("default_anger_handler", NFUEntityComponents.DEFAULT_ANGER_HANDLER.get())
                            .filter(c -> c.isAngryAt(l)).isEmpty()
                        && l.getLastHurtMob() != null
                        && !l.getLastHurtMob().hasEffect(NFFGirlsGaiaEffects.ANT_PHEROMONE.get())
                        && !l.getLastHurtMob().getType().is(NFFGirlsGaiaTags.ANT_PHEROMONE_AFFECTED)
                        ))
                    .min(PLAYER_FIRST); // Find a living with the pheromone effect, prioritize player
                e.ifPresent(l -> mob.setTarget(l.getLastHurtMob()));
            }
        }*/

        private static final Comparator<LivingEntity> PLAYER_FIRST = (l1, l2) -> {
            boolean b1 = l1 instanceof Player;
            boolean b2 = l2 instanceof Player;
            if (b1 == b2) return 0;
            else if (b1) return -1;
            else return 1;
        };

    }
}
