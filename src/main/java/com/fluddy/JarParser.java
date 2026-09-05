package com.fluddy;

import com.fluddy.validators.ClassNameValidator;
import com.fluddy.validators.FieldsValidator;
import com.fluddy.validators.MethodsValidator;
import com.fluddy.validators.StringsValidator;
import org.fusesource.jansi.Ansi;
import org.json.JSONObject;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarParser {
    private final JSONObject finalResult = new JSONObject();

    private final byte[] jarBytes;

    private int fields_time = 0;
    private int variables_time = 0;
    private int instructions_time = 0;

    public JarParser(String checksum, byte[] jarBytes) {
        this.jarBytes = jarBytes;
        finalResult.put("hash", checksum);
        this.finalResult.put("illegalStrings", false);
        this.finalResult.put("illegalClasses", false);
        this.finalResult.put("illegalFields", false);
        this.finalResult.put("stealler", false);
        this.finalResult.put("hitbox", false);
        this.finalResult.put("overweight", false);
    }

    public void analyzeJar() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.jarBytes);

        JarInputStream jarInputStream = new JarInputStream(byteArrayInputStream);
        List<JarEntry> jarEntries = new ArrayList<>();

        JarEntry entryTemp;
        while ((entryTemp = jarInputStream.getNextJarEntry()) != null) {
            jarEntries.add(entryTemp);
        }

        Enumeration<JarEntry> entries = Collections.enumeration(jarEntries);

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                processClass(entry);
            }
        }

        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("[~]").reset().a(" Fields: ").a(fields_time));
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("[~]").reset().a(" Variables: ").a(variables_time));
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("[~]").reset().a(" Instructions: ").a(instructions_time));
    }

    private void processClass(JarEntry entry) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.jarBytes);

        JarInputStream jarInputStream = new JarInputStream(byteArrayInputStream);
        JarEntry jarEntry = jarInputStream.getNextJarEntry();

        while (jarEntry != null) {
            if (jarEntry.getName().equals(entry.getName())) {
                ClassReader classReader = new ClassReader(jarInputStream);
                ClassNode classNode = new ClassNode(Opcodes.ASM9);
                classReader.accept(classNode, 0);

                if (ClassNameValidator.ValidateClassName(classNode.name)) {
                    //System.out.println("[ ! ] Found illegal classname in " + classNode.name); // You can uncomment for debugging
                    this.finalResult.put("illegalClasses", true);
                }

                if (classNode.fields != null) {
                    long startTime = System.nanoTime();// TIME

                    for (FieldNode field : classNode.fields) {
                        if (FieldsValidator.ValidateField(field)) this.finalResult.put("illegalFields", true);
                    }

                    long endTime = System.nanoTime();// TIME
                    long executionTime = (endTime - startTime) / 10000; // TIME
                    fields_time += executionTime;// TIME
                }

                for (MethodNode method : classNode.methods) {

                    long startTime = System.nanoTime();// TIME
                    if (method.localVariables != null) {
                        for (LocalVariableNode lfield : method.localVariables) {
                            if (FieldsValidator.ValidateField(lfield)) this.finalResult.put("illegalFields", true);
                        }
                    }
                    long endTime = System.nanoTime();// TIME
                    long executionTime = (endTime - startTime) / 10000; // TIME
                    variables_time += executionTime;// TIME

                    MethodsValidator.MethodsResults methResult = MethodsValidator.ValidateMethods(method.instructions.toArray());
                    if (methResult != MethodsValidator.MethodsResults.CLEAR) {
                        if (methResult == MethodsValidator.MethodsResults.HITBOX) this.finalResult.put("hitbox", true);
                    }
                    long startTime_1 = System.nanoTime();// TIME
                    for (Object insn : method.instructions.toArray()) {
                        if (insn instanceof LdcInsnNode ldcInsn) {
                            if (ldcInsn.cst instanceof String stringValue) {
                                StringsValidator.StringResults resultStringValidation = StringsValidator.ValidateString(stringValue);
                                if (resultStringValidation != StringsValidator.StringResults.CLEAR) {
                                    if (resultStringValidation == StringsValidator.StringResults.ILLEGAL) this.finalResult.put("illegalStrings", true);
                                    else if (resultStringValidation == StringsValidator.StringResults.STEALLER) this.finalResult.put("stealler", true);
                                }
                            }
                        }
                    }
                    long endTime_1 = System.nanoTime();// TIME
                    long executionTime_1 = (endTime_1 - startTime_1) / 10000; // TIME
                    instructions_time += executionTime_1;// TIME
                }
            }
            jarEntry = jarInputStream.getNextJarEntry();
        }
    }



    public JSONObject getFinalResult() {
        return this.finalResult;
    }
}
