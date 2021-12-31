package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Database;
import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import io.musician101.musicianlibrary.java.storage.database.mongo.MongoSerializable;
import io.musician101.musicianlibrary.java.storage.database.sql.SQLStatementSerializable;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.Document;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public final class Submitter {

    @Nonnull
    private final Map<String, Build> builds = new HashMap<>();
    @Nonnull
    private final UUID uuid;
    @Nonnull
    private String name;

    public Submitter(@Nonnull String name, @Nonnull UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Nullable
    public Build getBuild(@Nonnull String name) {
        return builds.get(name);
    }

    @Nonnull
    public List<Build> getBuilds() {
        return new ArrayList<>(builds.values());
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public UUID getUUID() {
        return uuid;
    }

    @Nonnull
    public Build newBuild(@Nonnull String name, @Nonnull Location location) {
        builds.putIfAbsent(name, new Build(name, location));
        return builds.get(name);
    }

    public void renameBuild(@Nonnull String newName, @Nonnull Build build) {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(newName, build);
    }

    public record Serializer() implements MongoSerializable<Submitter>, SQLStatementSerializable<Submitter>, TypeSerializer<Submitter> {

        @Override
        public Submitter deserialize(Type type, ConfigurationNode node) throws SerializationException {
            if (!type.equals(TypeToken.get(Submitter.class).getType())) {
                return null;
            }

            String name = node.node(Config.NAME).getString();
            if (name == null) {
                throw new SerializationException("Submitter name cannot be null.");
            }

            UUID uuid = node.node(Config.UUID).get(UUID.class);
            if (uuid == null) {
                throw new SerializationException("Submitter UUID cannot be null.");
            }

            Submitter submitter = new Submitter(name, uuid);
            node.node(Config.BUILDS).getList(Build.class);
            return submitter;
        }

        @Override
        public Submitter deserialize(@Nullable Document document) {
            if (document == null) {
                return null;
            }

            String name = document.getString(Config.NAME);
            if (name == null) {
                throw new IllegalStateException("Submitter name cannot be null.");
            }

            UUID uuid = document.get(Config.UUID, UUID.class);
            if (uuid == null) {
                throw new IllegalStateException("Submitter UUID cannot be null.");
            }

            Submitter submitter = new Submitter(name, uuid);
            submitter.builds.putAll(document.getList(Config.BUILDS, Document.class, new ArrayList<>()).stream().map(d -> new Build.Serializer().deserialize(d)).collect(Collectors.toMap(Build::getName, v -> v)));
            return submitter;
        }

        @Nonnull
        @Override
        public List<Submitter> fromStatement(@Nonnull Statement statement) throws SQLException {
            List<Submitter> submitters = new ArrayList<>();
            statement.addBatch(Database.CREATE_TABLE);
            statement.addBatch(Database.SELECT_TABLE);
            statement.executeBatch();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString(Database.UUID));
                String name = rs.getString(Database.USERNAME);
                Submitter submitter = submitters.stream().filter(s -> uuid.equals(s.getUUID())).findFirst().orElseGet(() -> {
                    Submitter s = new Submitter(name, uuid);
                    submitters.add(s);
                    return s;
                });

                String buildName = rs.getString(Database.BUILD_NAME);
                String world = rs.getString(Database.WORLD_NAME);
                double x = rs.getDouble(Database.X);
                double y = rs.getDouble(Database.Y);
                double z = rs.getDouble(Database.Z);
                float pitch = rs.getFloat(Database.PITCH);
                float yaw = rs.getFloat(Database.YAW);
                Location location = new Location(world, x, y, z, pitch, yaw);
                Build build = submitter.newBuild(buildName, location);
                build.setFeatured(rs.getBoolean(Database.FEATURED));
                build.setSubmitted(rs.getBoolean(Database.SUBMITTED));
                build.setDescription(rs.getString(Database.DESCRIPTION));
                build.setResourcePack(rs.getString(Database.RESOURCE_PACKS));
            }

            rs.close();
            return submitters;
        }

        @Override
        public Document serialize(@Nonnull Submitter obj) {
            Document document = new Document();
            document.put(Config.NAME, obj.name);
            document.put(Config.UUID, obj.uuid);
            document.put(Config.BUILDS, obj.getBuilds().stream().map(build -> new Build.Serializer().serialize(build)).collect(Collectors.toList()));
            return document;
        }

        @Override
        public void serialize(Type type, @Nullable Submitter obj, ConfigurationNode node) throws SerializationException {
            if (obj == null || !type.equals(new TypeToken<Submitter>() {

            }.getType())) {
                return;
            }

            node.node(Config.NAME).set(obj.name);
            node.node(Config.NAME).set(obj.uuid);
            node.node(Config.BUILDS).setList(new TypeToken<>() {

            }, obj.getBuilds());
        }

        @Override
        public void toStatement(@Nonnull Statement statement, @Nonnull List<Submitter> list) throws SQLException {
            statement.addBatch(Database.CLEAR_TABLE);
            statement.addBatch(Database.CREATE_TABLE);
            for (Submitter entry : list) {
                for (Build build : entry.getBuilds()) {
                    statement.addBatch(Database.addBuild(entry, build));
                }
            }

        }
    }
}
