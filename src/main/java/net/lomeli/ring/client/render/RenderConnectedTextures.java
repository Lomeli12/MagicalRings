package net.lomeli.ring.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderConnectedTextures {
    private Block ctBlock;
    private int meta;
    private RenderBlocks renderblocks;

    public RenderConnectedTextures(Block block, int meta, RenderBlocks renderblocks) {
        this.ctBlock = block;
        this.meta = meta;
        this.renderblocks = renderblocks;
    }

    public boolean renderCTBlock(IBlockAccess world, int x, int y, int z) {
        Tessellator tess = Tessellator.instance;
        renderblocks.enableAO = false;
        tess.setColorOpaque_F(1f, 1f, 1f);
        IIcon blockIcon = ctBlock.getIcon(7, meta);
        boolean flag = false;
        List<IIcon> iconList = new ArrayList<IIcon>();
        if (blockIcon instanceof IconCT) {
            IconCT icon = (IconCT) blockIcon;
            for (int side = 0; side < 6; side++) {
                int brightness = ctBlock.getMixedBrightnessForBlock(world, x, y, z);
                if (side == 2) {
                    iconList.add(icon.getBase());
                    tess.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : ctBlock.getMixedBrightnessForBlock(world, x, y, z + 1));
                    if (!doBlocksMatch(world, x - 1, y, z))
                        iconList.add(icon.getLeftEdge());
                    if (!doBlocksMatch(world, x + 1, y, z))
                        iconList.add(icon.getRightEdge());
                    if (!doBlocksMatch(world, x, y + 1, z))
                        iconList.add(icon.getTopEdge());
                    if (!doBlocksMatch(world, x, y - 1, z))
                        iconList.add(icon.getBottomEdge());
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x + 1, y, z)) {
                        if (!doBlocksMatch(world, x + 1, y - 1, z))
                            iconList.add(icon.getBottomRightCorner());
                    }
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x - 1, y, z)) {
                        if (!doBlocksMatch(world, x - 1, y - 1, z))
                            iconList.add(icon.getBottomLeftCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x + 1, y, z)) {
                        if (!doBlocksMatch(world, x + 1, y + 1, z))
                            iconList.add(icon.getTopRightCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x - 1, y, z)) {
                        if (!doBlocksMatch(world, x - 1, y + 1, z))
                            iconList.add(icon.getTopLeftCorner());
                    }
                    if (renderblocks.renderAllFaces || ctBlock.shouldSideBeRendered(world, x, y, z + 1, side)) {
                        flag = true;
                        if (iconList.size() == 1)
                            renderblocks.renderFaceZPos(ctBlock, x, y, z, getCenterIcon(world, x, y, z, side, icon));
                        else {
                            for (int i = 0; i < iconList.size(); i++) {
                                IIcon ic = iconList.get(i);
                                if (ic != null)
                                    renderblocks.renderFaceZPos(ctBlock, x, y, z, ic);
                            }
                        }
                    }
                } else if (side == 3) {
                    iconList.add(icon.getBase());
                    tess.setBrightness(renderblocks.renderMinZ > 0.0D ? brightness : ctBlock.getMixedBrightnessForBlock(world, x, y, z - 1));
                    if (!doBlocksMatch(world, x + 1, y, z))
                        iconList.add(icon.getLeftEdge());
                    if (!doBlocksMatch(world, x - 1, y, z))
                        iconList.add(icon.getRightEdge());
                    if (!doBlocksMatch(world, x, y + 1, z))
                        iconList.add(icon.getTopEdge());
                    if (!doBlocksMatch(world, x, y - 1, z))
                        iconList.add(icon.getBottomEdge());
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x + 1, y, z)) {
                        if (!doBlocksMatch(world, x + 1, y - 1, z))
                            iconList.add(icon.getBottomLeftCorner());
                    }
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x - 1, y, z)) {
                        if (!doBlocksMatch(world, x - 1, y - 1, z))
                            iconList.add(icon.getBottomRightCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x + 1, y, z)) {
                        if (!doBlocksMatch(world, x + 1, y + 1, z))
                            iconList.add(icon.getTopLeftCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x - 1, y, z)) {
                        if (!doBlocksMatch(world, x - 1, y + 1, z))
                            iconList.add(icon.getTopRightCorner());
                    }
                    if (renderblocks.renderAllFaces || ctBlock.shouldSideBeRendered(world, x, y, z - 1, side)) {
                        flag = true;
                        if (iconList.size() == 1)
                            renderblocks.renderFaceZNeg(ctBlock, x, y, z, getCenterIcon(world, x, y, z, side, icon));
                        else {
                            for (int i = 0; i < iconList.size(); i++) {
                                IIcon ic = iconList.get(i);
                                if (ic != null)
                                    renderblocks.renderFaceZNeg(ctBlock, x, y, z, ic);
                            }
                        }
                    }
                } else if (side == 4) {
                    iconList.add(icon.getBase());
                    tess.setBrightness(renderblocks.renderMaxX < 1.0D ? brightness : ctBlock.getMixedBrightnessForBlock(world, x + 1, y, z));
                    if (!doBlocksMatch(world, x, y, z + 1))
                        iconList.add(icon.getLeftEdge());
                    if (!doBlocksMatch(world, x, y, z - 1))
                        iconList.add(icon.getRightEdge());
                    if (!doBlocksMatch(world, x, y + 1, z))
                        iconList.add(icon.getTopEdge());
                    if (!doBlocksMatch(world, x, y - 1, z))
                        iconList.add(icon.getBottomEdge());
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x, y, z + 1)) {
                        if (!doBlocksMatch(world, x, y - 1, z + 1))
                            iconList.add(icon.getBottomLeftCorner());
                    }
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x, y, z - 1)) {
                        if (!doBlocksMatch(world, x, y - 1, z - 1))
                            iconList.add(icon.getBottomRightCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x, y, z + 1)) {
                        if (!doBlocksMatch(world, x, y + 1, z + 1))
                            iconList.add(icon.getTopLeftCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x, y, z - 1)) {
                        if (!doBlocksMatch(world, x, y + 1, z - 1))
                            iconList.add(icon.getTopRightCorner());
                    }
                    if (renderblocks.renderAllFaces || ctBlock.shouldSideBeRendered(world, x + 1, y, z, side)) {
                        flag = true;
                        if (iconList.size() == 1)
                            renderblocks.renderFaceXPos(ctBlock, x, y, z, getCenterIcon(world, x, y, z, side, icon));
                        else {
                            for (int i = 0; i < iconList.size(); i++) {
                                IIcon ic = iconList.get(i);
                                if (ic != null)
                                    renderblocks.renderFaceXPos(ctBlock, x, y, z, ic);
                            }
                        }
                    }
                } else if (side == 5) {
                    iconList.add(icon.getBase());
                    tess.setBrightness(renderblocks.renderMinX > 0.0D ? brightness : ctBlock.getMixedBrightnessForBlock(world, x - 1, y, z));
                    if (!doBlocksMatch(world, x, y, z - 1))
                        iconList.add(icon.getLeftEdge());
                    if (!doBlocksMatch(world, x, y, z + 1))
                        iconList.add(icon.getRightEdge());
                    if (!doBlocksMatch(world, x, y + 1, z))
                        iconList.add(icon.getTopEdge());
                    if (!doBlocksMatch(world, x, y - 1, z))
                        iconList.add(icon.getBottomEdge());
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x, y, z + 1)) {
                        if (!doBlocksMatch(world, x, y - 1, z + 1))
                            iconList.add(icon.getBottomRightCorner());
                    }
                    if (doBlocksMatch(world, x, y - 1, z) && doBlocksMatch(world, x, y, z - 1)) {
                        if (!doBlocksMatch(world, x, y - 1, z - 1))
                            iconList.add(icon.getBottomLeftCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x, y, z + 1)) {
                        if (!doBlocksMatch(world, x, y + 1, z + 1))
                            iconList.add(icon.getTopRightCorner());
                    }
                    if (doBlocksMatch(world, x, y + 1, z) && doBlocksMatch(world, x, y, z - 1)) {
                        if (!doBlocksMatch(world, x, y + 1, z - 1))
                            iconList.add(icon.getTopLeftCorner());
                    }
                    if (renderblocks.renderAllFaces || ctBlock.shouldSideBeRendered(world, x - 1, y, z, side)) {
                        flag = true;
                        if (iconList.size() == 1)
                            renderblocks.renderFaceXNeg(ctBlock, x, y, z, getCenterIcon(world, x, y, z, side, icon));
                        else {
                            for (int i = 0; i < iconList.size(); i++) {
                                IIcon ic = iconList.get(i);
                                if (ic != null)
                                    renderblocks.renderFaceXNeg(ctBlock, x, y, z, ic);
                            }
                        }
                    }
                } else if (side == 0 || side == 1) {
                    iconList.add(icon.getBase());
                    if (side == 0)
                        tess.setBrightness(renderblocks.renderMinY > 0.0D ? brightness : ctBlock.getMixedBrightnessForBlock(world, x, y - 1, z));
                    else
                        tess.setBrightness(renderblocks.renderMaxY < 1.0D ? brightness : ctBlock.getMixedBrightnessForBlock(world, x, y + 1, z));
                    if (!doBlocksMatch(world, x - 1, y, z))
                        iconList.add(icon.getLeftEdge());
                    if (!doBlocksMatch(world, x + 1, y, z))
                        iconList.add(icon.getRightEdge());
                    if (!doBlocksMatch(world, x, y, z - 1))
                        iconList.add(icon.getTopEdge());
                    if (!doBlocksMatch(world, x, y, z + 1))
                        iconList.add(icon.getBottomEdge());
                    if (doBlocksMatch(world, x - 1, y, z) && doBlocksMatch(world, x, y, z - 1)) {
                        if (!doBlocksMatch(world, x - 1, y, z - 1))
                            iconList.add(icon.getTopLeftCorner());
                    }
                    if (doBlocksMatch(world, x + 1, y, z) && doBlocksMatch(world, x, y, z - 1)) {
                        if (!doBlocksMatch(world, x + 1, y, z - 1))
                            iconList.add(icon.getTopRightCorner());
                    }
                    if (doBlocksMatch(world, x - 1, y, z) && doBlocksMatch(world, x, y, z + 1)) {
                        if (!doBlocksMatch(world, x - 1, y, z + 1))
                            iconList.add(icon.getBottomLeftCorner());
                    }
                    if (doBlocksMatch(world, x + 1, y, z) && doBlocksMatch(world, x, y, z + 1)) {
                        if (!doBlocksMatch(world, x + 1, y, z + 1))
                            iconList.add(icon.getBottomRightCorner());
                    }
                    if (renderblocks.renderAllFaces || ctBlock.shouldSideBeRendered(world, x, y + (side == 0 ? -1 : 1), z, side)) {
                        flag = true;
                        if (iconList.size() == 1) {
                            IIcon i = getCenterIcon(world, x, y, z, side, icon);
                            if (side == 0)
                                renderblocks.renderFaceYNeg(ctBlock, x, y, z, i);
                            else
                                renderblocks.renderFaceYPos(ctBlock, x, y, z, i);
                        } else {
                            for (int i = 0; i < iconList.size(); i++) {
                                IIcon ic = iconList.get(i);

                                if (ic != null) {
                                    if (side == 0)
                                        renderblocks.renderFaceYNeg(ctBlock, x, y, z, ic);
                                    else
                                        renderblocks.renderFaceYPos(ctBlock, x, y, z, ic);
                                }
                            }

                        }
                    }
                }
                iconList.clear();
            }
        } else
            this.renderblocks.renderStandardBlock(ctBlock, x, y, z);
        return flag;
    }

    private IIcon getCenterIcon(IBlockAccess world, int x, int y, int z, int side, IconCT iCT) {
        if (side == 0 || side == 1) {
            if (doBlocksMatch(world, x + 1, y, z) &&
                    doBlocksMatch(world, x - 1, y, z) &&
                    doBlocksMatch(world, x, y, z - 1) &&
                    doBlocksMatch(world, x, y, z + 1) &&
                    doBlocksMatch(world, x + 1, y, z - 1) &&
                    doBlocksMatch(world, x + 1, y, z + 1) &&
                    doBlocksMatch(world, x - 1, y, z - 1) &&
                    doBlocksMatch(world, x - 1, y, z + 1))
                return iCT.getBase();
        } else if (side == 4 || side == 5) {
            if (doBlocksMatch(world, x, y + 1, z) &&
                    doBlocksMatch(world, x, y - 1, z) &&
                    doBlocksMatch(world, x, y, z - 1) &&
                    doBlocksMatch(world, x, y, z + 1) &&
                    doBlocksMatch(world, x, y + 1, z - 1) &&
                    doBlocksMatch(world, x, y + 1, z + 1) &&
                    doBlocksMatch(world, x, y - 1, z - 1) &&
                    doBlocksMatch(world, x, y - 1, z + 1))
                return iCT.getBase();
        } else if (side == 2 || side == 3) {
            if (doBlocksMatch(world, x, y + 1, z) &&
                    doBlocksMatch(world, x, y - 1, z) &&
                    doBlocksMatch(world, x - 1, y, z) &&
                    doBlocksMatch(world, x + 1, y, z) &&
                    doBlocksMatch(world, x - 1, y + 1, z) &&
                    doBlocksMatch(world, x + 1, y + 1, z) &&
                    doBlocksMatch(world, x - 1, y - 1, z) &&
                    doBlocksMatch(world, x + 1, y - 1, z))
                return iCT.getBase();
        }
        return iCT.getFullIcon();
    }

    private boolean doBlocksMatch(IBlockAccess world, int x, int y, int z) {
        if (!world.isAirBlock(x, y, z)) {
            Block bl = world.getBlock(x, y, z);
            int blMeta = world.getBlockMetadata(x, y, z);
            if (bl != null)
                return bl == ctBlock && blMeta == meta;
        }
        return false;
    }
}
