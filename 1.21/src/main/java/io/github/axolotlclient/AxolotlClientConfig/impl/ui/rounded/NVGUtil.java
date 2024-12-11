package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.NVGMC;

public class NVGUtil {

	static {
		GlStateManager._glBindVertexArray(0);
	}

	public static void wrap(Consumer<Long> function) {
		NVGMC.wrap(function);
	}
}
