package net.pitan76.advancedreborn.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.Items;
import net.pitan76.advancedreborn.api.Energy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.reborn.energy.api.base.SimpleBatteryItem;

@Mixin(LivingEntity.class)
public abstract class ARLivingEntityMixin extends Entity {
    public ARLivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"))
    public void injectFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        if (getWorld().isClient()) return;
        if (!((Object)this instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getEquippedStack(EquipmentSlot.FEET).getItem() == Items.NANO_SUIT_BOOTS.getOrNull()) {
            ItemStack stack = player.getEquippedStack(EquipmentSlot.FEET);
            if (!isSneaking()) {
                int vanillaPlayerDamage = this.computeFallDamage(fallDistance, damageMultiplier);
                int userDamage = vanillaPlayerDamage / 5;
                int bootDamage = (int) Math.round(vanillaPlayerDamage * 0.4375);
                if (bootDamage > 0) {
                    SimpleBatteryItem energy = Energy.of(stack);
                    if (energy.getStoredEnergy(stack) <= 800 * vanillaPlayerDamage) return;
                    energy.tryUseEnergy(stack, 800 * vanillaPlayerDamage);
                }
                if (userDamage > 0) {
                    this.damage(DamageSource.FALL, (float) userDamage);
                }
                info.cancel();
            }
        }
    }

    @Shadow
    protected abstract int computeFallDamage(float fallDistance, float damageMultiplier);
}
