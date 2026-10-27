package com.max1nity.ascension;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AscensionCore.MODID)
public class AscensionCore {
    public static final String MODID = "ascension_core";

    public AscensionCore() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modBus);
        ModItems.TABS.register(modBus);
    }
}
