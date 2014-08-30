package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class WorldClientAnalyser extends Analyser {
	
	public WorldClientAnalyser() {
		super("WorldClient");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		for(MethodNode mNode : methods(cn)) {
			if (!mNode.name.equals("<init>"))
				continue;
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst != null) {
						if (ldc.cst.toString().equals("MpServer")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		InterfaceHook mcInterfaceData = new InterfaceHook(classHook, INTERFACES + "world/IWorldClient", INTERFACES + "world/IWorld");
		classHook.setInterfaceHook(mcInterfaceData);
		
		map.addClass(new ClassHook(cn.superName, "World"));
	}
}