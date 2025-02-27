package net.sodiumzh.nff.girls.gaia.events.hooks;

import gaia.entity.AbstractGaiaEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.sodiumzh.nautils.events.NaUtilsLivingEvent;

@Cancelable
public class GaiaMobFinalizeSpawnEvent extends NaUtilsLivingEvent<AbstractGaiaEntity>
{
	public GaiaMobFinalizeSpawnEvent(AbstractGaiaEntity e)
	{
		super(e);
	}
}
