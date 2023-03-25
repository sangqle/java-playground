package com.sangqle.sample.reflection;
import org.objectweb.asm.*;

import java.lang.reflect.Method;

public class TestMyReflection {
    public static void main(String[] args) throws Exception {
        Class<MyReflection> myReflectionClass = MyReflection.class;
        Method[] methods = myReflectionClass.getDeclaredMethods();
        for (Method method : methods) {
            System.err.println(String.format("method: %s", method.getName()));
        }

        // Use ASM to modify the bytecode of the isValidPin method
        ClassReader cr = new ClassReader(myReflectionClass.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cr.accept(new MethodModifier(cw), ClassReader.EXPAND_FRAMES);

        // Load the modified class and call method2 with any pin value
        byte[] modifiedClass = cw.toByteArray();
        MyClassLoader loader = new MyClassLoader();
        Class<?> modifiedMyReflectionClass = loader.defineClass(myReflectionClass.getName(), modifiedClass);
        Object instance = modifiedMyReflectionClass.getField("Instance").get(null);
        Method method2 = modifiedMyReflectionClass.getDeclaredMethod("method2", int.class);
        method2.setAccessible(true);

        System.err.println("\nInvoke private method2 without invoke check isValidPin inside method2");
        method2.invoke(instance, 44533); // isValidPin will now always return true
    }

    // A class that uses ASM to modify the bytecode of the isValidPin method
    static class MethodModifier extends ClassVisitor {
        public MethodModifier(ClassWriter cw) {
            super(Opcodes.ASM9, cw);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (name.equals("isValidPin")) {
                return new MethodVisitor(Opcodes.ASM9, mv) {
                    @Override
                    public void visitCode() {
                        mv.visitInsn(Opcodes.ICONST_1);
                        mv.visitInsn(Opcodes.IRETURN);
                        mv.visitMaxs(1, 1);
                    }
                };
            } else {
                return mv;
            }
        }
    }

    // A class loader that allows us to load the modified class at runtime
    static class MyClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}

