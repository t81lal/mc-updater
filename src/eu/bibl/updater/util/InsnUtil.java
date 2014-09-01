package eu.bibl.updater.util;

import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.asm.ClassNode;
import eu.bibl.banalysis.filter.FieldFilter;
import eu.bibl.banalysis.filter.MethodFilter;

public final class InsnUtil {
	
	public static boolean containsLdc(ClassNode cn, String cst) {
		for (MethodNode m : cn.methods()) {
			if (containsLdc(m, cst))
				return true;
		}
		return false;
	}
	
	public static boolean containsLdc(MethodNode m, String cst) {
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain instanceof LdcInsnNode) {
				LdcInsnNode ldc = (LdcInsnNode) ain;
				if (ldc.cst.toString().equals(cst))
					return true;
			}
		}
		return false;
	}
	
	public static LdcInsnNode getLdc(ClassNode cn, String cst) {
		for (MethodNode m : cn.methods()) {
			LdcInsnNode ldc = null;
			if ((ldc = getLdc(m, cst)) != null)
				return ldc;
		}
		return null;
	}
	
	public static LdcInsnNode getLdc(MethodNode m, String cst) {
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain instanceof LdcInsnNode) {
				LdcInsnNode ldc = (LdcInsnNode) ain;
				if (ldc.cst.toString().equals(cst))
					return ldc;
			}
		}
		return null;
	}
	
	public static List<FieldNode> fields(ClassNode cn, String desc) {
		return cn.fields(new FieldFilter() {
			
			@Override
			public boolean accept(FieldNode t) {
				return t.desc.equals(desc);
			}
		});
	}
	
	public static List<MethodNode> methods(ClassNode cn, String desc) {
		return cn.methods(new MethodFilter() {
			
			@Override
			public boolean accept(MethodNode t) {
				return t.desc.equals(desc);
			}
		});
	}
}