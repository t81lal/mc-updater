package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class UseEntityActionAnalyser extends Analyser {
	
	public UseEntityActionAnalyser() {
		super("UseEntityAction");
		hooks = new FieldHook[] { new FieldHook("getActionID", "I", "I") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "INTERACT");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IUseEntityAction"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[8].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
		
		for(MethodNode m : methods(cn)) {
			if (m.name.equals("<init>")) {
				addHook(hooks[0].buildObfFin((FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD)));
				break;
			}
		}
	}
}