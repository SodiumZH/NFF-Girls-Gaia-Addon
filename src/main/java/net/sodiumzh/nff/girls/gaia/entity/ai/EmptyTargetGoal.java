package net.sodiumzh.nff.girls.gaia.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class EmptyTargetGoal extends TargetGoal {
    public EmptyTargetGoal(Mob pMob) {
        super(pMob, false);
    }

    @Override
    public boolean canUse() {
        return false;
    }
}
