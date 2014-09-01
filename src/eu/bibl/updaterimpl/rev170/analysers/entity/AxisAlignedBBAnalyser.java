package eu.bibl.updaterimpl.rev170.analysers.entity;
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
				new FieldMappingData("getMinX", "D", "D"),
				new FieldMappingData("getMinY", "D", "D"),
				new FieldMappingData("getMinZ", "D", "D"),
				new FieldMappingData("getMaxX", "D", "D"),
				new FieldMappingData("getMaxY", "D", "D"),
				new FieldMappingData("getMaxZ", "D", "D") };
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData("createNewBoundingBox", "(DDDDDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"),
				new CallbackMappingData("setBounds", "(DDDDDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"),
				new CallbackMappingData("addCoord", "(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"),
				new CallbackMappingData("expand", "(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"),
				new CallbackMappingData("getOffsettedBoundingBox", "(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"),
				new CallbackMappingData("intersectsWith", "(L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;)Z"),
				new CallbackMappingData("offset", "(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"),
				new CallbackMappingData("contract", "(DDD)L" + MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB;"), };
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "box[");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IAxisAlignedBB"));
		
		addMethodHook(methodHooks[5].buildObfMn(methods(cn, "(L" + cn.name + ";)Z").get(0)));
		
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("toString")) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, new int[] { GETFIELD });
				if (is.match()) {
					for(int i = 0; i < is.size(); i++) {
						FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(i)[0];
						addFieldHook(fieldHooks[i].buildObfFin(fin));
					}
				}
			}
		}
		
		for(MethodNode m : methods(cn, "(DDDDDD)L" + cn.name + ";")) {
			if (Access.isStatic(m.access)) {
				addMethodHook(methodHooks[0].buildObfMn(m));
			} else {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, SET_BOUNDS_REGEX);
				if (is.match()) {
					addMethodHook(methodHooks[1].buildObfMn(m));
				}
			}
		}
		
		for(MethodNode m : methods(cn, "(DDD)L" + cn.name + ";")) {
			if (!methodHooks[2].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, ADD_CHORD_REGEX);
				if (is.match()) {
					addMethodHook(methodHooks[2].buildObfMn(m));
				}
			}
			if (!methodHooks[3].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, EXPAND_REGEX);
				if (is.match()) {
					addMethodHook(methodHooks[3].buildObfMn(m));
				}
			}
			if (!methodHooks[4].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, GET_OFFSETED_BOX__REGEX);
				if (is.match()) {
					addMethodHook(methodHooks[4].buildObfMn(m));
				}
			}
			if (!methodHooks[6].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, OFFSET_REGEX);
				if (is.match()) {
					addMethodHook(methodHooks[6].buildObfMn(m));
				}
			}
			if (!methodHooks[7].identified()) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, CONTRACT_REGEX);
				if (is.match()) {
					addMethodHook(methodHooks[7].buildObfMn(m));
				}
			}
		}
	}
}
