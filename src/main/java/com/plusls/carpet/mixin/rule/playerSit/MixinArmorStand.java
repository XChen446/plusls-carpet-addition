package com.plusls.carpet.mixin.rule.playerSit;

import com.plusls.carpet.util.rule.playerSit.SitEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class MixinArmorStand extends LivingEntity implements SitEntity {
    private boolean pca$sitEntity = false;

    protected MixinArmorStand(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract void setMarker(boolean marker);

    @Override
    public boolean pca$isSitEntity() {
        return pca$sitEntity;
    }

    @Override
    public void pca$setSitEntity(boolean isSitEntity) {
        this.pca$sitEntity = isSitEntity;
        this.setMarker(isSitEntity);
        this.setInvisible(isSitEntity);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (this.pca$isSitEntity()) {
            this.setPos(this.getX(), this.getY() + 0.16, this.getZ());
            this.kill();
        }
        super.removePassenger(passenger);
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (this.pca$sitEntity) {
            nbt.putBoolean("SitEntity", true);
        }
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postReadAdditionalSaveData(@NotNull CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("SitEntity", Tag.TAG_BYTE)) {
            this.pca$sitEntity = nbt.getBoolean("SitEntity");
        }
    }
}