package com.fluddy.validators;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;

public class FieldsValidator {

    private static boolean GlobalValidator(String name, String desc) {
        //System.out.println("\t[ Field ]: " + name + " (" + desc + ")"); // You can uncomment for debugging

        long count_l = name.chars().filter(ch -> ch == 'l').count();
        long count_i = name.chars().filter(ch -> ch == 'I').count();

        if (name.length() / 4 < count_i && name.length() / 4 < count_l) {
            return true; // Found illegal name
        }

        for (char ch : name.toCharArray()) {
            if ((int)ch <= 32 || (int)ch >= 127) {
                //System.out.println("[ ! ] Found illegal fieldname in " + name + " , symbol = " + ch + " ascii = " + (int)ch); // You can uncomment for debugging
                return true; // Found illegal symbol
            }
        }

        if (name.equals("ALLATORIxDEMO") || name.equals("nothing_to_see_here")) return true;

        return false;
    }
    public static Boolean ValidateField(FieldNode node) {
        return GlobalValidator(node.name, node.desc);
    }

    public static Boolean ValidateField(LocalVariableNode node) {
        return GlobalValidator(node.name, node.desc);
    }
}
