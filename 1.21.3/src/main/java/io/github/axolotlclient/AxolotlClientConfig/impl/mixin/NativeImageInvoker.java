package io.github.axolotlclient.AxolotlClientConfig.impl.mixin;

import com.mojang.blaze3d.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

@Mixin(NativeImage.class)
public interface NativeImageInvoker {
	@Invoker("write")
	boolean invokeWrite(WritableByteChannel channel) throws IOException;
}
