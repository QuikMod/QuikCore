/*
 */
package com.github.quikmod.quikcore.json;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.TypeHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 *
 * @author RlonRyan
 */
public class QuikJsonSaver {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static void saveElements(Path root, QuikJsonElement... objects) {
		saveElements(root, TypeHelper.asList(objects));
	}

	public static void saveElements(Path root, List<? extends QuikJsonElement> objects) {
        //AgriCore.getCoreLogger().debug("Saving AgriSerializables To: {0}!", location);
		objects.forEach(obj -> saveElement(root.resolve(obj.getId() + ".json"), obj));
        //AgriCore.getCoreLogger().debug("Finished Saving AgriSerializables To: {0}!", location);
	}

	public static void saveElement(Path location, QuikJsonElement obj) {
		// Determine if need to autoname file.
		try {
			Files.createDirectories(location.getParent());
		} catch (IOException e) {
			QuikCore.getCoreLogger().warn("Unable to create directories for element: \"{0}\"!", location);
			return;
		}
		try (Writer writer = Files.newBufferedWriter(location, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			gson.toJson(obj, writer);
			writer.append("\n");
		} catch (IOException e) {
			QuikCore.getCoreLogger().warn("Unable to save element: \"{0}\"!", location);
		}
	}

}
