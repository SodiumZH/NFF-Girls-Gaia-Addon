package net.sodiumzh.nff.girls.gaia.client.gui;

import com.github.alexthe666.citadel.client.gui.GuiBasicBook;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nautils.math.HtmlColors;
import net.sodiumzh.nautils.math.LinearColor;

@OnlyIn(Dist.CLIENT)
public class NFFGirlsGaiaCitadelMobDictScreen extends GuiBasicBook {
    private static final ResourceLocation ROOT = new ResourceLocation("nffgirlsgaia:book/mob_dictionary/root.json");

    public NFFGirlsGaiaCitadelMobDictScreen(ItemStack bookStack) {
        super(bookStack, Component.translatable("dict.nffgirlsgaia.title"));
    }

    public NFFGirlsGaiaCitadelMobDictScreen(ItemStack bookStack, String page) {
        super(bookStack, Component.translatable("dict.nffgirlsgaia.title"));
        String dir = this.getTextFileDirectory();
        this.currentPageJSON = new ResourceLocation(dir + page + ".json");
    }

    public NFFGirlsGaiaCitadelMobDictScreen(ItemStack bookStack, Component title) {
        super(bookStack, title);
    }

    public void render(GuiGraphics matrixStack, int x, int y, float partialTicks) {
        if (this.currentPageJSON.equals(this.getRootPage()) && this.currentPageCounter == 0) {
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize + 128) / 2;
            RenderSystem.applyModelViewMatrix();
            PoseStack stack = RenderSystem.getModelViewStack();
            stack.pushPose();
            stack.translate((double)k, (double)l, 0.0);
            stack.scale(2.75F, 2.75F, 2.75F);
            stack.popPose();
            RenderSystem.applyModelViewMatrix();
        }

        super.render(matrixStack, x, y, partialTicks);
    }

    protected int getBindingColor() {
        return HtmlColors.HTML_COLORS.get("gray").toCode();
    }

    public ResourceLocation getRootPage() {
        return ROOT;
    }

    public String getTextFileDirectory() {
        return "nffgirlsgaia:book/mob_dictionary/";
    }

    public static void openGUI(ItemStack itemStackIn) {
        Minecraft.getInstance().setScreen(new NFFGirlsGaiaCitadelMobDictScreen(itemStackIn));
    }

    public static void openGUI(ItemStack itemStackIn, String page) {
        Minecraft.getInstance().setScreen(new NFFGirlsGaiaCitadelMobDictScreen(itemStackIn, page));
    }
}
