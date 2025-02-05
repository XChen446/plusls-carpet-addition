package com.plusls.carpet.mixin.rule.superLead;

import com.plusls.carpet.PluslsCarpetAdditionSettings;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class MixinAbstractVillager extends AgableMob {
    protected MixinAbstractVillager(EntityType<? extends AgableMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "canBeLeashedBy",  // 正确目标方法名
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private void postCanBeLeashedBy(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (PluslsCarpetAdditionSettings.superLead) {
            // 逻辑：若启用 superLead 规则，允许牵引未被拴绳的村民
            cir.setReturnValue(!this.isLeashed());
        }
    }
}
