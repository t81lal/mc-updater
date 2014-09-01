package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class WorldInfoAnalyser extends Analyser {
	public WorldInfoAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldInfo", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getRandomSeed"), new MappingData("J", "J"), false),
				/* 1 */new FieldMappingData(new MappingData("getTerrainType"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "world/IWorldType;"), false),
				/* 2 */new FieldMappingData(new MappingData("getGeneratorOptions"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false),
				/* 3 */new FieldMappingData(new MappingData("hasMapFeatures"), new MappingData("Z", "Z"), false),
				/* 4 */new FieldMappingData(new MappingData("getSpawnX"), new MappingData("I", "I"), false),
				/* 5 */new FieldMappingData(new MappingData("getSpawnY"), new MappingData("I", "I"), false),
				/* 6 */new FieldMappingData(new MappingData("getSpawnZ"), new MappingData("I", "I"), false),
				/* 7 */new FieldMappingData(new MappingData("getWorldLife"), new MappingData("J", "J"), false),
				/* 8 */new FieldMappingData(new MappingData("getWorldTime"), new MappingData("J", "J"), false),
				/* 9 */new FieldMappingData(new MappingData("getSizeOnDisk"), new MappingData("J", "J"), false),
				/* 10 */new FieldMappingData(new MappingData("getLevelName"), new MappingData("Ljava/lang/String;", "Ljava/lang/String;"), false),
				/* 11 */new FieldMappingData(new MappingData("getVersion"), new MappingData("I", "I"), false),
				/* 12 */new FieldMappingData(new MappingData("getRainTime"), new MappingData("I", "I"), false),
				/* 13 */new FieldMappingData(new MappingData("isRaining"), new MappingData("Z", "Z"), false),
				/* 14 */new FieldMappingData(new MappingData("getThunderTime"), new MappingData("I", "I"), false),
				/* 15 */new FieldMappingData(new MappingData("isThundering"), new MappingData("Z", "Z"), false),
				/* 16 */new FieldMappingData(new MappingData("isHardcore"), new MappingData("Z", "Z"), false),
				/* 17 */new FieldMappingData(new MappingData("allowCommands"), new MappingData("Z", "Z"), false),
				/* 18 */new FieldMappingData(new MappingData("isInitialised"), new MappingData("Z", "Z"), false),
				/* 19 */new FieldMappingData(new MappingData("getGameRules"), new MappingData("L" + MinecraftAnalyser.INTERFACES + "client/IGameRules;"), false), };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "RandomSeed");
	}
	
	@Override
	public InterfaceMappingData run() {
		methods: for (MethodNode m : cn.methods()) {
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
						addField(fieldHooks[0].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("generatorName")) {
						addField(fieldHooks[1].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("generatorOptions")) {
						addField(fieldHooks[2].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("MapFeatures")) {
						addField(fieldHooks[3].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnX")) {
						addField(fieldHooks[4].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnY")) {
						addField(fieldHooks[5].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnZ")) {
						addField(fieldHooks[6].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("Time")) {
						addField(fieldHooks[7].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("DayTime")) {
						addField(fieldHooks[8].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("SizeOnDisk")) {
						addField(fieldHooks[9].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("LevelName")) {
						addField(fieldHooks[10].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("version")) {
						addField(fieldHooks[11].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("rainTime")) {
						addField(fieldHooks[12].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("raining")) {
						addField(fieldHooks[13].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("thunderTime")) {
						addField(fieldHooks[14].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("thundering")) {
						addField(fieldHooks[15].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("hardcore")) {
						addField(fieldHooks[16].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("allowCommands")) {
						addField(fieldHooks[17].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("initialized")) {
						addField(fieldHooks[18].buildObf((FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD)));
					} else if (cst.equals("GameRules")) {
						FieldInsnNode fin = (FieldInsnNode) InsnUtil.getNext(ldc, GETFIELD);
						String gameRules = Type.getReturnType(fin.desc).getClassName();
						hookMap.addClass(new ClassMappingData(gameRules, "GameRules", null));
						addField(fieldHooks[19].buildObf(fin));
					}
				}
			}
		}
		
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorldInfo");
	}
}
