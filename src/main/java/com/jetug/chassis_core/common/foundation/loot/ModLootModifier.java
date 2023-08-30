package com.jetug.chassis_core.common.foundation.loot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jetug.chassis_core.common.data.constants.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.*;

public class ModLootModifier extends LootModifier {
    private static final Gson GSON = Deserializers.createFunctionSerializer().create();

    private static final Map<Integer, Float> chances = new HashMap<>();
    private static int id = 0;

    private static float buffChance;

    private final LootItemCondition[] conditions;
    private final LootItem[] entries;
    private final LootItemFunction[] functions;
    private final Random random = new Random();
    private final float chance;

    public ModLootModifier(LootItemCondition[] conditions, LootItem[] entries, LootItemFunction[] functions) {
        super(resetChance(conditions.clone()));
        this.conditions = conditions;
        this.chance = buffChance;
        this.entries = entries;
        this.functions = functions;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        for (var entry : entries) {
            var rand = random.nextFloat(0.0f, 1.0f);

            if(rand <= chance) {
                entry.expand(context, generator -> generator.createItemStack(LootItemFunction.decorate(
                        LootItemFunctions.compose(this.functions), generatedLoot::add, context), context));
            }
        }

        return generatedLoot;
    }

    private static LootItemCondition[] resetChance(LootItemCondition[] conditions)
    {
        for (var item : conditions){
            if(item instanceof LootItemRandomChanceCondition chanceCondition){
                try {
                    var probability = LootItemRandomChanceCondition.class.getDeclaredField("probability");
                    probability.setAccessible(true);
                    buffChance = probability.getFloat(chanceCondition);

                    probability.set(chanceCondition, 1);

                } catch (Exception ignored) {}
            }
        }
        return conditions;
    }

//    private float getChance(LootItemCondition[] conditions)
//    {
//        for (var item : conditions){
//            if(item instanceof LootItemRandomChanceCondition chanceCondition){
//                try {
//                    var probability = LootItemRandomChanceCondition.class.getDeclaredField("probability");
//                    probability.setAccessible(true);
//                    return probability.getFloat(chanceCondition);
//
//                } catch (Exception e) {
//                    return 1;
//                }
//            }
//        }
//        return 1;
//    }

    private static class Serializer extends GlobalLootModifierSerializer<ModLootModifier> {
        @Override
        public ModLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            var entries = GSON.fromJson(GsonHelper.getAsJsonArray(object, "entries"), LootItem[].class);
            var functions = object.has("functions") ? GSON.fromJson(GsonHelper.getAsJsonArray(
                    object, "functions"), LootItemFunction[].class) : new LootItemFunction[0];
            return new ModLootModifier(conditions, entries, functions);
        }

        @Override
        public JsonObject write(ModLootModifier instance) {
            JsonObject object = makeConditions(instance.conditions);

            object.add("entries", GSON.toJsonTree(instance.entries, LootItem[].class));
            object.add("functions", GSON.toJsonTree(instance.functions, LootItemFunction[].class));

            return object;
        }
    }

    @Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventHandler {
        @SubscribeEvent
        public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
            event.getRegistry().register(new Serializer()
                    .setRegistryName(new ResourceLocation(Global.MOD_ID, "loot_gen")));
        }
    }
}