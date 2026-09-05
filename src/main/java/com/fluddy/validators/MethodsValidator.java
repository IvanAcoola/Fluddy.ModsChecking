package com.fluddy.validators;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.Objects;

public class MethodsValidator {

    public enum MethodsResults {
        CLEAR,
        HITBOX
    }

    public static MethodsResults ValidateMethods(AbstractInsnNode[] methods) {
        for (Object insn : methods) {
            if (insn instanceof MethodInsnNode methodInsn) {
                // ITS FORGE DETECT
                // System.out.println("\tMethod call: " + methodInsn.owner + "\t\t" + methodInsn.name + "\t\t" + methodInsn.desc); // You can uncomment for debugging
                if ((Objects.equals(methodInsn.owner, "net/minecraft/entity/player/PlayerEntity") || Objects.equals(methodInsn.owner, "net/minecraft/world/entity/Entity") || Objects.equals(methodInsn.owner, "net/minecraft/entity/player/EntityPlayer")) &&
                        (Objects.equals(methodInsn.name, "func_174826_a") || Objects.equals(methodInsn.name, "m_20011_")) &&
                        (Objects.equals(methodInsn.desc, "(Lnet/minecraft/util/math/AxisAlignedBB;)V") || Objects.equals(methodInsn.desc, "(Lnet/minecraft/world/phys/AABB;)V"))){
                    return MethodsResults.HITBOX;
                }
                // ITS FABRIC
                if ((Objects.equals(methodInsn.owner, "net/minecraft/class_1297") || Objects.equals(methodInsn.owner, "net/minecraft/class_1657")) &&
                        Objects.equals(methodInsn.name, "method_5857") &&
                        Objects.equals(methodInsn.desc, "(Lnet/minecraft/class_238;)V")){
                    return MethodsResults.HITBOX;
                }
                // EXTRA FORGE
                if (Objects.equals(methodInsn.owner, "net/minecraft/entity/Entity") &&
                        Objects.equals(methodInsn.name, "func_174813_aQ") &&
                        Objects.equals(methodInsn.desc, "()Lnet/minecraft/util/AxisAlignedBB;")){
                    return MethodsResults.HITBOX;
                }
                // ITS LABYMOD DETECT
                if ((Objects.equals(methodInsn.owner, "bfw") || Objects.equals(methodInsn.owner, "aqa")) &&
                        Objects.equals(methodInsn.name, "a") &&
                        Objects.equals(methodInsn.desc, "(Ldci;)V")){
                    return MethodsResults.HITBOX;
                }
            }
        }
        return MethodsResults.CLEAR;
    }
}
