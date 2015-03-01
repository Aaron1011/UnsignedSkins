package org.aaron1011.unsignedskins;


import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8")
public class UnsignedSkins implements IFMLLoadingPlugin, IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        if (name.equals("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService")) {
            System.out.println("Willl probably transform");
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classBytes);
            classReader.accept(classNode, 0);

            MethodNode getTextures = this.findMethod(classNode, "getTextures", "(Lcom/mojang/authlib/GameProfile;Z)Ljava/util/Map;");
            MethodNode fillProfileProperties = this.findMethod(classNode, "fillProfileProperties", "(Lcom/mojang/authlib/GameProfile;Z)Lcom/mojang/authlib/GameProfile;");
            MethodNode fillGameProfile = this.findMethod(classNode, "fillGameProfile", "(Lcom/mojang/authlib/GameProfile;Z)Lcom/mojang/authlib/GameProfile;");

            forceFalse(getTextures, 2);
            forceFalse(fillProfileProperties, 2);
            forceFalse(fillGameProfile, 2);

            ClassWriter writer = new ClassWriter(0);
            classNode.accept(writer);
            //return writer.toByteArray();
        }
        return classBytes;
    }

    // Insert in reverse order, as the insns get inserted at the beginning
    private void forceFalse(MethodNode node, int index) {
        node.instructions.insert(new VarInsnNode(Opcodes.ISTORE, index));
        node.instructions.insert(new IntInsnNode(Opcodes.BIPUSH, 0));
    }

    private MethodNode findMethod(ClassNode classNode, String name, String desc) {
        for (MethodNode node: classNode.methods) {
            if (node.name.equals(name) && node.desc.equals(desc)) {
                return node;
            }
        }
        throw new IllegalStateException("Well, look who thought they knew where the method was...");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { getClass().getName() };
    }

    @Override
    public String getModContainerClass() {
        return UnsignedSkinsMod.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
