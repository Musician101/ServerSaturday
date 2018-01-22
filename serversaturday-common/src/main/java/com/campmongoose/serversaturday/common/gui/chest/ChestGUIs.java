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

public abstract class ChestGUIs<B extends ChestGUIBuilder<B, C, G, I, P, S, T>, C, G extends ChestGUI<C, G, I, P, S>, I, L extends Build<S, N, U>, N, P, S, T, U extends Submitter<L, S, N>> {

    @Nonnull
    public abstract Optional<G> allSubmissions(int page, @Nonnull P player, @Nullable G prevGUI);

    @Nonnull
    protected abstract B build(int featureSlot, int teleportSlot, @Nonnull L build, @Nonnull P player, @Nullable G prevGUI);

    @Nonnull
    protected abstract B builder(int size, int backButtonSlot, @Nonnull T name, @Nonnull P player, @Nullable G prevGUI);

    @Nonnull
    public abstract Optional<G> editBuild(@Nonnull L build, @Nonnull U submitter, @Nonnull P player, @Nullable G prevGUI);

    @Nonnull
    protected abstract <O> B paged(@Nonnull T name, @Nonnull P player, @Nullable G prevGUI, int page, @Nonnull C clickType, @Nonnull List<O> contents, @Nonnull Function<O, S> itemStackMapper, @Nullable BiFunction<P, O, BiConsumer<G, P>> actionMapper, @Nonnull BiConsumer<P, Integer> pageNavigator);

    @Nonnull
    public abstract Optional<G> submissions(int page, @Nonnull P player, @Nullable G prevGUI);

    @Nonnull
    public abstract Optional<G> submitter(int page, @Nonnull P player, @Nonnull U submitter, @Nullable G prevGUI);

    @Nonnull
    public abstract Optional<G> viewBuild(@Nonnull L build, @Nonnull U submitter, @Nonnull P player, @Nullable G prevGUI);
}