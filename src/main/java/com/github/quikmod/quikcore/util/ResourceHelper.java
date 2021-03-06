/*
 */
package com.github.quikmod.quikcore.util;

import com.github.quikmod.quikcore.core.QuikCore;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.function.Predicate;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

/**
 *
 * @author RlonRyan
 */
public class ResourceHelper {

	private static final Reflections REFLECTIONS = new Reflections(null, new ResourcesScanner());

	public static Set<String> findResources(Predicate<String> nameFilter) {
		return REFLECTIONS.getResources(nameFilter::test);
	}

	/**
	 * Copies a file from inside the jar to the specified location outside the
	 * jar, retaining the file name. The default copy action is to not overwrite
	 * an existing file.
	 *
	 * @param from the location of the internal resource.
	 * @param to the location to copy the resource to.
	 * @param overwrite if the copy task should overwrite existing files.
	 */
	public static void copyResource(String from, Path to, boolean overwrite) {
		try {
			if (overwrite || !Files.exists(to)) {
				Files.createDirectories(to.getParent());
				Files.copy(getResourceAsStream(from), to, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			QuikCore.getCoreLogger().error(
					"Unable to copy Jar resource: \"{0}\" to: \"{1}\"!",
					from,
					to
			);
			QuikCore.getCoreLogger().trace(e);
		}
	}

	/**
	 * Retrieves the requested resource by using the current thread's class
	 * loader or the AgriCore class loader.
	 *
	 * @param location the location of the desired resource stream.
	 * @return the resource, as a stream.
	 */
	public static InputStream getResourceAsStream(String location) {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
		return in != null ? in : ResourceHelper.class.getResourceAsStream(location);
	}

}
