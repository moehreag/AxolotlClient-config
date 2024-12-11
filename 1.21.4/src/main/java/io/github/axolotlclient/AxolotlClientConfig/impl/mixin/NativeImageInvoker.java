package io.github.axolotlclient.AxolotlClientConfig.impl.mixin;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NativeImage.class)
public interface NativeImageInvoker {
	@Invoker("writeToChannel")
	boolean invokeWrite(WritableByteChannel channel) throws IOException;
}
