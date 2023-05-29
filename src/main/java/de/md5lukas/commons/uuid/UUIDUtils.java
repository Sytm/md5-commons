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
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
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
    private final Executor executor;

    /**
     * Creates a new instance of UUIDUtils using the plugin for logging and scheduler creation
     *
     * @param plugin   The plugin to use
     * @param executor The executor to perform async operations on
     * @param settings The settings for the cache that should be used
     */
    public UUIDUtils(@NotNull Plugin plugin, @NotNull Executor executor, @NotNull UUIDCacheSettings settings) {
        this.plugin = plugin;
        this.executor = executor;

        uuidCache = CacheBuilder.newBuilder().maximumSize(settings.getMaxSize())
                .expireAfterWrite(settings.getExpireAfterWrite(), settings.getExpireAfterWriteTimeUnit()).build(new CacheLoader<>() {
                    @Override
                    public @NotNull UUID load(String name) throws Exception {
                        checkArgument(name.length() >= 3, "The name provided for uuid look-up is too short");
                        name = name.toLowerCase();
                        Player player = Bukkit.getPlayerExact(name);
                        if (player != null) {
                            return player.getUniqueId();
                        }
                        HttpURLConnection connection = (HttpURLConnection) new URL(String.format(UUID_URL, name)).openConnection();
                        connection.setReadTimeout(500);

                        JsonObject json = JsonParser
                                .parseReader(new InputStreamReader(connection.getInputStream()))
                                .getAsJsonObject();

                        return uuidFromTrimmed(json.get("id").getAsString());
                    }
                });
        nameCache = CacheBuilder.newBuilder().maximumSize(settings.getMaxSize())
                .expireAfterWrite(settings.getExpireAfterWrite(), settings.getExpireAfterWriteTimeUnit()).build(new CacheLoader<>() {
                    @Override
                    public @NotNull String load(UUID uuid) throws Exception {
                        Player player = Bukkit.getPlayer(checkNotNull(uuid, "The uuid to look up cannot be null!"));
                        if (player != null) {
                            return player.getName();
                        }

                        HttpURLConnection connection = (HttpURLConnection) new URL(
                                String.format(NAME_URL, trimUUID(uuid))).openConnection();
                        connection.setReadTimeout(500);

                        JsonArray jsonArray = JsonParser
                                .parseReader(new InputStreamReader(connection.getInputStream()))
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
    public UUIDUtils(@NotNull Plugin plugin) {
        this(plugin, block -> Bukkit.getScheduler().runTaskAsynchronously(plugin, block), new UUIDCacheSettings());
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
    public @NotNull Optional<@NotNull UUID> getUUID(@NotNull String name) {
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
    public @NotNull Optional<@NotNull String> getName(@NotNull UUID uuid) {
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
     * Looks up a players uuid in an async thread.
     *
     * @param name The name of the player to look up
     * @throws NullPointerException If the name is null
     * @see #getUUID(String)
     */
    public @NotNull CompletableFuture<@NotNull Optional<@NotNull UUID>> getUUIDAsync(@NotNull String name) {
        checkNotNull(name, "The name cannot be null");
        return CompletableFuture.supplyAsync(() -> getUUID(name), executor);
    }

    /**
     * Looks up a players name in an async thread
     *
     * @param uuid The uuid of the player to look up
     * @throws NullPointerException If the uuid is null
     * @see #getName(UUID)
     */
    public @NotNull CompletableFuture<@NotNull Optional<@NotNull String>> getNameAsync(@NotNull UUID uuid) {
        checkNotNull(uuid, "The uuid cannot be null");
        return CompletableFuture.supplyAsync(() -> getName(uuid), executor);
    }

    /**
     * Test a string if it matches the format of a uuid
     *
     * @param input The string to test
     * @return <code>true</code> if the string is a uuid
     */
    public static boolean isUUID(@NotNull String input) {
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