package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class WorldInfoAnalyser extends Analyser {

	public WorldInfoAnalyser() {
		super("WorldInfo");
		hooks = new FieldHook[] { 
			new FieldHook("getRandomSeed", "J", "J"), 
			new FieldHook("getTerrainType", "L" + INTERFACES + "world/IWorldType;"), 
			new FieldHook("getGeneratorOptions", "Ljava/lang/String;", "Ljava/lang/String;"), 
			new FieldHook("hasMapFeatures", "Z", "Z"), 
			new FieldHook("getSpawnX", "I", "I"), 
			new FieldHook("getSpawnY", "I", "I"), 
			new FieldHook("getSpawnZ", "I", "I"), 
			new FieldHook("getWorldLife", "J", "J"), 
			new FieldHook("getWorldTime", "J", "J"), 
			new FieldHook("getSizeOnDisk", "J", "J"), 
			new FieldHook("getLevelName", "Ljava/lang/String;", "Ljava/lang/String;"), 
			new FieldHook("getVersion", "I", "I"), 
			new FieldHook("getRainTime", "I", "I"), 
			new FieldHook("isRaining", "Z", "Z"), 
			new FieldHook("getThunderTime", "I", "I"), 
			new FieldHook("isThundering", "Z", "Z"), 
			new FieldHook("isHardcore", "Z", "Z"), 
			new FieldHook("allowCommands", "Z", "Z"), 
			new FieldHook("isInitialised", "Z", "Z"), 
			new FieldHook("getGameRules", "L" + INTERFACES + "client/IGameRules;"), 
		};
	}

	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "RandomSeed");
	}

	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/IWorldInfo"));

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
						addHook(hooks[0].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("generatorName")) {
						addHook(hooks[1].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("generatorOptions")) {
						addHook(hooks[2].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("MapFeatures")) {
						addHook(hooks[3].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnX")) {
						addHook(hooks[4].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnY")) {
						addHook(hooks[5].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SpawnZ")) {
						addHook(hooks[6].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("Time")) {
						addHook(hooks[7].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("DayTime")) {
						addHook(hooks[8].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("SizeOnDisk")) {
						addHook(hooks[9].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("LevelName")) {
						addHook(hooks[10].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("version")) {
						addHook(hooks[11].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("rainTime")) {
						addHook(hooks[12].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("raining")) {
						addHook(hooks[13].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("thunderTime")) {
						addHook(hooks[14].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("thundering")) {
						addHook(hooks[15].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("hardcore")) {
						addHook(hooks[16].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("allowCommands")) {
						addHook(hooks[17].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("initialized")) {
						addHook(hooks[18].buildObfFin((FieldInsnNode) getNext(ldc, GETFIELD)));
					} else if (cst.equals("GameRules")) {
						FieldInsnNode fin = (FieldInsnNode) getNext(ldc, GETFIELD);
						String gameRules = Type.getReturnType(fin.desc).getClassName();
						map.addClass(new ClassHook(gameRules, "GameRules"));
						addHook(hooks[19].buildObfFin(fin));
					}
				}
			}
		}
	}
}