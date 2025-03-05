package net.sodiumzh.nff.girls.gaia.client.gui;

import com.github.alexthe666.citadel.client.gui.GuiBasicBook;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nautils.math.HtmlColors;
import net.sodiumzh.nautils.math.LinearColor;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.client.gui.screen.CitadelBasedMobDictionaryGUI;

@OnlyIn(Dist.CLIENT)
public class NFFGirlsGaiaCitadelMobDictScreen extends GuiBasicBook {
    private static final ResourceLocation ROOT = new ResourceLocation("nffgirlsgaia:book/mob_dictionary/root.json");
    private static final String TITLE_TRANSLATION_KEY = "dict.nffgirlsgaia.title";
    private static final String TEXT_FILE_DIR = "nffgirlsgaia:book/mob_dictionary/";

    public NFFGirlsGaiaCitadelMobDictScreen(ItemStack bookStack) {
        super(bookStack, Component.translatable(TITLE_TRANSLATION_KEY));
    }

    public NFFGirlsGaiaCitadelMobDictScreen(ItemStack bookStack, String page) {
        super(bookStack, Component.translatable(TITLE_TRANSLATION_KEY));
        String dir = this.getTextFileDirectory();
        this.currentPageJSON = new ResourceLocation(dir + page + ".json");
    }

    public NFFGirlsGaiaCitadelMobDictScreen(ItemStack bookStack, Component title) {
        super(bookStack, title);
    }

    public void render(PoseStack matrixStack, int x, int y, float partialTicks) {
        if (this.currentPageJSON.equals(this.getRootPage()) && this.currentPageCounter == 0) {
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize + 128) / 2;
            RenderSystem.applyModelViewMatrix();
            PoseStack stack = RenderSystem.getModelViewStack();
            stack.pushPose();
            stack.translate((double)k, (double)l, 0.0);
            stack.scale(2.75F, 2.75F, 2.75F);
            //this.itemRenderer.renderGuiItem(new ItemStack((ItemLike) NFFGirlsItems.TAB_ICON.get()), 25, 14);
            //this.itemRenderer.blitOffset = 0.0F;
            stack.popPose();
            RenderSystem.applyModelViewMatrix();
        }

       /* RenderLaviathan.renderWithoutShaking = true;
        RenderMurmurBody.renderWithHead = true;
        RenderUnderminer.renderWithPickaxe = true;*/
        super.render(matrixStack, x, y, partialTicks);
       /* RenderLaviathan.renderWithoutShaking = false;
        RenderMurmurBody.renderWithHead = false;
        RenderUnderminer.renderWithPickaxe = false;*/
    }

    protected int getBindingColor() {
        return HtmlColors.HTML_COLORS.get("gray").toCode();
    }

    public ResourceLocation getRootPage() {
        return ROOT;
    }

    public String getTextFileDirectory() {
        return TEXT_FILE_DIR;
    }

    public static void openGUI(ItemStack itemStackIn) {
        Minecraft.getInstance().setScreen(new CitadelBasedMobDictionaryGUI(itemStackIn));
    }

    public static void openGUI(ItemStack itemStackIn, String page) {
        Minecraft.getInstance().setScreen(new CitadelBasedMobDictionaryGUI(itemStackIn, page));
    }
}
