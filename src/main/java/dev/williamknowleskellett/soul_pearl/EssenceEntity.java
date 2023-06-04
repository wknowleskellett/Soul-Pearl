package dev.williamknowleskellett.soul_pearl;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EssenceEntity extends ThrownItemEntity {
    public EssenceEntity(EntityType<? extends EssenceEntity> entityType, World world) {
        super(entityType, world);
    }

    public EssenceEntity(World world, LivingEntity owner) {
        // super((EntityType<? extends ThrownItemEntity>)TogetherPearl., owner, world);
        super(SoulPearl.ESSENCE_ENTITY_TYPE, owner, world);
    }

    public EssenceEntity(World world, double x, double y, double z) {
        super(SoulPearl.ESSENCE_ENTITY_TYPE, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return SoulPearl.ESSENCE_ITEM;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        entityHitResult.getEntity().damage(this.getDamageSources().thrown(this, this.getOwner()), 0.0f);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        for (int i = 0; i < 32; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0, this.getZ(), this.random.nextGaussian(), 0.0, this.random.nextGaussian());
        }
        ItemStack stack = this.getItem();
        Entity entity = EssenceItem.getSoulEntity(this.world, stack);
        if (this.world.isClient || this.isRemoved() || entity == null) return;
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
            if (!serverPlayerEntity.networkHandler.isConnectionOpen() || serverPlayerEntity.world != this.world || serverPlayerEntity.isSleeping()) return;
        }
        EndermiteEntity endermiteEntity;
        if (this.random.nextFloat() < 0.05f && this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && (endermiteEntity = EntityType.ENDERMITE.create(this.world)) != null) {
            endermiteEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
            this.world.spawnEntity(endermiteEntity);
        }
        if (entity.hasVehicle()) {
            entity.requestTeleportAndDismount(this.getX(), this.getY(), this.getZ());
        } else {
            entity.requestTeleport(this.getX(), this.getY(), this.getZ());
        }
        entity.onLanding();
        Entity owner = this.getOwner();
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), 0.0f);
        if (owner != null) {
            this.getOwner().damage(this.getDamageSources().fall(), 5.0f);
        }
        this.discard();
    }

    @Override
    @Nullable
    public Entity moveToWorld(ServerWorld destination) {
        Entity entity = this.getOwner();
        if (entity != null && entity.world.getRegistryKey() != destination.getRegistryKey()) {
            this.setOwner(null);
        }
        return super.moveToWorld(destination);
    }
    
}
