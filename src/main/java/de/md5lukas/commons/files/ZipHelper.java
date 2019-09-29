package de.md5lukas.commons.files;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

	public static void toZipFile(File src, File dest) throws IOException {
		FileOutputStream fos = new FileOutputStream(dest);
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

		Path root = src.toPath();

		try {
			for (File f : Objects.requireNonNull(src.listFiles())) {
				addFile(root, f, zos);
			}
		} catch (NullPointerException e) {
			zos.flush();
			zos.close();
			dest.delete();
		}

		zos.flush();
		zos.close();
	}

	public static List<String> readZipFilePaths(File zip) throws IOException {
		FileInputStream fis = new FileInputStream(zip);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		List<String> paths = new ArrayList<String>();

		ZipEntry zen;
		while ((zen = zis.getNextEntry()) != null) {
			paths.add(zen.getName());
			zis.closeEntry();
		}

		zis.close();
		return paths;
	}

	private static void addFile(Path root, File file, ZipOutputStream zos) throws IOException, NullPointerException {
		if (file.isDirectory()) {
			for (File child : Objects.requireNonNull(file.listFiles())) {
				addFile(root, child, zos);
			}
		} else {
			String relPath = root.relativize(file.toPath()).toString();
			zos.putNextEntry(new ZipEntry(relPath));
			try (FileInputStream fis = new FileInputStream(file)) {
				fis.getChannel().transferTo(0, Long.MAX_VALUE, Channels.newChannel(zos));
			}
			zos.closeEntry();
		}
	}
}