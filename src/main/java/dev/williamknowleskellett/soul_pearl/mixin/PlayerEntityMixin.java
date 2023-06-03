package dev.williamknowleskellett.soul_pearl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import dev.williamknowleskellett.soul_pearl.EssenceItem;
import dev.williamknowleskellett.soul_pearl.SoulPearl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(at = @At(value="INVOKE", target="Lnet/minecraft/entity/Entity;interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"),
        method = "interact(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD)
	private void tryCaptureSoul(Entity you, Hand hand, CallbackInfoReturnable<ActionResult> info, ItemStack itemStack, ItemStack itemStack2) {
                PlayerEntity me = (PlayerEntity)(Object)this;
                if (!(you instanceof PlayerEntity)) return;
                
                if (itemStack.isOf(SoulPearl.SOUL_PEARL_ITEM)) {
                        if (!me.getAbilities().creativeMode) {
                                info.setReturnValue(ActionResult.FAIL);
                                return;
                        }

                        ItemStack essence = EssenceItem.cage(itemStack, (LivingEntity)(Object) you);
                        ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, me, essence);
                        me.setStackInHand(hand, itemStack3);
                        info.setReturnValue(ActionResult.success(((LivingEntity)(Object)this).world.isClient));
                }
	}
}
