package eu.bibl.updaterimpl.rev170.analysers.client.profiler;
public class ProfilerAnalyser extends Analyser {
	
	private MethodInsnNode startEndMin;
	
	public ProfilerAnalyser(ClassContainer container, HookMap hookMap) {
		super("Profiler", container, hookMap);
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData("endStartSection", "(Ljava/lang/String;)V", "(Ljava/lang/String;)V"),
				new CallbackMappingData("endSection", "()V", "()V"),
				new CallbackMappingData("startSection", "(Ljava/lang/String;)V", "(Ljava/lang/String;)V"),
				new CallbackMappingData("clear", "()V", "()V") };
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("isProfilingEnabled", "Z", "Z"),
				new FieldMappingData("getTimestampList", "Ljava/uti/List;", "Ljava/util/List;"),
				new FieldMappingData("getSectionList", "Ljava/uti/List;", "Ljava/util/List;"),
				new FieldMappingData("getProfilingMap", "Ljava/util/Map;", "Ljava/util/Map;"),
				new FieldMappingData("getSectionName", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByRefactoredName("Profiler");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/profiler/IProfiler"));
		addMethodHook(methodHooks[0].buildObfMin(startEndMin));
		for(MethodNode m : methods(cn, startEndMin.desc)) {
			if (m.name.equals(startEndMin.name)) {
				InsnVector<MethodInsnNode> vector = new InsnVector<MethodInsnNode>(m, MethodInsnNode.class);
				MethodInsnNode end = vector.getInsn(0);
				MethodInsnNode start = vector.getInsn(1);
				addMethodHook(methodHooks[1].buildObfMin(end));
				addMethodHook(methodHooks[2].buildObfMin(start));
				findFields(end);
			}
		}
		
		all: for(MethodNode m : methods(cn, "()V")) {
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof MethodInsnNode) {
					MethodInsnNode min = (MethodInsnNode) ain;
					if (min.name.equals("clear")) {
						addMethodHook(methodHooks[3].buildObfMn(m).identify());
						break all;
					}
				}
			}
		}
	}
	
	private void findFields(MethodInsnNode min) {
		for(MethodNode m : methods(cn)) {
			if (m.name.equals(min.name) && m.desc.equals(min.desc)) {
				InsnVector<FieldInsnNode> vector = new InsnVector<FieldInsnNode>(m, FieldInsnNode.class);
				FieldInsnNode enabled = vector.getInsn(0);
				FieldInsnNode ts = vector.getInsn(1);
				FieldInsnNode sl = vector.getInsn(3);
				FieldInsnNode pm = vector.getInsn(5);
				FieldInsnNode sec = vector.getInsn(6);
				
				addFieldHook(fieldHooks[0].buildObfFin(enabled));
				addFieldHook(fieldHooks[1].buildObfFin(ts));
				addFieldHook(fieldHooks[2].buildObfFin(sl));
				addFieldHook(fieldHooks[3].buildObfFin(pm));
				addFieldHook(fieldHooks[4].buildObfFin(sec));
			}
		}
		
		for(FieldNode f : fields(analysisMap.requestNode(hookMap.getClassByRefactoredName("Minecraft").getObfuscatedName()))) {
			if (f.desc.equals("L" + cn.name + ";")) {
				MinecraftAnalyser mcAnalyser = (MinecraftAnalyser) analysers.get("Minecraft");
				addMinecraftHook(mcAnalyser.getHooks()[27].buildObfFn(f));
				break;
			}
		}
	}
	
	public void findEndStart(MethodInsnNode min) {
		startEndMin = min;
		classHook.setObfuscatedName(min.owner);
	}
}
