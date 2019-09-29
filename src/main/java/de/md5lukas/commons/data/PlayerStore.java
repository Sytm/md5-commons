package de.md5lukas.commons.data;

import com.google.common.cache.*;
import com.google.common.util.concurrent.UncheckedExecutionException;
import de.md5lukas.commons.internal.CommonsMain;
import de.md5lukas.nbt.NbtIo;
import de.md5lukas.nbt.tags.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

import static com.google.common.base.Preconditions.checkNotNull;

public class PlayerStore {

	private static boolean saveAll = false;
	private static LoadingCache<UUID, PlayerStore> stores;

	static {
		stores = CacheBuilder.newBuilder().maximumSize(Bukkit.getMaxPlayers()).expireAfterAccess(30, TimeUnit.MINUTES)
			.removalListener((RemovalListener<UUID, PlayerStore>) notification -> {
				CompoundTag tag = notification.getValue().tag;
				notification.getValue().onSaveSync.forEach((pluginName, callbacks) -> {
					CompoundTag pluginTag = tag.getCompound(pluginName);
					callbacks.forEach(callback -> {
						try {
							callback.accept(pluginTag);
						} catch (Exception e) {
							CommonsMain.logger().log(Level.WARNING, "An exception occurred while executing a sync save hook for the plugin \"" + pluginName + "\" for the player" +
								" with the uuid " + notification.getValue().owner + ". This exception will be ignored and the save cycle will continue", e);
						}
					});
				});
				Runnable save = () -> {
					notification.getValue().onSaveAsync.forEach((pluginName, callbacks) -> {
						CompoundTag pluginTag = tag.getCompound(pluginName);
						callbacks.forEach(callback -> {
							try {
								callback.accept(pluginTag);
							} catch (Exception e) {
								CommonsMain.logger().log(Level.WARNING, "An exception occurred while executing a async save hook for the plugin \"" + pluginName + "\" for the " +
									"player with the uuid " + notification.getValue().owner + ". This exception will be ignored and the save cycle will continue", e);
							}
						});
					});
					try {
						NbtIo.writeCompressed(tag, getPlayerStoreFile(notification.getKey()));
					} catch (IOException e) {
						CommonsMain.logger().log(Level.SEVERE, "Couldn't save player store for the uuid " + notification.getKey(), e);
					}
				};
				if (saveAll)
					save.run();
				else
					Bukkit.getScheduler().runTaskAsynchronously(CommonsMain.instance(), save);
			}).build(new CacheLoader<UUID, PlayerStore>() {
				@Override
				public PlayerStore load(UUID uuid) throws Exception {
					File file = getPlayerStoreFile(uuid);
					return new PlayerStore(uuid, file.exists() ? NbtIo.readCompressed(file) : new CompoundTag());
				}
			});
	}

	public static PlayerStore getPlayerStore(UUID uuid) {
		try {
			return stores.get(checkNotNull(uuid, "The uuid cannot be null"));
		} catch (ExecutionException | UncheckedExecutionException e) {
			CommonsMain.logger().log(Level.SEVERE, "Couldn't load player store for player with the uuid " + uuid, e);
		}
		return null;
	}

	public static void saveAll() {
		saveAll = true;
		stores.invalidateAll();
		saveAll = false;
	}

	private UUID owner;
	private CompoundTag tag;
	private Map<String, List<Consumer<CompoundTag>>> onSaveSync, onSaveAsync;

	public PlayerStore(UUID owner, CompoundTag tag) {
		this.owner = owner;
		this.tag = tag;
		this.onSaveSync = new HashMap<>();
		this.onSaveAsync = new HashMap<>();
	}

	public UUID getOwner() {
		return owner;
	}

	public PlayerStore onSaveSync(Plugin plugin, Consumer<CompoundTag> callback) {
		checkNotNull(callback, "The callback cannot be null");
		onSaveSync.compute(checkNotNull(plugin, "The plugin to register a save hook with cannot be null").getName(), (k, v) -> {
			if (v == null) {
				v = new ArrayList<>();
			}
			v.add(callback);
			return v;
		});
		return this;
	}

	public PlayerStore onSaveAsync(Plugin plugin, Consumer<CompoundTag> callback) {
		checkNotNull(callback, "The callback cannot be null");
		onSaveAsync.compute(checkNotNull(plugin, "The plugin to register a save hook with cannot be null").getName(), (k, v) -> {
			if (v == null) {
				v = new ArrayList<>();
			}
			v.add(callback);
			return v;
		});
		return this;
	}

	public CompoundTag getTag(Plugin plugin) {
		return tag.getCompound(checkNotNull(plugin, "The plugin cannot be null").getName());
	}

	private static File getPlayerStoreFile(UUID uuid) {
		return new File(CommonsMain.instance().getDataFolder(), "playerdata/" + uuid + ".dat");
	}
}
