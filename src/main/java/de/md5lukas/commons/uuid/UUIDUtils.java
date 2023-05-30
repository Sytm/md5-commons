package de.md5lukas.commons.uuid;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A helper class providing access to Mojang APIs to access player name and uuid conversion, even if the player itself is not online on the server
 */
public final class UUIDUtils {

    private static final Pattern UUID_PATTERN = Pattern.compile("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
    private final Executor executor;

    /**
     * Creates a new instance of UUIDUtils using the executor to perform the blocking network tasks
     *
     * @param executor The executor to perform async operations on
     */
    public UUIDUtils(@NotNull Executor executor) {
        this.executor = executor;
    }

    /**
     * Looks up a players uuid in an async thread.
     *
     * @param name The name of the player to look up
     * @throws NullPointerException If the name is null
     */
    public @NotNull CompletableFuture<@NotNull Optional<@NotNull UUID>> getUUIDAsync(@NotNull String name) {
        checkNotNull(name, "The name cannot be null");
        return CompletableFuture.supplyAsync(() -> {
            PlayerProfile profile = Bukkit.createProfile(name);

            if (!profile.isComplete()) {
                profile.complete(false);
            }

            return Optional.ofNullable(profile.getId());
        }, executor);
    }

    /**
     * Looks up a players name in an async thread
     *
     * @param uuid The uuid of the player to look up
     * @throws NullPointerException If the uuid is null
     */
    public @NotNull CompletableFuture<@NotNull Optional<@NotNull String>> getNameAsync(@NotNull UUID uuid) {
        checkNotNull(uuid, "The uuid cannot be null");
        return CompletableFuture.supplyAsync(() -> {
            PlayerProfile profile = Bukkit.createProfile(uuid);

            if (!profile.isComplete()) {
                profile.complete(false);
            }

            return Optional.ofNullable(profile.getName());
        }, executor);
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
}