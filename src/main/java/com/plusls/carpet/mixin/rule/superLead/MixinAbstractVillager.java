package com.plusls.carpet.mixin.rule.superLead;

import com.plusls.carpet.PluslsCarpetAdditionSettings;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantEntity.class)
public abstract class MixinMerchantEntity {

    @Inject(
        method = "canBeLeashedBy", // 确认方法存在于 MerchantEntity 或其父类
        at = @At("HEAD"),
        cancellable = true
    )
    private void postCanBeLeashedBy(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (PluslsCarpetAdditionSettings.superLead) {
            cir.setReturnValue(!((MerchantEntity) (Object) this).isLeashed());
        }
    }
}
