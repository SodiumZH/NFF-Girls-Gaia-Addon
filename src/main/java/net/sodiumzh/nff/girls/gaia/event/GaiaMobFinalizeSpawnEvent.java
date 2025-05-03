package net.sodiumzh.nff.girls.gaia.event;

import gaia.entity.AbstractGaiaEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.sodiumzh.nfu.event.NFULivingEvent;

@Cancelable
public class GaiaMobFinalizeSpawnEvent extends NFULivingEvent<AbstractGaiaEntity>
{
	public GaiaMobFinalizeSpawnEvent(AbstractGaiaEntity e)
	{
		super(e);
	}
}
