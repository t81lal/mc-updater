package eu.bibl.updaterimpl.rev170.analysers.world;
public class WorldAnalyser extends Analyser {
	
	private static final int[] REMOVE_ENTITY_REGEX = new int[] {
			ALOAD,
			INVOKEVIRTUAL,
			ALOAD,
			INSTANCEOF,
			IFEQ };
	
	private static final int[] WEATHER_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			ALOAD,
			INVOKEINTERFACE,
			POP,
			ICONST_1 };
	
	private static final int[] SPAWN_ENTITY_REGEX = new int[] {
			ALOAD,
			GETFIELD,
			LDC,
			DDIV,
			INVOKESTATIC,
			ISTORE,
			ALOAD,
			GETFIELD,
			LDC,
			DDIV,
			INVOKESTATIC,
			ISTORE,
			ALOAD,
			GETFIELD,
			ISTORE };
	
	public WorldAnalyser(ClassContainer container, HookMap hookMap) {
		super("World", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getLoadedEntities", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldMappingData("getUnloadedEntites", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldMappingData("getPlayerEntities", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldMappingData("getWeatherEntities", "Ljava/util/List;", "Ljava/util/List;") };
		methodHooks = new CallbackMappingData[] { new CallbackMappingData("addWeatherEffect", "(L" + MinecraftAnalyser.INTERFACES + "entity/IEntity;)Z"),
		
		};
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("World").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorld"));
		
		all: for(MethodNode m : methods(cn)) {
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst.equals("remove")) {
						FieldInsnNode fin = (FieldInsnNode) getNext(ain, GETFIELD);
						if (fin.desc.equals("Ljava/util/List;")) {
							addFieldHook(fieldHooks[0].buildObfFin(fin));
							FieldInsnNode fin2 = (FieldInsnNode) getNext(fin.getNext(), GETFIELD);
							addFieldHook(fieldHooks[1].buildObfFin(fin2));
							MethodInsnNode profiler = (MethodInsnNode) getNext(ain, INVOKEVIRTUAL);
							hookMap.addClass(new ClassMappingData(profiler.owner, "Profiler"));
							ProfilerAnalyser profilerAnalyser = (ProfilerAnalyser) analysers.get("Profiler");
							profilerAnalyser.findEndStart(profiler);
							break all;
						}
					}
				}
			}
		}
		
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, REMOVE_ENTITY_REGEX);
			if (is.match()) {
				AbstractInsnNode ain = is.getMatches().get(0)[4];
				AbstractInsnNode ain2 = ain.getNext().getNext();
				if (ain2.getOpcode() == GETFIELD) {
					FieldInsnNode fin = (FieldInsnNode) ain2;
					if (fin.desc.equals("Ljava/util/List;")) {
						addFieldHook(fieldHooks[2].buildObfFin(fin));
						break;
					}
				}
			}
		}
		
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, WEATHER_REGEX);
			if (is.match()) {
				FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[1];
				addFieldHook(fieldHooks[3].buildObfFin(fin));
				addMethodHook(methodHooks[0].buildObfMn(m));
				break;
			}
		}
		
		for(MethodNode m : methods(cn)) {
			if (Type.getArgumentTypes(m.desc).length != 1)
				continue;
			InsnSearcher is = new InsnSearcher(m.instructions, 0, SPAWN_ENTITY_REGEX);
			if (is.match()) {
				FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[13];
				EntityAnalyser analyser = (EntityAnalyser) analysers.get("Entity");
				analyser.getHooks()[37].buildObfFin(fin);
				break;
			}
		}
	}
}
