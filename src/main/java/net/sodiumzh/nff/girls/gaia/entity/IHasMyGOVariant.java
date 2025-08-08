package net.sodiumzh.nff.girls.gaia.entity;

import net.minecraft.network.chat.Component;

public interface IHasMyGOVariant {
    public boolean itsMyGO();

    public void setMyGO(boolean value);

    public Component getMyGOName();
}
