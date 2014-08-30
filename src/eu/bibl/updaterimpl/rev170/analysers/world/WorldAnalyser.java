package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.pattern.InsnSearcher;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.client.profiler.ProfilerAnalyser;
import eu.bibl.updaterimpl.rev170.analysers.entity.EntityAnalyser;

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
	
	public WorldAnalyser() {
		super("World");
		hooks = new FieldHook[] {
				new FieldHook("getLoadedEntities", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldHook("getUnloadedEntites", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldHook("getPlayerEntities", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldHook("getWeatherEntities", "Ljava/util/List;", "Ljava/util/List;") };
		methodHooks = new MethodHook[] { new MethodHook("addWeatherEffect", "(L" + INTERFACES + "entity/IEntity;)Z"),
		
		};
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("World").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/IWorld"));
		
		all: for(MethodNode m : methods(cn)) {
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst.equals("remove")) {
						FieldInsnNode fin = (FieldInsnNode) getNext(ain, GETFIELD);
						if (fin.desc.equals("Ljava/util/List;")) {
							addHook(hooks[0].buildObfFin(fin));
							FieldInsnNode fin2 = (FieldInsnNode) getNext(fin.getNext(), GETFIELD);
							addHook(hooks[1].buildObfFin(fin2));
							MethodInsnNode profiler = (MethodInsnNode) getNext(ain, INVOKEVIRTUAL);
							map.addClass(new ClassHook(profiler.owner, "Profiler"));
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
						addHook(hooks[2].buildObfFin(fin));
						break;
					}
				}
			}
		}
		
		for(MethodNode m : methods(cn)) {
			InsnSearcher is = new InsnSearcher(m.instructions, 0, WEATHER_REGEX);
			if (is.match()) {
				FieldInsnNode fin = (FieldInsnNode) is.getMatches().get(0)[1];
				addHook(hooks[3].buildObfFin(fin));
				addHook(methodHooks[0].buildObfMn(m));
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