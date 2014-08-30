package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.util.Access;
import eu.bibl.updater.analysis.Analyser;

public class AxisAlignedBBAnalyser extends Analyser {
	
	private static final int[] SET_BOUNDS_REGEX = new int[] {
			ALOAD,
			DLOAD,
			PUTFIELD,
			ALOAD,
			DLOAD,
			PUTFIELD,
			ALOAD,
			DLOAD,
			PUTFIELD,
			ALOAD,
			DLOAD,
			PUTFIELD,
			ALOAD,
			DLOAD,
			PUTFIELD,
			ALOAD,
			DLOAD,
			PUTFIELD,
			ALOAD };
	
	private static final int[] ADD_CHORD_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DSTORE,
			DLOAD,
			DCONST_0,
			DCMPG };
	
	private static final int[] EXPAND_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			DLOAD,
			DSUB,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DSUB,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DSUB,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			DSTORE };
	
	private static final int[] GET_OFFSETED_BOX__REGEX = new int[] {
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD, };
	
	private static final int[] OFFSET_REGEX = new int[] {
			ALOAD,
			DUP,
			GETFIELD,
			DLOAD,
			DADD,
			PUTFIELD,
			ALOAD,
			DUP,
			GETFIELD,
			DLOAD,
			DADD,
			PUTFIELD,
			ALOAD,
			DUP,
			GETFIELD,
			DLOAD,
			DADD,
			PUTFIELD,
			ALOAD,
			DUP,
			GETFIELD,
			DLOAD,
			DADD,
			PUTFIELD,
			ALOAD,
			DUP,
			GETFIELD,
			DLOAD,
			DADD,
			PUTFIELD,
			ALOAD,
			DUP,
			GETFIELD,
			DLOAD,
			DADD,
			PUTFIELD,
			ALOAD };
	
	private static final int[] CONTRACT_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DADD,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DSUB,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DSUB,
			DSTORE,
			ALOAD,
			GETFIELD,
			DLOAD,
			DSUB,
			DSTORE };
	
	public AxisAlignedBBAnalyser() {
		super("AxisAlignedBB");
		hooks = new FieldHook[] {
				new FieldHook("getMinX", "D", "D"),
				new FieldHook("getMinY", "D", "D"),
				new FieldHook("getMinZ", "D", "D"),
				new FieldHook("getMaxX", "D", "D"),
				new FieldHook("getMaxY", "D", "D"),
				new FieldHook("getMaxZ", "D", "D") };
		methodHooks = new MethodHook[] {
				new MethodHook("createNewBoundingBox", "(DDDDDD)L" + INTERFACES + "entity/IAxisAlignedBB;"),
				new MethodHook("setBounds", "(DDDDDD)L" + INTERFACES + "entity/IAxisAlignedBB;"),
				new MethodHook("addCoord", "(DDD)L" + INTERFACES + "entity/IAxisAlignedBB;"),
				new MethodHook("expand", "(DDD)L" + INTERFACES + "entity/IAxisAlignedBB;"),
				new MethodHook("getOffsettedBoundingBox", "(DDD)L" + INTERFACES + "entity/IAxisAlignedBB;"),
				new MethodHook("intersectsWith", "(L" + INTERFACES + "entity/IAxisAlignedBB;)Z"),
				new MethodHook("offset", "(DDD)L" + INTERFACES + "entity/IAxisAlignedBB;"),
				new MethodHook("contract", "(DDD)L" + INTERFACES + "entity/IAxisAlignedBB;"), };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "box[");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IAxisAlignedBB"));
		
		addHook(methodHooks[5].buildObfMn(methods(cn, "(L" + cn.name + ";)Z").get(0)));
		
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("toString")) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, new int[] { GETFIELD });
				if (is.match()) {
					for(int i = 0; i < is.size(); i++) {
						FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(i)[0];
						addHook(hooks[i].buildObfFin(fin));
					}
				}
			}
		}
		
		for(MethodNode m : methods(cn, "(DDDDDD)L" + cn.name + ";")) {
			if (Access.isStatic(m.access)) {
				addHook(methodHooks[0].buildObfMn(m));
			} else {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, SET_BOUNDS_REGEX);
				if (is.match()) {
					addHook(methodHooks[1].buildObfMn(m));
				}
			}
		}
		
		for(MethodNode m : methods(cn, "(DDD)L" + cn.name + ";")) {
			if (!methodHooks[2].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, ADD_CHORD_REGEX);
				if (is.match()) {
					addHook(methodHooks[2].buildObfMn(m));
				}
			}
			if (!methodHooks[3].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, EXPAND_REGEX);
				if (is.match()) {
					addHook(methodHooks[3].buildObfMn(m));
				}
			}
			if (!methodHooks[4].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, GET_OFFSETED_BOX__REGEX);
				if (is.match()) {
					addHook(methodHooks[4].buildObfMn(m));
				}
			}
			if (!methodHooks[6].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, OFFSET_REGEX);
				if (is.match()) {
					addHook(methodHooks[6].buildObfMn(m));
				}
			}
			if (!methodHooks[7].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, CONTRACT_REGEX);
				if (is.match()) {
					addHook(methodHooks[7].buildObfMn(m));
				}
			}
		}
	}
}