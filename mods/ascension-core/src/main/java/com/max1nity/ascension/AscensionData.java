package com.max1nity.ascension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Stores Ascension progress in the player's persistent data and mirrors it into entity tags
 * so that KubeJS, quests and commands can read it, for example:
 * {@code ascension_tech_tier_2}, {@code ascension_balanced_1}.
 */
public final class AscensionData {
    public static final int POINTS_PER_TIER = 25;
    public static final int MAX_TIER = 10;
    public static final int MAX_POINTS = POINTS_PER_TIER * MAX_TIER;

    private static final String ROOT = "AscensionCore";
    private static final String KILLS_KEY = "wanderer_kills";

    private AscensionData() {
    }

    private static CompoundTag root(Player player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(Player.PERSISTED_NBT_TAG, Tag.TAG_COMPOUND)) {
            persistent.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }
        CompoundTag persisted = persistent.getCompound(Player.PERSISTED_NBT_TAG);
        if (!persisted.contains(ROOT, Tag.TAG_COMPOUND)) {
            persisted.put(ROOT, new CompoundTag());
        }
        return persisted.getCompound(ROOT);
    }

    public static int getPoints(Player player, AscensionPath path) {
        return root(player).getInt(path.getId());
    }

    public static int tierOf(int points) {
        return Math.min(MAX_TIER, points / POINTS_PER_TIER);
    }

    public static int getTier(Player player, AscensionPath path) {
        return tierOf(getPoints(player, path));
    }

    /** The lowest tier across all three paths: rewards balanced play. */
    public static int getBalancedTier(Player player) {
        int lowest = MAX_TIER;
        for (AscensionPath path : AscensionPath.values()) {
            lowest = Math.min(lowest, getTier(player, path));
        }
        return lowest;
    }

    public static void addPoints(ServerPlayer player, AscensionPath path, int amount) {
        if (amount <= 0) {
            return;
        }
        CompoundTag root = root(player);
        int before = root.getInt(path.getId());
        int after = Math.min(MAX_POINTS, before + amount);
        root.putInt(path.getId(), after);

        int tierBefore = tierOf(before);
        int tierAfter = tierOf(after);
        if (tierAfter > tierBefore) {
            syncTags(player);
            player.sendSystemMessage(Component.literal("Ascension: ")
                    .append(Component.translatable(path.getTranslationKey()))
                    .append(Component.literal(" reached tier " + tierAfter + "!")));
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    /** Counts a hostile kill. Every 10 kills grants one Wanderer point. */
    public static void recordKill(ServerPlayer player) {
        CompoundTag root = root(player);
        int kills = root.getInt(KILLS_KEY) + 1;
        root.putInt(KILLS_KEY, kills);
        if (kills % 10 == 0) {
            addPoints(player, AscensionPath.WANDERER, 1);
        }
    }

    /** Rewrites the player's tags so they always match their stored points. */
    public static void syncTags(ServerPlayer player) {
        for (AscensionPath path : AscensionPath.values()) {
            int tier = getTier(player, path);
            for (int t = 1; t <= MAX_TIER; t++) {
                String tag = "ascension_" + path.getId() + "_tier_" + t;
                if (t <= tier) {
                    player.addTag(tag);
                } else {
                    player.removeTag(tag);
                }
            }
        }
        int balanced = getBalancedTier(player);
        for (int t = 1; t <= MAX_TIER; t++) {
            String tag = "ascension_balanced_" + t;
            if (t <= balanced) {
                player.addTag(tag);
            } else {
                player.removeTag(tag);
            }
        }
    }

    /** Copies progress when a player entity is recreated (death, returning from the End). */
    public static void copy(Player from, Player to) {
        CompoundTag source = root(from).copy();
        root(to).merge(source);
    }
}
