package eu.bibl.updaterimpl.rev170.analysers.network.packet;

import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.asm.ClassNode;
import eu.bibl.banalysis.storage.CallbackMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.classes.ClassContainer;

public abstract class PacketBaseAnalyser extends Analyser {
	
	protected static final int[] PATTERN = new int[] {
			ALOAD,
			ALOAD,
			CHECKCAST,
			INVOKEVIRTUAL };
	
	public PacketBaseAnalyser(String name, ClassContainer container, HookMap hookMap) {
		super(name, container, hookMap);
	}
	
	@Override
	public void run(ClassNode cn) {
		this.cn = cn;
		if (accept1() && accept()) {
			classHook.setObfuscatedName(cn.name);
			run();
			hookMap.addClass(classHook);
		}
	}
	
	public boolean accept1() {
		return hookMap.getClassByRefactoredName("Packet").getObfuscatedName().equals(cn.superName);
	}
	
	@Override
	public abstract boolean accept();
	
	public MethodNode getReadMethod(ClassNode cn) {
		PacketAnalyser analyser = (PacketAnalyser) AnalyserCache.contextGet("Packet");
		CallbackMappingData read = analyser.getMethodHooks()[0];
		for (MethodNode m : cn.methods()) {
			if (read.getMethodName().getObfuscatedName().equals(m.name) && read.getMethodDesc().getObfuscatedName().equals(m.desc))
				return m;
		}
		return null;
	}
	
	public MethodNode getWriteMethod(ClassNode cn) {
		PacketAnalyser analyser = (PacketAnalyser) AnalyserCache.contextGet("Packet");
		CallbackMappingData read = analyser.getMethodHooks()[1];
		for (MethodNode m : cn.methods()) {
			if (read.getMethodName().getObfuscatedName().equals(m.name) && read.getMethodDesc().getObfuscatedName().equals(m.desc))
				return m;
		}
		return null;
	}
}
