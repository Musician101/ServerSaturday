package com.campmongoose.serversaturday;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import net.kyori.adventure.util.TriState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public class Messages extends MiniMessageTranslator {

    private final Map<Locale, ConfigurationNode> locales = new HashMap<>();

    @Override
    public TriState hasAnyTranslations() {
        return TriState.TRUE;
    }

    @Override
    protected @Nullable String getMiniMessageString(String key, Locale locale) {
        return locales.getOrDefault(locale, locales.get(Locale.US)).node(NodePath.of(key.split("\\."))).getString();
    }

    @Override
    public Key name() {
        return Key.key("serversaturday:lang");
    }

    public void load() throws IOException {
        getPlugin().saveResource("lang/en_us/main.yml", false);
        try (Stream<Path> langStream = Files.list(getPlugin().getDataPath().resolve("lang"))) {
            ExceptionUtils.throwIOException("One or more errors occurred while attempting to load lang files.", langStream.map(this::loadLocales).mapMulti(Optional::ifPresent));
        }
    }

    private Optional<IOException> loadLocales(Path path) {
        return Locale.availableLocales().map(locale -> loadLocale(path, locale)).<IOException>mapMulti(Optional::ifPresent).collect(ExceptionUtils.toIOException("One or more errors occurred while trying to load locales."));
    }

    private Optional<IOException> loadLocale(Path path, Locale locale) {
        try (Stream<Path> localeStream = Files.walk(path)) {
            return localeStream.filter(this::isYAML).map(this::loader).map(loader -> loadFile(locale, loader)).filter(Objects::nonNull).collect(ExceptionUtils.toIOException("One or more errors occurred while loading " + locale));
        }
        catch (IOException e) {
            return Optional.of(e);
        }
    }

    private boolean isYAML(Path path) {
        return !Files.isDirectory(path) && path.getFileName().toString().endsWith(".yml");
    }

    private YamlConfigurationLoader loader(Path path) {
        return YamlConfigurationLoader.builder().path(path).build();
    }

    @Nullable
    private IOException loadFile(Locale locale, YamlConfigurationLoader loader) {
        try {
            locales.put(locale, loader.load());
            return null;
        }
        catch (ConfigurateException e) {
            return e;
        }
    }
}
