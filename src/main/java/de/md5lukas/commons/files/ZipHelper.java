/*
 *     A collection of classes and methods designed for use in spigot plugins
 *     Copyright (C) 2020 Lukas Planz
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.md5lukas.commons.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A  class containing helpers for zipping folders
 */
@SuppressWarnings("ConstantConditions")
public final class ZipHelper {

    /**
     * Creates a zip file based on the source folder.
     * <br><br>
     * If the parent folder of the destination does not exist, it will be created
     *
     * @param sourceFolder    The folder which contents should be zipped
     * @param destinationFile The file where the zip file should be stored at
     * @throws NullPointerException  If either the sourceFolder or destinationFile are null
     * @throws IllegalStateException If the sourceFolder is not a folder or does not exit or the destinationFile already exists
     * @throws IOException           If an IO error occurred
     */
    public static void toZipFile(File sourceFolder, File destinationFile) throws IOException {
        checkNotNull(sourceFolder, "The source folder cannot be null");
        checkNotNull(destinationFile, "The destination file cannot be null");
        checkState(sourceFolder.isDirectory(), "The sourceFolder %s does not exist or isn't a folder", sourceFolder.getAbsolutePath());
        checkState(!destinationFile.exists(), "The destination file %s already exists", destinationFile.getAbsolutePath());
        destinationFile.mkdirs();

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destinationFile))) {
            Path root = sourceFolder.toPath();

            for (File f : sourceFolder.listFiles()) {
                addFile(root, f, zos);
            }

            zos.flush();
        }

    }

    /**
     * Lists the zip entries with their relative path
     *
     * @param zipFile The zip file
     * @return A list containing all paths in this zip file
     * @throws IOException           If an IO error occurred
     * @throws NullPointerException  If the zipFile is null
     * @throws IllegalStateException If the zipFile is not a file
     */
    public static List<String> readZipFilePaths(File zipFile) throws IOException {
        checkNotNull(zipFile, "The zip file cannot be null");
        checkState(zipFile.isFile(), "The zip file %s must be an actual file", zipFile.getAbsolutePath());
        List<String> paths = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zen;

            while ((zen = zis.getNextEntry()) != null) {
                paths.add(zen.getName());
                zis.closeEntry();
            }
        }

        return paths;
    }

    private static void addFile(Path root, File file, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
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