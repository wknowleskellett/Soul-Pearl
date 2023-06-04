package dev.williamknowleskellett.soul_pearl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.Category;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoulPearl implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("soul_pearl");
	public static final Item SOUL_PEARL_ITEM = new SoulPearlItem(new Item.Settings().maxCount(16));
	public static final Identifier SOUL_PEARL_ID = new Identifier("soul_pearl", "soul_pearl");
	public static final Item ESSENCE_ITEM = new EssenceItem(new Item.Settings().maxCount(1));
	public static final Identifier ESSENCE_ID = new Identifier("soul_pearl", "essence");

	// Register the item entity
	public static final EntityType<EssenceEntity> ESSENCE_ENTITY_TYPE = Registry.register(
		Registries.ENTITY_TYPE, 
		ESSENCE_ID, 
		Builder.create(new EntityFactory<EssenceEntity>() {
			@Override
			public EssenceEntity create(EntityType<EssenceEntity> entityType, World world) {
				return new EssenceEntity(entityType, world);
			}
		}, SpawnGroup.MISC).setDimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).build(ESSENCE_ID.toString())
	);

	// Create and register a boolean game rule with the name "doPlayerEssence" which is false by default.
	public static final GameRules.Key<GameRules.BooleanRule> DO_PLAYER_ESSENCE =
	GameRuleRegistry.register("doPlayerEssence", Category.MOBS, GameRuleFactory.createBooleanRule(false));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Register the items
		Registry.register(Registries.ITEM, SOUL_PEARL_ID, SOUL_PEARL_ITEM);
		// ModelPredicateProviderRegistry.register(SOUL_PEARL_ITEM, new Identifier("caged"), (stack, world, entity, seed) -> SoulPearlItem.isSoul((ItemStack)stack) ? 1.0f : 0.0f);
		Registry.register(Registries.ITEM, ESSENCE_ID, ESSENCE_ITEM);
		
        DispenserBlock.registerBehavior(ESSENCE_ITEM, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new EssenceEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }
        });
        DispenserBlock.registerBehavior(SOUL_PEARL_ITEM, new FallibleItemDispenserBehavior(){
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                List<LivingEntity> list = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos), entity -> {
                    return entity instanceof MobEntity || entity instanceof PlayerEntity;
                });
				if (list.isEmpty()) {
					return super.dispenseSilently(pointer, stack);
				}

				ItemStack essence = EssenceItem.cage(stack, (LivingEntity) list.get(0));
				stack.decrement(1);
				if (stack.isEmpty()) {
					return essence;
				}
				if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(essence) < 0) {
					this.fallbackBehavior.dispense(pointer, essence);
				}
				return stack;
            }
        });
	}
}