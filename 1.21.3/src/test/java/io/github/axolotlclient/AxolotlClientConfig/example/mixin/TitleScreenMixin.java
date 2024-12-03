package io.github.axolotlclient.AxolotlClientConfig.example.mixin;

import io.github.axolotlclient.AxolotlClientConfig.example.Example;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	protected TitleScreenMixin() {
		super(null);
	}

	@Inject(method = "createNormalMenuOptions", at = @At("TAIL"))
	private void addButton(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
		addRenderableWidget(Button.builder(Component.literal("test gui"), buttonWidget -> {
			minecraft.setScreen(Example.getInstance().getConfigScreenFactory("axolotlclientconfig-test").apply(this));
		}).build());
	}
}
