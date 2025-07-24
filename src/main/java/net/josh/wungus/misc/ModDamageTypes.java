package net.josh.wungus.misc;

import net.josh.wungus.WungusMod;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Holder;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> STEROIDS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(WungusMod.MOD_ID, "steroids"));
    public static final ResourceKey<DamageType> CASHEW = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(WungusMod.MOD_ID, "cashew"));
    public static final ResourceKey<DamageType> NONEGUS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(WungusMod.MOD_ID, "nonegus"));
    public static final ResourceKey<DamageType> LANDMINE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(WungusMod.MOD_ID, "landmine"));

    public static DamageSource causeWungusSteroids(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(STEROIDS), 1);
    }

    public static DamageSource causeSantonioCashew(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(CASHEW), 2);
    }

    public static DamageSource causeLandMine(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(LANDMINE), 2);
    }

    public static DamageSource causeNonegusBite(RegistryAccess registryAccess, Entity source) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(NONEGUS), source, 2);
    }


    private static class DamageSourceRandomMessages extends DamageSource {

        private int messageCount;

        public DamageSourceRandomMessages(Holder.Reference<DamageType> message, int messageCount) {
            super(message);
            this.messageCount = messageCount;
        }

        public DamageSourceRandomMessages(Holder.Reference<DamageType> message, Entity source, int messageCount) {
            super(message, source);
            this.messageCount = messageCount;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity attacked) {
            int type = attacked.getRandom().nextInt(this.messageCount);
            String s = "death.attack." + this.getMsgId() + "_" + type;
            Entity entity = this.getDirectEntity() == null ? this.getEntity() : this.getDirectEntity();
            if (entity != null) {
                return Component.translatable(s + ".entity", attacked.getDisplayName(), entity.getDisplayName());
            }else{
                return Component.translatable(s, attacked.getDisplayName());
            }
        }
    }
}