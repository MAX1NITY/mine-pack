package com.max1nity.ascension;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AscensionCore.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AscensionCore.MODID);

    public static final RegistryObject<Item> TECH_ESSENCE = ITEMS.register("tech_essence",
            () -> new EssenceItem(AscensionPath.TECH, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ARCANE_ESSENCE = ITEMS.register("arcane_essence",
            () -> new EssenceItem(AscensionPath.ARCANE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WANDERER_ESSENCE = ITEMS.register("wanderer_essence",
            () -> new EssenceItem(AscensionPath.WANDERER, new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + AscensionCore.MODID))
                    .icon(() -> new ItemStack(ARCANE_ESSENCE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(TECH_ESSENCE.get());
                        output.accept(ARCANE_ESSENCE.get());
                        output.accept(WANDERER_ESSENCE.get());
                    })
                    .build());

    private ModItems() {
    }
}
