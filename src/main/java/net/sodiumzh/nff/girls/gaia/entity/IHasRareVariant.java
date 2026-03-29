package net.sodiumzh.nff.girls.gaia.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.sodiumzh.nfu.math.RandomSelection;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public interface IHasRareVariant {

    /**
     * Get which rare variant this mob is. -1 if not a rare variant.
     * This method should be linked to a {@link EntityDataAccessor}.
     * The variant info should be saved/loaded in entity class.
     */
    public int getRareVariantID();

    public default Optional<RareVariant> getRareVariant() {
        return Optional.ofNullable(this.rareVariantByID(this.getRareVariantID()));
    }

    public default boolean isRareVariant() {
        return this.getRareVariant().isPresent();
    }

    /**
     * Set the rare variant type ID of this mob. Set -1 to use a normal variant.
     * This method should be linked to a {@link EntityDataAccessor}.
     * The variant info should be saved/loaded in entity class.
     */
    public void setRareVariantID(int id);

    /**
     * Get rare variant descriptor from ID.
     * <p>Note: ID should start from 0 and be continuous. Otherwise,
     * the default {@code pickRareVariant()} will be unable to correctly determine the amount of
     * possible variants.
     */
    public @Nullable RareVariant rareVariantByID(int id);

    /**
     * Invoked when friended, to randomly pick if this mob should be a rare variant.
     * Note: this method only randomly pick a rare variant but doesn't set anything. To apply to mob,
     * use {@code setRareVariant}.
     */
    public default int pickRareVariant() {
        RandomSelection<Integer> sel = new RandomSelection<>(-1);
        int i = 0;
        while (true) {
            RareVariant rv = this.rareVariantByID(i);
            if (rv == null) break;
            sel.add(i, rv.probability());
            ++i;
        }
        if (this instanceof Entity e)
            return sel.select(e.level.getRandom());
        else return sel.select();
    }


    public static record RareVariant(String name, double probability, Component displayName) {

        public RareVariant(String name, double probability, String translationKey, Object... translationArgs) {
            this(name, probability, NFUInfoStatics.createTranslatable(translationKey, translationArgs));
        }

        public boolean is(String name) {
            return this.name().equals(name);
        }

        public boolean is(RareVariant other) {
            return this.name().equals(other.name());
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof RareVariant rv
                && this.name().equals(rv.name())
                && this.probability() == rv.probability()
                && this.displayName().equals(rv.displayName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name(), this.probability(), this.displayName());
        }
    }

}
