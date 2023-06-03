package dev.williamknowleskellett.together_pearl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CagedPearlItem extends Item {

    public CagedPearlItem(Settings settings) {
        super(settings);
    }

    // public static boolean isCaged(ItemStack stack) {
    //     NbtCompound nbt = stack.getNbt();
    //     return nbt != null && nbt.containsUuid(CAGED_UUID_KEY);
    // }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.isSneaking()) return TypedActionResult.pass(itemStack);
        ItemStack essence = EssenceItem.cage(itemStack, user);
        ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, user, essence);
        user.setStackInHand(hand, itemStack2);
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
