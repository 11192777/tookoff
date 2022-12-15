package pers.qingyu.tookoff.asm;


import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

public class ClassAdapterVisitor extends ClassVisitor {
    //当前类的类名称
    //本例：com/zxj/plugin/demo/MainActivity
    private String className;

    //className类的父类名称
    //本例：androidx/appcompat/app/AppCompatActivity
    private String superName;

    public ClassAdapterVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
        System.out.println("className:" + name + ",superName:" + superName + ",interfaces.length:" + interfaces.length);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println("====access:" + access + ",name:" + name + ",descriptor:" + descriptor + ",signature:" + signature + ",value:" + value);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("methodName:" + name + ",descriptor:" + descriptor + ",signature:" + signature);

        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        if ("init".equals(name) && "()V".equals(descriptor)) {
            return new MethodAdapterVisitor(Opcodes.ASM7, mv, access, name, descriptor, className);
        }
        return mv;
    }

    public static void main(String[] args) {
        SayHello sayHello = new SayHello();
        sayHello.test();
    }
}
