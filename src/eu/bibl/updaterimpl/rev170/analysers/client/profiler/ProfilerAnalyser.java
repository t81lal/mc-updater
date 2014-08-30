package eu.bibl.updaterimpl.rev170.analysers.client.profiler;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.classattr.InsnVector;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ProfilerAnalyser extends Analyser {
	
	private MethodInsnNode startEndMin;
	
	public ProfilerAnalyser() {
		super("Profiler");
		methodHooks = new MethodHook[] {
				new MethodHook("endStartSection", "(Ljava/lang/String;)V", "(Ljava/lang/String;)V"),
				new MethodHook("endSection", "()V", "()V"),
				new MethodHook("startSection", "(Ljava/lang/String;)V", "(Ljava/lang/String;)V"),
				new MethodHook("clear", "()V", "()V") };
		hooks = new FieldHook[] {
				new FieldHook("isProfilingEnabled", "Z", "Z"),
				new FieldHook("getTimestampList", "Ljava/uti/List;", "Ljava/util/List;"),
				new FieldHook("getSectionList", "Ljava/uti/List;", "Ljava/util/List;"),
				new FieldHook("getProfilingMap", "Ljava/util/Map;", "Ljava/util/Map;"),
				new FieldHook("getSectionName", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook hook = map.getClassByRefactoredName("Profiler");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "client/profiler/IProfiler"));
		addHook(methodHooks[0].buildObfMin(startEndMin));
		for(MethodNode m : methods(cn, startEndMin.desc)) {
			if (m.name.equals(startEndMin.name)) {
				InsnVector<MethodInsnNode> vector = new InsnVector<MethodInsnNode>(m, MethodInsnNode.class);
				MethodInsnNode end = vector.getInsn(0);
				MethodInsnNode start = vector.getInsn(1);
				addHook(methodHooks[1].buildObfMin(end));
				addHook(methodHooks[2].buildObfMin(start));
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
						addHook(methodHooks[3].buildObfMn(m).identify());
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
				
				addHook(hooks[0].buildObfFin(enabled));
				addHook(hooks[1].buildObfFin(ts));
				addHook(hooks[2].buildObfFin(sl));
				addHook(hooks[3].buildObfFin(pm));
				addHook(hooks[4].buildObfFin(sec));
			}
		}
		
		for(FieldNode f : fields(analysisMap.requestNode(map.getClassByRefactoredName("Minecraft").getObfuscatedName()))) {
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