package net.sodiumzh.nff.girls.gaia.entity;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

/**
 * Adding to NFF Girls mobs of which the original mob has Gaia dynamic goal mechanic, to prevent
 * unexpected dynamic goal addition. Implemented in event listeners.
 */
public interface IBlocksGaiaDynamicGoals {

    public List<Class<? extends Goal>> getGoalsToRemove();

}
