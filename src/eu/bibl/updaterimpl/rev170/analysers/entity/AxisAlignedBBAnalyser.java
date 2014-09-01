package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.asm.insn.InstructionSearcher;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

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
	
	public AxisAlignedBBAnalyser(ClassContainer container, HookMap hookMap) {
		super("AxisAlignedBB", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getMinX"), new MappingData("D", "D"), false),
				/* 1 */new FieldMappingData(new MappingData("getMinY"), new MappingData("D", "D"), false),
				/* 2 */new FieldMappingData(new MappingData("getMinZ"), new MappingData("D", "D"), false),
				/* 3 */new FieldMappingData(new MappingData("getMaxX"), new MappingData("D", "D"), false),
				/* 4 */new FieldMappingData(new MappingData("getMaxY"), new MappingData("D", "D"), false),
				/* 5 */new FieldMappingData(new MappingData("getMaxZ"), new MappingData("D", "D"), false), };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData(new MappingData("createNewBoundingBox"), new MappingData("(DDDDDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), null, null, false),
				new CallbackMappingData(new MappingData("setBounds"), new MappingData("(DDDDDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), null, null, false),
				new CallbackMappingData(new MappingData("addCoord"), new MappingData("(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), null, null, false),
				new CallbackMappingData(new MappingData("expand"), new MappingData("(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), null, null, false),
				new CallbackMappingData(new MappingData("getOffsettedBoundingBox"), new MappingData("(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), null, null, false),
				new CallbackMappingData(new MappingData("intersectsWith"), new MappingData("(L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;)Z"), null, null, false),
				new CallbackMappingData(new MappingData("offset"), new MappingData("(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), null, null, false),
				new CallbackMappingData(new MappingData("contract"), new MappingData("(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), null, null, false), };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "box[");
	}
	
	@Override
	public InterfaceMappingData run() {
		addMethod(methodHooks[5].buildObfMethod(InsnUtil.methods(cn, "(L" + cn.name + ";)Z").get(0)));
		
		for (MethodNode m : cn.methods()) {
			if (m.name.equals("toString")) {
				InstructionSearcher is = new InstructionSearcher(m.instructions, new int[] { GETFIELD });
				if (is.search()) {
					for (int i = 0; i < is.size(); i++) {
						FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(i)[0];
						addField(fieldHooks[i].buildObf(fin));
					}
				}
			}
		}
		
		for (MethodNode m : InsnUtil.methods(cn, "(DDDDDD)L" + cn.name + ";")) {
			if ((m.access & ACC_STATIC) != 0) {
				addMethod(methodHooks[0].buildObfMethod(m));
			} else {
				InstructionSearcher is = new InstructionSearcher(m.instructions, SET_BOUNDS_REGEX);
				if (is.search()) {
					addMethod(methodHooks[1].buildObfMethod(m));
				}
			}
		}
		
		for (MethodNode m : InsnUtil.methods(cn, "(DDD)L" + cn.name + ";")) {
			if (!methodHooks[2].isIdentified()) {
				InstructionSearcher is = new InstructionSearcher(m.instructions, ADD_CHORD_REGEX);
				if (is.search()) {
					addMethod(methodHooks[2].buildObfMethod(m));
				}
			}
			if (!methodHooks[3].isIdentified()) {
				InstructionSearcher is = new InstructionSearcher(m.instructions, EXPAND_REGEX);
				if (is.search()) {
					addMethod(methodHooks[3].buildObfMethod(m));
				}
			}
			if (!methodHooks[4].isIdentified()) {
				InstructionSearcher is = new InstructionSearcher(m.instructions, GET_OFFSETED_BOX__REGEX);
				if (is.search()) {
					addMethod(methodHooks[4].buildObfMethod(m));
				}
			}
			if (!methodHooks[6].isIdentified()) {
				InstructionSearcher is = new InstructionSearcher(m.instructions, OFFSET_REGEX);
				if (is.search()) {
					addMethod(methodHooks[6].buildObfMethod(m));
				}
			}
			if (!methodHooks[7].isIdentified()) {
				InstructionSearcher is = new InstructionSearcher(m.instructions, CONTRACT_REGEX);
				if (is.search()) {
					addMethod(methodHooks[7].buildObfMethod(m));
				}
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB");
	}
}
