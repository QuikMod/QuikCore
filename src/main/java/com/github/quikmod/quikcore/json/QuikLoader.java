/*
 */
package com.github.quikmod.quikcore.json;

import com.github.quikmod.quikcore.core.QuikCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import com.github.quikmod.quikcore.config.QuikConfigurable;

/**
 *
 * @author RlonRyan
 */
public final class QuikLoader {
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	@QuikConfigurable(config = "QuikCore", key = "Enable JSON Writeback", category = "Core", comment = "Set to false to disable automatic JSON writeback.")
	private static boolean writeback = true;
	
	private QuikLoader() {
	}

	public static void loadDirectory(Path dir, QuikLoadableRegistry... registries) {
		try (Stream<Path> stream = Files.walk(dir)) {
			stream.forEach(p -> handleFile(dir, p, registries));
		} catch (IOException e) {
			QuikCore.getCoreLogger().debug("Unable to load directory: \"{0}\"!", dir);
		}
	}

	private static void handleFile(final Path root, Path location, QuikLoadableRegistry... registries) {
		for (QuikLoadableRegistry r : registries) {
			if (r.acceptsElement(location.getFileName().toString())) {
				loadElement(root, location, r);
			}
		}
	}

	private static <T extends QuikSerializable> void loadElement(Path root, Path location, QuikLoadableRegistry<T> registry) {

		// The Element
		T obj;

		// Ensure File Exists
		if (!Files.exists(location)) {
			QuikCore.getCoreLogger().warn("Tried to load non-existant File: \"{0}\"!", location);
			return;
		}

		// Attempt to load element.
		// If fails, return.
		try (Reader reader = Files.newBufferedReader(location)) {
			obj = GSON.fromJson(reader, registry.getElementClass());
			obj.setPath(root.relativize(location).toString().replaceAll("\\\\", "/"));
		} catch (IOException | JsonParseException e) {
			QuikCore.getCoreLogger().warn("Unable to load Element: \"{0}\"!", location);
			QuikCore.getCoreLogger().trace(e);
			return;
		}

		// Writeback, to keep file formatted.
		// If fails, ignore.
		if (writeback) {
			QuikSaver.saveElement(location, obj);
		}

		// Register the Element.
		registry.registerElement(obj);

	}

}
