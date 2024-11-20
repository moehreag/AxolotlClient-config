package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.NVGMC;

import java.util.function.Consumer;

public class NVGUtil {

	static {
		GlStateManager._glBindVertexArray(0);
	}

	public static void wrap(Consumer<Long> function) {
		NVGMC.wrap(function);
	}
}
