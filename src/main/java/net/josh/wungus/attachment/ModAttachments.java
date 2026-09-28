package net.josh.wungus.attachment;

import net.josh.wungus.WungusMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, WungusMod.MOD_ID);

    // One per steroid type, so taking different steroids at the same time keeps separate progress
    public static final Supplier<AttachmentType<SteroidState>> HEALTH_STEROID_STATE = steroidState("health_steroid_state");
    public static final Supplier<AttachmentType<SteroidState>> SPEED_STEROID_STATE = steroidState("speed_steroid_state");
    public static final Supplier<AttachmentType<SteroidState>> JUMP_STEROID_STATE = steroidState("jump_steroid_state");

    private static Supplier<AttachmentType<SteroidState>> steroidState(String name) {
        return ATTACHMENT_TYPES.register(name, () -> AttachmentType.builder(SteroidState::new).serialize(SteroidState.CODEC).build());
    }

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
