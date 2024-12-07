package net.pitan76.advancedreborn.items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.pitan76.advancedreborn.Items;
import net.pitan76.mcpitanlib.api.item.ArmorEquipmentType;
import net.pitan76.mcpitanlib.api.item.CompatibleArmorMaterial;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import reborncore.api.events.ApplyArmorToDamageCallback;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.items.armor.TRArmourItem;

public class NanoSuitItem extends TRArmourItem implements ArmorBlockEntityTicker, RcEnergyItem {
    public NanoSuitItem(CompatibleArmorMaterial material, ArmorEquipmentType slot, CompatibleItemSettings settings) {
        super(material.build(), slot.getSlot(), settings.build());

        ApplyArmorToDamageCallback.EVENT.register(((player, source, amount) -> {
            for (ItemStack stack : player.getArmorItems()) {
                if (!(stack.getItem() instanceof NanoSuitItem)) {
                    continue;
                }
                long stackEnergy = getStoredEnergy(stack);
                if (stackEnergy <= 0) {
                    continue;
                }
                //System.out.println(amount);
                float damageToAbsorb = Math.min(stackEnergy, amount * 2500);
                tryUseEnergy(stack, (long) damageToAbsorb);
                return damageToAbsorb / 2500;
            }
            return amount;
        }));
    }

    @Override
    public long getEnergyCapacity() {
        return 1_000_000;
    }

    @Override
    public RcEnergyTier getTier() {
        return RcEnergyTier.EXTREME;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return ItemUtils.getPowerForDurabilityBar(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ItemUtils.getColorForDurabilityBar(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot equipmentSlot) {
        ArrayListMultimap<EntityAttribute, EntityAttributeModifier> attributes = ArrayListMultimap.create(super.getAttributeModifiers(stack, getSlotType()));

        if (equipmentSlot == this.getSlotType() && getStoredEnergy(stack) > 0) {
            attributes.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(MODIFIERS[getSlotType().getEntitySlotId()], "Armor modifier", 6, EntityAttributeModifier.Operation.ADDITION));
        } else if (equipmentSlot == this.getSlotType()) {
            attributes.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(MODIFIERS[getSlotType().getEntitySlotId()], "Armor modifier", -1, EntityAttributeModifier.Operation.ADDITION));
        }

        return ImmutableMultimap.copyOf(attributes);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return HashMultimap.create();
    }

    @Override
    public void tickArmor(ItemStack stack, PlayerEntity player) {
        if (stack.getItem().equals(Items.NANO_SUIT_HELMET)) {
            if (getStoredEnergy(stack) > 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300, 3));
            }
        }
    }
}
