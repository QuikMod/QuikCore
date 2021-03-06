/*
 */
package com.github.quikmod.quikcore.config;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.reflection.exceptions.UnknownQuikDomainException;
import com.github.quikmod.quikcore.util.ReflectionHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

	private final QuikConfigAdapter provider;

	private final Map<Object, List<Field>> configurables;

	private final List<QuikConfigListener> configurationListeners;

	public QuikConfig(QuikConfigAdapter provider) {
		this.configurables = new HashMap<>();
		this.configurables.put(null, new ArrayList<>());
		this.configurationListeners = new ArrayList<>();
		this.provider = provider;
	}

	public void load() {
		//AgriCore.getCoreLogger().debug("Loading Config!");
		this.configurables.forEach(
				(configurable, fields) -> fields.forEach(
						(field) -> handleConfigurable(configurable instanceof Class ? null : configurable, field)
				)
		);
		this.configurationListeners.forEach(QuikConfigListener::onConfigChanged);
		//AgriCore.getCoreLogger().debug("Loaded Config!");
	}

	public void save() {
		//AgriCore.getCoreLogger().debug("Saving Config!");
		this.provider.save();
		//AgriCore.getCoreLogger().debug("Config Saved!");
	}

	public final synchronized void addListener(QuikConfigListener listener) {
		this.configurationListeners.add(listener);
	}

	public final synchronized void removeListener(QuikConfigListener listener) {
		if (listener != null) {
			this.configurationListeners.remove(listener);
		}
	}

	public final synchronized boolean addConfigurableField(Field f) {
		return Modifier.isStatic(f.getModifiers()) && configurables.get(null).add(f);
	}

	public final synchronized void addConfigurable(Object configurable) {
		if (!configurables.containsKey(configurable)) {
			List<Field> fields = new ArrayList<>();
			ReflectionHelper.forEachFieldIn(configurable, QuikConfigurable.class, (field, anno) -> {
				if (Modifier.isFinal(field.getModifiers())) {
					QuikCore.getCoreLogger().error("Configurable Field: " + field.getName() + " is final!");
				} else {
					handleConfigurable(configurable, field);
					fields.add(field);
				}
			});
			configurables.put(configurable, fields);
		}
	}

	protected final void handleConfigurable(Object configurable, Field f) {

		//AgriCore.getCoreLogger().debug("Loading Configurable Field: " + configurable.toString());
		final QuikConfigurable anno = f.getAnnotation(QuikConfigurable.class);
		try {

			f.setAccessible(true);
			Object obj = f.get(configurable);

			String config = QuikCore.getDomains().resolveDomain(f);
			String key = anno.key();
			String comment = anno.comment();

			final String category = anno.category();

			if (configurable instanceof QuikConfigurableInstance) {
				QuikConfigurableInstance ins = (QuikConfigurableInstance) configurable;
				key = ins.resolve(key).replaceAll("\\s+", "_").toLowerCase();
				comment = ins.resolve(comment);
			}

			if (obj instanceof String) {
				f.set(configurable, provider.getString(config, category, key, (String) obj, comment));
			} else if (obj instanceof Boolean) {
				f.set(configurable, provider.getBoolean(config, category, key, (boolean) obj, comment));
			} else if (obj instanceof Integer) {
				f.set(configurable, provider.getInt(config, category, key, (int) obj, (int) anno.min(), (int) anno.max(), comment));
			} else if (obj instanceof Float) {
				f.set(configurable, provider.getFloat(config, category, key, (float) obj, (float) anno.min(), (float) anno.max(), comment));
			} else if (obj instanceof Double) {
				f.set(configurable, provider.getDouble(config, category, key, (double) obj, anno.min(), anno.max(), comment));
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

	public Stream<QuikConfigurable> getElements() {
		return this.configurables
				.values()
				.stream()
				.flatMap(l -> l.stream())
				.map(e -> e.getAnnotation(QuikConfigurable.class));
	}

	public Set<String> getConfigs() {
		return this.provider.getConfigs();
	}

	public Set<String> getConfigCategories(String config) {
		return this.provider.getConfigCategories(config);
	}

	public boolean getBoolean(String config, String category, String key, boolean defaultValue, String comment) {
		return this.provider.getBoolean(config, category, key, defaultValue, comment);
	}

	public int getInt(String config, String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
		return this.provider.getInt(config, category, key, defaultValue, minValue, maxValue, comment);
	}

	public float getFloat(String config, String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
		return this.provider.getFloat(config, category, key, defaultValue, minValue, maxValue, comment);
	}

	public double getDouble(String config, String category, String key, double defaultValue, double minValue, double maxValue, String comment) {
		return this.provider.getDouble(config, category, key, defaultValue, minValue, maxValue, comment);
	}

	public String getString(String config, String category, String key, String defaultValue, String comment) {
		return this.provider.getString(config, category, key, defaultValue, comment);
	}

	@Override
	public String toString() {
		return "\nAgriConfig:\n" + this.provider.toString().replaceAll("\\{", "{\n\t").replaceAll("}", "\n}\n").replaceAll(", ", ",\n\t");
	}

}
