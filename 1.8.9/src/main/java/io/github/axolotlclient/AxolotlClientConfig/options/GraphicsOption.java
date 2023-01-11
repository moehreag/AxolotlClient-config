package io.github.axolotlclient.AxolotlClientConfig.options;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.github.axolotlclient.AxolotlClientConfig.common.commands.CommandResponse;
import io.github.axolotlclient.AxolotlClientConfig.screen.OptionsScreenBuilder;
import io.github.axolotlclient.AxolotlClientConfig.screen.overlay.GraphicsEditorWidget;
import io.github.axolotlclient.AxolotlClientConfig.screen.widgets.GenericOptionWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphicsOption extends OptionBase<int[][]> {

    private NativeImageBackedTexture texture;
    private Identifier imageId;
    private int height;
    private int width;

    public GraphicsOption(String name, int[][] def) {
        super(name, def);
    }

    public GraphicsOption(String name, ChangedListener<int[][]> onChange, int[][] def) {
        super(name, onChange, def);
    }

    public GraphicsOption(String name, String tooltipKeyPrefix, int[][] def) {
        super(name, tooltipKeyPrefix, def);
    }

    public GraphicsOption(String name, String tooltipKeyPrefix, ChangedListener<int[][]> onChange, int[][] def) {
        super(name, tooltipKeyPrefix, onChange, def);
    }

    @Override
    protected CommandResponse onCommandExecution(String[] arg) {

        if (arg.length > 0) {
            try {
                setValueFromJsonElement(new JsonParser().parse(arg[0]));
                return new CommandResponse(true, "Successfully set "+getName()+" to its new value!");
            } catch (Exception e){
                return new CommandResponse(false, "Failed to parse input "+arg[0]+"!");
            }
        }

        return new CommandResponse(true, getName() + " is currently set to '" + getJson() + "'.");
    }

    @Override
    public List<String> getCommandSuggestions(String[] args) {
        return new ArrayList<>();
    }

    @Override
    public ButtonWidget getWidget(int x, int y, int width, int height) {
        return new GenericOptionWidget(x, y, width, height,
                new GenericOption(getName(), "openEditor", (mouseX, mouseY) ->
                        ((OptionsScreenBuilder) MinecraftClient.getInstance().currentScreen).setOverlay(
                                new GraphicsEditorWidget(this))));
    }

    @Override
    public JsonElement getJson() {
        JsonArray data = new JsonArray();

        for (int[] a : get()) {
            JsonArray j = new JsonArray();
            for (int i : a) {
                j.add(new JsonPrimitive(i));
            }
            data.add(j);
        }

        return data;
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {

        JsonArray data = element.getAsJsonArray();

        int i = 0;
        for (int[] a : option) {
            JsonArray r = data.get(i).getAsJsonArray();

            for (int it = 0; it < a.length; it++) {
                a[it] = r.get(it).getAsInt();
            }

            i++;
        }
    }

    public void bindTexture() {
        if(texture == null){
            int[][] data = get();
            AtomicInteger width = new AtomicInteger();
            Arrays.stream(data).forEach(arr -> width.set(Math.max(width.get(), arr.length)));
            height = data.length;
            this.width = width.get();
            imageId = new Identifier("graphicsoption", getName().toLowerCase(Locale.ROOT)+java.util.UUID.randomUUID().toString().replace('-', '_'));
            texture = new NativeImageBackedTexture(this.width, height);

            MinecraftClient.getInstance().getTextureManager().loadTexture(imageId, texture);
        }

        int[] pix = texture.getPixels();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rows = (y)*width + x;
                pix[rows] = get()[x][y];
            }
        }
        texture.upload();

        MinecraftClient.getInstance().getTextureManager().bindTexture(imageId);
    }
}
