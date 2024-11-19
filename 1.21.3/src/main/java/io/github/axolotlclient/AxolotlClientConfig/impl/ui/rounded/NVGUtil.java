package io.github.axolotlclient.AxolotlClientConfig.impl.ui.rounded;

import io.github.axolotlclient.AxolotlClientConfig.impl.ui.NVGMC;

import java.util.function.Consumer;

public class NVGUtil {

	public static void wrap(Consumer<Long> function) {
		NVGMC.wrap(function);
	}
}
