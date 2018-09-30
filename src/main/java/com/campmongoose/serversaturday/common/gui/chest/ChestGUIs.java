package com.campmongoose.serversaturday.common.gui.chest;

import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ChestGUIs<B extends ChestGUIBuilder<B, C, G, I, P, S, T>, C, G extends ChestGUI<C, G, I, P, S>, I, L extends Build<L, S, N, U, T>, N, P, S, T, U extends Submitter<L, S, N, U, T>> {

    @Nonnull
    public abstract Optional<G> allSubmissions(int page, @Nonnull P player);

    @Nonnull
    protected abstract B build(int featureSlot, int teleportSlot, @Nonnull L build, @Nonnull P player);

    @Nonnull
    protected abstract B builder(int size, @Nonnull T name, @Nonnull P player);

    @Nonnull
    public abstract Optional<G> editBuild(@Nonnull L build, @Nonnull U submitter, @Nonnull P player);

    @Nonnull
    protected abstract <O> B paged(@Nonnull T name, @Nonnull P player, int page, @Nonnull C clickType, @Nonnull List<O> contents, @Nonnull Function<O, S> itemStackMapper, @Nullable BiFunction<P, O, BiConsumer<G, P>> actionMapper, @Nonnull BiConsumer<P, Integer> pageNavigator);

    @Nonnull
    public abstract Optional<G> submissions(int page, @Nonnull P player);

    @Nonnull
    public abstract Optional<G> submitter(int page, @Nonnull P player, @Nonnull U submitter);

    @Nonnull
    public abstract Optional<G> viewBuild(@Nonnull L build, @Nonnull U submitter, @Nonnull P player);
}
