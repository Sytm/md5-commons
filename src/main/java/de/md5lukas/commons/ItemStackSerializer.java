package de.md5lukas.commons;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class to help with converting ItemStacks to strings and back
 */
public final class ItemStackSerializer {

    /**
     * Serializes a single item stack into a Base64 string
     *
     * @param itemStack The item stack to serialize
     * @return The item stack as a Base64 string or <code>null</code> if an error occurred
     */
    public static String serializeSingle(ItemStack itemStack) {
        checkNotNull(itemStack, "The item stack to serialize cannot be null");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos)) {
            boos.writeObject(itemStack);
            boos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Serializes a all item stacks into a Base64 string
     *
     * @param itemStacks The item stacks to serialize
     * @return The item stacks as a Base64 string or <code>null</code> if an error occurred
     */
    public static String serializeAll(List<ItemStack> itemStacks) {
        checkNotNull(itemStacks, "The item stack list to serialize cannot be null");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos)) {
            boos.writeInt(itemStacks.size());
            for (ItemStack itemStack : itemStacks) {
                boos.writeObject(itemStack);
            }
            boos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Deserializes a Base64 string expecting a single item stack
     *
     * @param string The Base64 string to deserialize
     * @return The deserialized item stack or <code>null</code> if an error occurred
     */
    public static ItemStack deserializeSingle(String string) {
        checkNotNull(string, "The string to deserialize cannot be null");
        byte[] bytes = Base64.getDecoder().decode(string);
        try (BukkitObjectInputStream bois = new BukkitObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (ItemStack) bois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Deserializes a Base64 string expecting a list of item stacks
     *
     * @param string The Base64 string to deserialize
     * @return The deserialized item stacks or <code>null</code> if an error occurred
     */
    public static List<ItemStack> deserializeAll(String string) {
        checkNotNull(string, "The string to deserialize cannot be null");
        byte[] bytes = Base64.getDecoder().decode(string);
        try (BukkitObjectInputStream bois = new BukkitObjectInputStream(new ByteArrayInputStream(bytes))) {
            int size = bois.readInt();
            List<ItemStack> result = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                result.add((ItemStack) bois.readObject());
            }
            return result;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
