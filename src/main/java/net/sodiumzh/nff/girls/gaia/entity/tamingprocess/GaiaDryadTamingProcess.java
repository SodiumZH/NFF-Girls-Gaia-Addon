package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import gaia.entity.AbstractGaiaEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.tamingprocesses.hmag.HmagAlrauneTamingProcess;

public class GaiaDryadTamingProcess extends HmagAlrauneTamingProcess
{
	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		if (mob instanceof AbstractGaiaEntity ge)
			return ge.isFriendly() && super.additionalConditions(player, mob);
		else return false;
	}
}
