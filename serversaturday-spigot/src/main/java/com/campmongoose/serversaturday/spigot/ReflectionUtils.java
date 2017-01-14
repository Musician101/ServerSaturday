package com.campmongoose.serversaturday.spigot;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;

public class ReflectionUtils {

    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String CRAFTBUKKIT = "org.bukkit.craftbukkit." + VERSION + ".";
    private static final String NMS = "net.minecraft." + VERSION + ".";

    private ReflectionUtils() {

    }

    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName(NMS + className);
    }

    public static Class<?> getCraftClass(String className) throws ClassNotFoundException {
        return Class.forName(CRAFTBUKKIT + className);
    }

    public static Class<?> getAttributeClass(String className) throws ClassNotFoundException {
        return getCraftClass("attribute." + className);
    }

    public static Class<?> getBlockClass(String className) throws ClassNotFoundException {
        return getCraftClass("block." + className);
    }

    public static Class<?> getBossClass(String className) throws ClassNotFoundException {
        return getCraftClass("boss." + className);
    }

    public static Class<?> getChunkIOClass(String className) throws ClassNotFoundException {
        return getCraftClass("chunkio." + className);
    }

    public static Class<?> getCommandClass(String className) throws ClassNotFoundException {
        return getCraftClass("command." + className);
    }

    public static Class<?> getConversationsClass(String className) throws ClassNotFoundException {
        return getCraftClass("conversations." + className);
    }

    public static Class<?> getEnchantmentsClass(String className) throws ClassNotFoundException {
        return getCraftClass("enchantments." + className);
    }

    public static Class<?> getEntityClass(String className) throws ClassNotFoundException {
        return getCraftClass("entity." + className);
    }

    public static Class<?> getEventClass(String className) throws ClassNotFoundException {
        return getCraftClass("event." + className);
    }

    public static Class<?> getGeneratorClass(String className) throws ClassNotFoundException {
        return getCraftClass("generator." + className);
    }

    public static Class<?> getHelpClass(String className) throws ClassNotFoundException {
        return getCraftClass("help." + className);
    }

    public static Class<?> getInventoryClass(String className) throws ClassNotFoundException {
        return getCraftClass("inventory." + className);
    }

    public static Class<?> getMapClass(String className) throws ClassNotFoundException {
        return getCraftClass("map." + className);
    }

    public static Class<?> getMetaDataClass(String className) throws ClassNotFoundException {
        return getCraftClass("metadata." + className);
    }

    public static Class<?> getPotionclass(String className) throws ClassNotFoundException {
        return getCraftClass("potion." + className);
    }

    public static Class<?> getProjectilesClass(String className) throws ClassNotFoundException {
        return getCraftClass("projectiles." + className);
    }

    public static Class<?> getSchedulerClass(String className) throws ClassNotFoundException {
        return getCraftClass("scheduler." + className);
    }

    public static Class<?> getScoreboardClass(String className) throws ClassNotFoundException {
        return getCraftClass("scoreboard." + className);
    }

    public static Class<?> getUtilClass(String className) throws ClassNotFoundException {
        return getCraftClass("util." + className);
    }

    public static Class<?> getPermissionsClass(String className) throws ClassNotFoundException {
        return getUtilClass("permissions." + className);
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... argTypes) throws NoSuchMethodException {
        return clazz.getMethod(name, argTypes);
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }

    public static Object invokeMethod(Method method, Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }
}
