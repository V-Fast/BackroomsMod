package org.vfast.backrooms.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.vfast.backrooms.BackroomsMod;

import java.util.function.Function;

public class BackroomsItems {
    public static final Item SILK = registerItem("silk", new Item.Properties());
    public static final Item DECAYING_BRICK = registerItem("decaying_brick", new Item.Properties());

    public static final Item CAMCORDER = registerItem("camcorder", CamcorderItem::new, new Item.Properties().stacksTo(1));

    // Can be used for new 1.19.3+ creative inventory system

    private static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemConstructor, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name));
        T item = itemConstructor.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name), item);
        return item;
    }

    private static Item registerItem(String name, Item.Properties properties) {
        return registerItem(name, Item::new, properties);
    }

    public static void registerItems() {
        BackroomsMod.LOGGER.info("[BackroomsMod] Items initialized");
    }
}
