package io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.screen;

import io.github.axolotlclient.AxolotlClientConfig.api.manager.ConfigManager;
import io.github.axolotlclient.AxolotlClientConfig.api.options.OptionCategory;
import io.github.axolotlclient.AxolotlClientConfig.api.ui.screen.ConfigScreen;
import io.github.axolotlclient.AxolotlClientConfig.impl.ui.vanilla.widgets.VanillaEntryListWidget;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ScreenTexts;
import net.minecraft.text.Text;

public class VanillaConfigScreen extends Screen implements ConfigScreen {
	private final Screen parent;
	@Getter
	private final ConfigManager configManager;
	private final OptionCategory category;

	public VanillaConfigScreen(Screen parent, ConfigManager manager, OptionCategory category) {
		super(Text.translatable(category.getName()));
		this.parent = parent;
		this.configManager = manager;
		this.category = category;
	}

	@Override
	protected void init() {
		addDrawableChild(new VanillaEntryListWidget(configManager, category, width, height, 45, height - 55, 25));
		addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, w -> closeScreen())
			.position(width / 2 - 75, height - 45).build());
	}

	@Override
	public void render(MatrixStack graphics, int mouseX, int mouseY, float delta) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, delta);

		drawCenteredText(graphics, client.textRenderer, getTitle(), width / 2, 25, -1);
	}

	@Override
	public void closeScreen() {
		client.setScreen(parent);
		configManager.save();
	}
}
