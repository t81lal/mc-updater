package eu.bibl.updaterimpl.rev170.analysers.world;
public class WorldInfoAnalyser extends Analyser {
	public WorldInfoAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldInfo", container, hookMap);
		fieldHooks = new FieldMappingData[] { 
			new FieldMappingData("getRandomSeed", "J", "J"), 
			new FieldMappingData("getTerrainType", "L" + MinecraftAnalyser.INTERFACES + "world/IWorldType;"), 
			new FieldMappingData("getGeneratorOptions", "Ljava/lang/String;", "Ljava/lang/String;"), 
			new FieldMappingData("hasMapFeatures", "Z", "Z"), 
			new FieldMappingData("getSpawnX", "I", "I"), 
			new FieldMappingData("getSpawnY", "I", "I"), 
			new FieldMappingData("getSpawnZ", "I", "I"), 
			new FieldMappingData("getWorldLife", "J", "J"), 
			new FieldMappingData("getWorldTime", "J", "J"), 
			new FieldMappingData("getSizeOnDisk", "J", "J"), 
			new FieldMappingData("getLevelName", "Ljava/lang/String;", "Ljava/lang/String;"), 
			new FieldMappingData("getVersion", "I", "I"), 
			new FieldMappingData("getRainTime", "I", "I"), 
			new FieldMappingData("isRaining", "Z", "Z"), 
			new FieldMappingData("getThunderTime", "I", "I"), 
			new FieldMappingData("isThundering", "Z", "Z"), 
			new FieldMappingData("isHardcore", "Z", "Z"), 
			new FieldMappingData("allowCommands", "Z", "Z"), 
			new FieldMappingData("isInitialised", "Z", "Z"), 
			new FieldMappingData("getGameRules", "L" + MinecraftAnalyser.INTERFACES + "client/IGameRules;"), 
		};
	}
	@Override
public boolean accept() {
		return containsLdc(cn, "RandomSeed");
	}
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorldInfo"));
		methods: for (MethodNode m : methods(cn)) {
			if (m.name.equals("<init>") || m.name.equals("<clinit>"))
				continue;
			boolean found = false;
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (!found) {
						if (ldc.cst.equals("RandomSeed")) {
							found = true;
						} else {
							continue methods;
						}
					}
					String cst = ldc.cst.toString();
					if (cst.equals("RandomSeed")) {
						addFieldHook(fieldHooks[0].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("generatorName")) {
						addFieldHook(fieldHooks[1].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("generatorOptions")) {
						addFieldHook(fieldHooks[2].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("MapFeatures")) {
						addFieldHook(fieldHooks[3].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnX")) {
						addFieldHook(fieldHooks[4].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnY")) {
						addFieldHook(fieldHooks[5].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnZ")) {
						addFieldHook(fieldHooks[6].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("Time")) {
						addFieldHook(fieldHooks[7].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("DayTime")) {
						addFieldHook(fieldHooks[8].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SizeOnDisk")) {
						addFieldHook(fieldHooks[9].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("LevelName")) {
						addFieldHook(fieldHooks[10].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("version")) {
						addFieldHook(fieldHooks[11].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("rainTime")) {
						addFieldHook(fieldHooks[12].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("raining")) {
						addFieldHook(fieldHooks[13].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("thunderTime")) {
						addFieldHook(fieldHooks[14].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("thundering")) {
						addFieldHook(fieldHooks[15].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("hardcore")) {
						addFieldHook(fieldHooks[16].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("allowCommands")) {
						addFieldHook(fieldHooks[17].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("initialized")) {
						addFieldHook(fieldHooks[18].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("GameRules")) {
						FieldInsnNode fin = (FieldInsnNode) getNext(ldc, GETFIELD);
						String gameRules = Type.getReturnType(fin.desc).getClassName();
						hookMap.addClass(new ClassMappingData(gameRules, "GameRules"));
						addFieldHook(fieldHooks[19].buildObfFin(fin));
					}
				}
			}
		}
	}
}
