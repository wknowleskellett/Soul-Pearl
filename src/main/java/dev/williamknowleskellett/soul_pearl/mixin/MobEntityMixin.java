package dev.williamknowleskellett.soul_pearl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import dev.williamknowleskellett.soul_pearl.EssenceItem;
import dev.williamknowleskellett.soul_pearl.SoulPearl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    //ActionResult interactWithItem(PlayerEntity player, Hand hand)
	@Inject(at = @At("HEAD"),
        method = "interactWithItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD)
	private void init(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(SoulPearl.SOUL_PEARL_ITEM)) {
            ItemStack essence = EssenceItem.cage(itemStack, (MobEntity)(Object) this);
            ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, player, essence);
            player.setStackInHand(hand, itemStack2);
            info.setReturnValue(ActionResult.success(((MobEntity)(Object)this).world.isClient));
        }
	}
}
