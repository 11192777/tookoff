package pers.qingyu.tookoff.asm;


import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

public class MethodAdapterVisitor extends AdviceAdapter {
    private String methodName;
    private String className;

    public MethodAdapterVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, String className) {
        super(api, methodVisitor, access, name, descriptor);
        this.className = className;
        methodName = name;
        System.out.println("MethodAdapterVisitor->MethodName:"+name);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        /**
         * if (this.mContext == null) {
         *     Log.e("zuojie", "mContext is null");
         *     return;
         * }
         */
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "bi/com/xxx/bi/GetBaseDataInfo", "mContext", "Landroid/content/Context;");
        Label label1 = new Label();
        mv.visitJumpInsn(IFNONNULL, label1);// IFNULL IFNONNULL
        Label label2 = new Label();
        mv.visitLabel(label2);
        mv.visitLdcInsn("zuojie");
        mv.visitLdcInsn(className+",mContext is null");
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
        Label label3 = new Label();
        mv.visitLabel(label3);
        mv.visitInsn(RETURN);
        mv.visitLabel(label1);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }
}
