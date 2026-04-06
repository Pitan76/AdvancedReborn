package net.pitan76.advancedreborn.armormaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.pitan76.mcpitanlib.api.item.ArmorEquipmentType;
import net.pitan76.mcpitanlib.api.item.CompatibleArmorMaterial;
import net.pitan76.mcpitanlib.api.sound.CompatSoundEvents;
import net.pitan76.mcpitanlib.api.util.IdentifierUtil;

public class BBArmorMaterial implements CompatibleArmorMaterial {

    public String name = "";

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

    public BBArmorMaterial(String name) {
        this.name = name;
    }

    @Override
    public int getDurability(ArmorEquipmentType type) {
        if (type.equals(ArmorEquipmentType.HEAD)) {
            return BASE_DURABILITY[0] * 15;
        } else if (type.equals(ArmorEquipmentType.CHEST)) {
            return BASE_DURABILITY[1] * 15;
        } else if (type.equals(ArmorEquipmentType.LEGS)) {
            return BASE_DURABILITY[2] * 15;
        } else if (type.equals(ArmorEquipmentType.FEET)) {
            return BASE_DURABILITY[3] * 15;
        }
        return 0;
    }

    @Override
    public int getProtection(ArmorEquipmentType type) {
        if (type.equals(ArmorEquipmentType.HEAD)) {
            return 2;
        } else if (type.equals(ArmorEquipmentType.CHEST)) {
            return 5;
        } else if (type.equals(ArmorEquipmentType.LEGS)) {
            return 6;
        } else if (type.equals(ArmorEquipmentType.FEET)) {
            return 2;
        }
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 9;
    }

    @Override
    public SoundEvent getEquipSound() {
        return CompatSoundEvents.ITEM_ARMOR_EQUIP_IRON.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.IRON_INGOT);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Identifier getId() {
        return IdentifierUtil.id(name);
    }

    @Override
    public float getToughness() {
        return 0.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}
