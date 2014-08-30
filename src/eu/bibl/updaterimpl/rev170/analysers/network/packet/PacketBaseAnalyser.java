package eu.bibl.updaterimpl.rev170.analysers.network.packet;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updater.analysis.Analyser;

public abstract class PacketBaseAnalyser extends Analyser {
	
	protected static final int[] PATTERN = new int[] { ALOAD, ALOAD, CHECKCAST, INVOKEVIRTUAL };
	
	public PacketBaseAnalyser(String name) {
		super(name);
	}
	
	@Override
	public void checkAnalyser(ClassNode cn) {
		if (accept1(cn) && accept(cn)) {
			this.cn = cn;
			classHook.setObfuscatedName(cn.name);
			run();
			map.addClass(classHook);
		}
	}
	
	public boolean accept1(ClassNode cn) {
		return map.getClassByRefactoredName("Packet").getObfuscatedName().equals(cn.superName);
	}
	
	@Override
	public abstract boolean accept(ClassNode cn);
	
	public MethodNode getReadMethod(ClassNode cn) {
		PacketAnalyser analyser = (PacketAnalyser) analysers.get("Packet");
		MethodHook read = analyser.getMethodHooks()[0];
		for(MethodNode m : methods(cn)) {
			if (read.getObfuscatedName().equals(m.name) && read.getObfuscatedDesc().equals(m.desc))
				return m;
		}
		return null;
	}
	
	public MethodNode getWriteMethod(ClassNode cn) {
		PacketAnalyser analyser = (PacketAnalyser) analysers.get("Packet");
		MethodHook read = analyser.getMethodHooks()[1];
		for(MethodNode m : methods(cn)) {
			if (read.getObfuscatedName().equals(m.name) && read.getObfuscatedDesc().equals(m.desc))
				return m;
		}
		return null;
	}
}