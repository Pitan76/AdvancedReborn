package net.pitan76.advancedreborn.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.mcpitanlib.api.item.ArmorEquipmentType;
import net.pitan76.mcpitanlib.api.item.CompatibleArmorMaterial;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.IdentifierUtil;
import reborncore.api.events.ApplyArmorToDamageCallback;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.items.armor.TREnergyArmourItem;


public class NanoSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker {

    private static final EntityAttributeModifier ENABLED_ARMOR_MODIFIER = new EntityAttributeModifier(IdentifierUtil.id(AdvancedReborn.MOD_ID, "nano_armor") , 6, EntityAttributeModifier.Operation.ADD_VALUE);

    private static final EntityAttributeModifier DISABLED_ARMOR_MODIFIER = new EntityAttributeModifier(IdentifierUtil.id(AdvancedReborn.MOD_ID, "nano_armor"), -1, EntityAttributeModifier.Operation.ADD_VALUE);


    public NanoSuitItem(CompatibleArmorMaterial material, ArmorEquipmentType slot, CompatibleItemSettings settings) {
        super(material.build(), slot.getType(), 1_000_000, RcEnergyTier.EXTREME);//, settings.build());

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
    public long getEnergyCapacity(ItemStack itemStack) {
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
    public void tickArmor(ItemStack stack, PlayerEntity player) {
        /*
        if (stack.getItem().equals(Items.NANO_SUIT_HELMET)) {
            if (getStoredEnergy(stack) > 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300, 3, false, false));
            }
        }

         */

        final EquipmentSlot slotType = this.getSlotType();
        AttributeModifiersComponent attributes = stack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);

        attributes = attributes.with(EntityAttributes.GENERIC_ARMOR, getStoredEnergy(stack) > 0 ? ENABLED_ARMOR_MODIFIER : DISABLED_ARMOR_MODIFIER, AttributeModifierSlot.forEquipmentSlot(slotType));
        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes);
    }
}
