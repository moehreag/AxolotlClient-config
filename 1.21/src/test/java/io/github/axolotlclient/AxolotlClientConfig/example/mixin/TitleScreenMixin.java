package io.github.axolotlclient.AxolotlClientConfig.example.mixin;

import io.github.axolotlclient.AxolotlClientConfig.example.Example;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.button.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	protected TitleScreenMixin() {
		super(null);
	}

	@Inject(method = "initWidgetsNormal", at = @At("TAIL"))
	private void addButton(int y, int spacingY, CallbackInfo ci) {
		addDrawableSelectableElement(ButtonWidget.builder(Text.literal("test gui"), buttonWidget -> {
			client.setScreen(Example.getInstance().getConfigScreenFactory("axolotlclientconfig-test").apply(this));
		}).build());
	}
}
