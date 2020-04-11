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

package de.md5lukas.commons.uuid;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A helper class providing access to Mojang APIs to access player name and uuid conversion, even if the player itself is not online on the server
 */
public final class UUIDUtils {

    private static final Pattern UUID_PATTERN = Pattern.compile("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";

    private final LoadingCache<String, UUID> uuidCache;
    private final LoadingCache<UUID, String> nameCache;
    private final Plugin plugin;

    /**
     * Creates a new instance of UUIDUtils using the plugin for logging and scheduler creation
     *
     * @param plugin   The plugin to use
     * @param settings The settings for the cache that should be used
     */
    public UUIDUtils(Plugin plugin, UUIDCacheSettings settings) {
        this.plugin = plugin;

        uuidCache = CacheBuilder.newBuilder().maximumSize(settings.getMaxSize())
                .expireAfterWrite(settings.getExpireAfterWrite(), settings.getExpireAfterWriteTimeUnit()).build(new CacheLoader<String, UUID>() {
                    @Override
                    public UUID load(String name) throws Exception {
                        checkArgument(name.length() >= 3, "The name provided for uuid look-up is too short");
                        name = name.toLowerCase();
                        Player player = Bukkit.getPlayerExact(name);
                        if (player != null) {
                            return player.getUniqueId();
                        }
                        HttpURLConnection connection = (HttpURLConnection) new URL(String.format(UUID_URL, name)).openConnection();
                        connection.setReadTimeout(500);
                        JsonObject json = new JsonParser()
                                .parse(new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine())
                                .getAsJsonObject();
                        return uuidFromTrimmed(json.get("id").getAsString());
                    }
                });
        nameCache = CacheBuilder.newBuilder().maximumSize(settings.getMaxSize())
                .expireAfterWrite(settings.getExpireAfterWrite(), settings.getExpireAfterWriteTimeUnit()).build(new CacheLoader<UUID, String>() {
                    @Override
                    public String load(UUID uuid) throws Exception {
                        Player player = Bukkit.getPlayer(checkNotNull(uuid, "The uuid to look up cannot be null!"));
                        if (player != null) {
                            return player.getName();
                        }
                        HttpURLConnection connection = (HttpURLConnection) new URL(
                                String.format(NAME_URL, trimUUID(uuid))).openConnection();
                        connection.setReadTimeout(500);

                        JsonArray jsonArray = new JsonParser()
                                .parse(new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine())
                                .getAsJsonArray();
                        JsonObject json = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();
                        String name = json.get("name").getAsString();
                        uuidCache.put(name.toLowerCase(), uuid);
                        return name;
                    }
                });
    }

    /**
     * Creates a new instance of UUIDUtils using the plugin for logging and scheduler creation and using the default settings.
     * <br>
     * The default values are shown here: {@link UUIDCacheSettings#UUIDCacheSettings()}
     *
     * @param plugin The plugin to use
     */
    public UUIDUtils(Plugin plugin) {
        this(plugin, new UUIDCacheSettings());
    }

    /**
     * Looks up a players uuid using the Mojang API.
     * <br>
     * If a player could be found, the returned optional contains a uuid, otherwise it will be empty.
     *
     * @param name The name of the player to look up
     * @return An optional containing a uuid if the player could be found
     * @throws NullPointerException If the name is null
     */
    public Optional<UUID> getUUID(String name) {
        try {
            return Optional.ofNullable(uuidCache.get(checkNotNull(name, "The name cannot be null").toLowerCase()));
        } catch (ExecutionException ee) {
            if (ee.getCause() instanceof IOException) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while trying to retrieve the uuid of the player '" + name + "'", ee);
            }
        }
        return Optional.empty();
    }

    /**
     * Looks up a players name using the Mojang API
     * <br>
     * If a player could be found with that name, the returned optional contains the players uuid, otherwise it is empty
     *
     * @param uuid The uuid of the player to look up
     * @return An optional containing the name if the player could be found
     * @throws NullPointerException If the uuid is null
     */
    public Optional<String> getName(UUID uuid) {
        try {
            return Optional.ofNullable(nameCache.get(checkNotNull(uuid, "The uuid cannot be null")));
        } catch (ExecutionException ee) {
            if (ee.getCause() instanceof IOException) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while trying to retrieve the name of the player with the uuid of " + uuid, ee);
            }
        }
        return Optional.empty();
    }

    /**
     * Looks up a players uuid in an async thread and then will run the consumer in the main thread again
     *
     * @param name     The name of the player to look up
     * @param callback The callback that will be called once the result is in
     * @throws NullPointerException If the name or callback is null
     * @see #getUUID(String)
     */
    public void getUUID(String name, Consumer<Optional<UUID>> callback) {
        checkNotNull(name, "The name cannot be null");
        checkNotNull(callback, "The callback cannot be null");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Optional<UUID> uuid = getUUID(name);
            Bukkit.getScheduler().runTask(plugin, () -> {
                callback.accept(uuid);
            });
        });
    }

    /**
     * Looks up a players name in an async thread and then will run the consumer in the main thread again
     *
     * @param uuid     The uuid of the player to look up
     * @param callback The callback that will be called once the result is in
     * @throws NullPointerException If the uuid or callback is null
     * @see #getName(UUID)
     */
    public void getName(UUID uuid, Consumer<String> callback) {
        checkNotNull(uuid, "The uuid cannot be null");
        checkNotNull(callback, "The callback cannot be null");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String name = nameCache.get(uuid);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    callback.accept(name);
                });
                return;
            } catch (ExecutionException ee) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while trying to retrieve the name of the player with the uuid of " + uuid, ee);
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                callback.accept(null);
            });
        });
    }

    /**
     * Test a string if it matches the format of a uuid
     *
     * @param input The string to test
     * @return <code>true</code> if the string is a uuid
     */
    public static boolean isUUID(String input) {
        return UUID_PATTERN.matcher(checkNotNull(input, "The input to check can't be null")).matches();
    }

    private String trimUUID(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    private UUID uuidFromTrimmed(String input) {
        StringBuilder builder = new StringBuilder(input);
        builder.insert(8, '-').insert(13, '-').insert(18, '-').insert(23, '-');
        return UUID.fromString(builder.toString());
    }
}