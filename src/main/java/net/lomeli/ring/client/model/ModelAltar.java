package net.lomeli.ring.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAltar extends ModelBase {
    ModelRenderer mainSupport;
    ModelRenderer dishBase;
    ModelRenderer dishEdge1;
    ModelRenderer dishEdge2;
    ModelRenderer dishEdge3;
    ModelRenderer dishEdge4;
    ModelRenderer base1;
    ModelRenderer base2;

    public ModelAltar() {
        textureWidth = 64;
        textureHeight = 32;

        mainSupport = new ModelRenderer(this, 0, 0);
        mainSupport.addBox(0F, 0F, 0F, 2, 5, 2);
        mainSupport.setRotationPoint(-1F, 16F, -1F);
        mainSupport.setTextureSize(64, 32);
        mainSupport.mirror = true;
        setRotation(mainSupport, 0F, 0F, 0F);
        dishBase = new ModelRenderer(this, 8, 0);
        dishBase.addBox(0F, 0F, 0F, 6, 1, 6);
        dishBase.setRotationPoint(-3F, 15F, -3F);
        dishBase.setTextureSize(64, 32);
        dishBase.mirror = true;
        setRotation(dishBase, 0F, 0F, 0F);
        dishEdge1 = new ModelRenderer(this, 0, 7);
        dishEdge1.addBox(0F, 0F, 0F, 6, 1, 1);
        dishEdge1.setRotationPoint(-3F, 14F, 3F);
        dishEdge1.setTextureSize(64, 32);
        dishEdge1.mirror = true;
        setRotation(dishEdge1, 0F, 0F, 0F);
        dishEdge2 = new ModelRenderer(this, 0, 7);
        dishEdge2.addBox(0F, 0F, 0F, 6, 1, 1);
        dishEdge2.setRotationPoint(-3F, 14F, -4F);
        dishEdge2.setTextureSize(64, 32);
        dishEdge2.mirror = true;
        setRotation(dishEdge2, 0F, 0F, 0F);
        dishEdge3 = new ModelRenderer(this, 0, 9);
        dishEdge3.addBox(0F, 0F, 0F, 1, 1, 6);
        dishEdge3.setRotationPoint(3F, 14F, -3F);
        dishEdge3.setTextureSize(64, 32);
        dishEdge3.mirror = true;
        setRotation(dishEdge3, 0F, 0F, 0F);
        dishEdge4 = new ModelRenderer(this, 0, 9);
        dishEdge4.addBox(0F, 0F, 0F, 1, 1, 6);
        dishEdge4.setRotationPoint(-4F, 14F, -3F);
        dishEdge4.setTextureSize(64, 32);
        dishEdge4.mirror = true;
        setRotation(dishEdge4, 0F, 0F, 0F);
        base1 = new ModelRenderer(this, 32, 0);
        base1.addBox(0F, 0F, 0F, 6, 2, 6);
        base1.setRotationPoint(-3F, 22F, -3F);
        base1.setTextureSize(64, 32);
        base1.mirror = true;
        setRotation(base1, 0F, 0F, 0F);
        base2 = new ModelRenderer(this, 14, 7);
        base2.addBox(0F, 0F, 0F, 4, 1, 4);
        base2.setRotationPoint(-2F, 21F, -2F);
        base2.setTextureSize(64, 32);
        base2.mirror = true;
        setRotation(base2, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        mainSupport.render(f5);
        dishBase.render(f5);
        dishEdge1.render(f5);
        dishEdge2.render(f5);
        dishEdge3.render(f5);
        dishEdge4.render(f5);
        base1.render(f5);
        base2.render(f5);
    }
    
    public void render(float f5) {
        mainSupport.render(f5);
        dishBase.render(f5);
        dishEdge1.render(f5);
        dishEdge2.render(f5);
        dishEdge3.render(f5);
        dishEdge4.render(f5);
        base1.render(f5);
        base2.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
