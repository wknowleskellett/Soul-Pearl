package dev.williamknowleskellett.soul_pearl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;

@Mixin(Entity.class)
public class EntityMixin {
    //ActionResult interactWithItem(PlayerEntity player, Hand hand)
	@Inject(at = @At("HEAD"),
        method = "requestTeleportAndDismount(DDD)V")
    private void dismountBeforeTeleporting(double destX, double destY, double destZ, CallbackInfo info) {
        if ((Object) this instanceof MobEntity) {
            ((MobEntity)(Object) this).dismountVehicle();
        }
    }
}
