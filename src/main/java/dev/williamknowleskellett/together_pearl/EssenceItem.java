package dev.williamknowleskellett.together_pearl;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EssenceItem extends Item {
    private static final String CAGED_UUID_KEY = "CagedUuid";
    private static final String CAGED_NAME_KEY = "CagedName";

    public EssenceItem(Settings settings) {
        super(settings);
    }

    public static ItemStack cage(ItemStack stack, @Nullable LivingEntity entity) {
        ItemStack essence = new ItemStack(TogetherPearl.ESSENCE_ITEM);
        if (entity != null) {
            NbtCompound nbt = essence.getOrCreateNbt();
            nbt.putUuid(CAGED_UUID_KEY, entity.getUuid());
            nbt.putString(CAGED_NAME_KEY, Text.Serializer.toJson(entity.getDisplayName()));
        }
        return essence;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            EssenceEntity essenceEntity = new EssenceEntity(world, user);
            essenceEntity.setItem(itemStack);
            essenceEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(essenceEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Nullable
    public static Entity getCagedEntity(World world, ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.containsUuid(CAGED_UUID_KEY) && world instanceof ServerWorld) {
            return ((ServerWorld)world).getEntity(nbt.getUuid(CAGED_UUID_KEY));
        }
        return null;
    }

    public Text getName(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains(CAGED_NAME_KEY, NbtElement.STRING_TYPE)) {
            return Text.translatable("item.together_pearl.essence.filled",
                Text.Serializer.fromJson(nbt.getString(CAGED_NAME_KEY)).getString());
        }
        return Text.translatable("item.together_pearl.essence.empty");
    }
    
}
