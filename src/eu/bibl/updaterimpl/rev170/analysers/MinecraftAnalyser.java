package eu.bibl.updaterimpl.rev170.analysers;

import eu.bibl.banalysis.analyse.single.SingleAnalyser;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.util.Access;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class MinecraftAnalyser extends SingleAnalyser {

	public MinecraftAnalyser() {
		super("Minecraft");
		hooks = new FieldHook[] {
				new FieldHook("getLocalPlayer", "L" + INTERFACES + "entity/IEntityClientPlayerMP;"),
				new FieldHook("getMemoryReserve", "[B", "[B"),
				new FieldHook("getWorldTypes", "[L" + INTERFACES + "world/IWorldType;"),
				new FieldHook("getDefaultWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldHook("getFlatWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldHook("getLargeBiomesWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldHook("getAmplifiedWorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldHook("getDefault1_1WorldType", "L" + INTERFACES + "world/IWorldType;"),
				new FieldHook("getUseEntityActions", "[L" + INTERFACES + "entity/IUseEntityAction;"),
				new FieldHook("getChatVisibilityTypes", "[L" + INTERFACES + "chat/IChatVisibility;"),
				new FieldHook("getDifficultyModes", "[L" + INTERFACES + "world/IDifficulty;"),
				new FieldHook("getClientStates", "[L" + INTERFACES + "client/IClientState;"),
				new FieldHook("getServerStatusInfoGson", "Lcom/google/gson/Gson;", "Lcom/google/gson/Gson;"),
				new FieldHook("inFireDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("onFireDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("lavaDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("inWallDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("drowningDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("starvingDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("cactusDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("fallingDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("outOfWorldDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("genericDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("magicDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("witherDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("anvilDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("fallingBlockDamageSource", "L" + INTERFACES + "entity/combat/IDamageSource;"),
				new FieldHook("getMCProfiler", "L" + INTERFACES + "client/profiler/IProfiler;") };
		methodHooks = new MethodHook[] {
				new MethodHook("runtick", "()V", "()V"),
				new MethodHook("getItemID", "(L" + INTERFACES + "item/IItem;)I"),
				new MethodHook("shutdown", "()V", "()V") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode lin = (LdcInsnNode) ain;
						Object o = lin.cst;
						if (o != null) {
							if (o.toString().equals("10485760")) {
								classHook.setObfuscatedName(cn.name);
								addHook(hooks[1].buildObfFin(((FieldInsnNode) ain.getNext().getNext())));
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "IMinecraft"));
		
		findRuntickMethod();
		findLocalPlayer();
	}
	
	private void findLocalPlayer() {
		ecpmpFinder: for(MethodNode mNode : methods(cn)) {
			if (!mNode.desc.endsWith(")V"))
				continue;
			boolean found3553Int = false;
			boolean foundDisplayLdc = false;
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (!foundDisplayLdc) {
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst.toString().equals("display")) {
								foundDisplayLdc = true;
							}
						}
					}
				} else {
					if (found3553Int) {
						if (ain instanceof FieldInsnNode) {
							FieldInsnNode fin = (FieldInsnNode) ain;
							String pmpcName = fin.desc.substring(1, fin.desc.length()).replace(";", "");
							map.addClass(new ClassHook(pmpcName, "EntityClientPlayerMP"));
							addHook(hooks[0].buildObfFin(fin)); // localPlayer
							break ecpmpFinder;
						}
					} else {
						if (ain instanceof IntInsnNode) {
							IntInsnNode iin = (IntInsnNode) ain;
							if (iin.operand == 3553) {
								found3553Int = true;
							}
						}
					}
				}
			}
		}
	}
	
	private void findRuntickMethod() {
		runtickLoop: for(MethodNode mNode : methods(cn)) {
			if (!mNode.desc.endsWith(")V"))
				continue;
			if (Access.isPublic(mNode.access)) {
				boolean guiCheck = false;
				boolean pickCheck = false;
				boolean gameModeCheck = false;
				boolean texturesCheck = false;
				ListIterator<?> it = mNode.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (guiCheck && pickCheck && gameModeCheck && texturesCheck) {
						addHook(methodHooks[0].buildObfMn(mNode)); // runtick
						break runtickLoop;
					}
					if (ain instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) ain;
						if (ldc.cst != null) {
							if (ldc.cst instanceof String) {
								if (ldc.cst.toString().equals("gui")) {
									guiCheck = true;
								} else if (ldc.cst.toString().equals("pick")) {
									pickCheck = true;
								} else if (ldc.cst.toString().equals("gameMode")) {
									gameModeCheck = true;
								} else if (ldc.cst.toString().equals("textures")) {
									texturesCheck = true;
								}
							}
						}
					}
				}
			}
		}
	}
}