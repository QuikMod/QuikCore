/*
 */
package com.github.quikmod.quikcore.config;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.defaults.QuikDefaultConfig;
import com.github.quikmod.quikcore.reflection.QuikDomains;
import com.github.quikmod.quikcore.reflection.exceptions.UnknownQuikDomainException;
import com.github.quikmod.quikcore.util.ReflectionHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author RlonRyan
 */
public class QuikConfig {

    private static final Map<Object, List<Field>> CONFIGURABLES;

    private static final List<QuikConfigListener> LISTENERS;

    private static QuikConfigAdapter adapter;

    static {
        CONFIGURABLES = new HashMap<>();
        CONFIGURABLES.put(null, new ArrayList<>());
        LISTENERS = new ArrayList<>();
        adapter = new QuikDefaultConfig(Paths.get("config"));
    }

    private QuikConfig() {
    }

    public static void load() {
        //AgriCore.getCoreLogger().debug("Loading Config!");
        QuikConfig.CONFIGURABLES.forEach(
                (configurable, fields) -> fields.forEach(
                        (field) -> handleConfigurable(configurable instanceof Class ? null : configurable, field)
                )
        );
        QuikConfig.LISTENERS.forEach(QuikConfigListener::onConfigChanged);
        //AgriCore.getCoreLogger().debug("Loaded Config!");
    }

    public static void save() {
        //AgriCore.getCoreLogger().debug("Saving Config!");
        QuikConfig.adapter.save();
        //AgriCore.getCoreLogger().debug("Config Saved!");
    }

    public static final synchronized void addListener(QuikConfigListener listener) {
        QuikConfig.LISTENERS.add(listener);
    }

    public static final synchronized void removeListener(QuikConfigListener listener) {
        if (listener != null) {
            QuikConfig.LISTENERS.remove(listener);
        }
    }

    public static final synchronized boolean addConfigurableField(Field f) {
        return Modifier.isStatic(f.getModifiers()) && CONFIGURABLES.get(null).add(f);
    }

    public static final synchronized void addConfigurable(Object configurable) {
        if (!CONFIGURABLES.containsKey(configurable)) {
            List<Field> fields = new ArrayList<>();
            ReflectionHelper.forEachFieldIn(configurable, QuikConfigurable.class, (field, anno) -> {
                if (Modifier.isFinal(field.getModifiers())) {
                    QuikCore.getCoreLogger().error("Configurable Field: " + field.getName() + " is final!");
                } else {
                    handleConfigurable(configurable, field);
                    fields.add(field);
                }
            });
            CONFIGURABLES.put(configurable, fields);
        }
    }

    protected static final void handleConfigurable(Object configurable, Field f) {

        //AgriCore.getCoreLogger().debug("Loading Configurable Field: " + configurable.toString());
        final QuikConfigurable anno = f.getAnnotation(QuikConfigurable.class);
        try {

            f.setAccessible(true);
            Object obj = f.get(configurable);

            String config = QuikDomains.resolveDomain(f);
            String key = anno.key();
            String comment = anno.comment();

            final String category = anno.category();

            if (configurable instanceof QuikConfigurableInstance) {
                QuikConfigurableInstance ins = (QuikConfigurableInstance) configurable;
                key = ins.resolve(key).replaceAll("\\s+", "_").toLowerCase();
                comment = ins.resolve(comment);
            }

            if (obj instanceof String) {
                f.set(configurable, QuikConfig.adapter.getString(config, category, key, (String) obj, comment));
            } else if (obj instanceof Boolean) {
                f.set(configurable, QuikConfig.adapter.getBoolean(config, category, key, (boolean) obj, comment));
            } else if (obj instanceof Integer) {
                f.set(configurable, QuikConfig.adapter.getInt(config, category, key, (int) obj, (int) anno.min(), (int) anno.max(), comment));
            } else if (obj instanceof Float) {
                f.set(configurable, QuikConfig.adapter.getFloat(config, category, key, (float) obj, (float) anno.min(), (float) anno.max(), comment));
            } else if (obj instanceof Double) {
                f.set(configurable, QuikConfig.adapter.getDouble(config, category, key, (double) obj, anno.min(), anno.max(), comment));
            } else {
                QuikCore.getCoreLogger().debug("Bad Type: " + f.getType().toString());
            }

        } catch (NumberFormatException e) {
            QuikCore.getCoreLogger().debug("Invalid parameter bound!");
        } catch (UnknownQuikDomainException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
            QuikCore.getCoreLogger().trace(e);
        }

    }

    public static Stream<QuikConfigurable> getElements() {
        return QuikConfig.CONFIGURABLES
                .values()
                .stream()
                .flatMap(l -> l.stream())
                .map(e -> e.getAnnotation(QuikConfigurable.class));
    }

    public static Set<String> getConfigs() {
        return QuikConfig.adapter.getConfigs();
    }

    public static Set<String> getConfigCategories(String config) {
        return QuikConfig.adapter.getConfigCategories(config);
    }

    public static boolean getBoolean(String config, String category, String key, boolean defaultValue, String comment) {
        return QuikConfig.adapter.getBoolean(config, category, key, defaultValue, comment);
    }

    public static int getInt(String config, String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
        return QuikConfig.adapter.getInt(config, category, key, defaultValue, minValue, maxValue, comment);
    }

    public static float getFloat(String config, String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
        return QuikConfig.adapter.getFloat(config, category, key, defaultValue, minValue, maxValue, comment);
    }

    public static double getDouble(String config, String category, String key, double defaultValue, double minValue, double maxValue, String comment) {
        return QuikConfig.adapter.getDouble(config, category, key, defaultValue, minValue, maxValue, comment);
    }

    public static String getString(String config, String category, String key, String defaultValue, String comment) {
        return QuikConfig.adapter.getString(config, category, key, defaultValue, comment);
    }

}
