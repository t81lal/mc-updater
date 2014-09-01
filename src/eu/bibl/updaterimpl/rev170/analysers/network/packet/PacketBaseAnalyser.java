package eu.bibl.updaterimpl.rev170.analysers.network.packet;
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
			hookMap.addClass(classHook);
		}
	}
	
	public boolean accept1(ClassNode cn) {
		return hookMap.getClassByRefactoredName("Packet").getObfuscatedName().equals(cn.superName);
	}
	
	@Override
	public abstract boolean accept(ClassNode cn);
	
	public MethodNode getReadMethod(ClassNode cn) {
		PacketAnalyser analyser = (PacketAnalyser) analysers.get("Packet");
		CallbackMappingData read = analyser.getCallbackMappingDatas()[0];
		for(MethodNode m : methods(cn)) {
			if (read.getObfuscatedName().equals(m.name) && read.getObfuscatedDesc().equals(m.desc))
				return m;
		}
		return null;
	}
	
	public MethodNode getWriteMethod(ClassNode cn) {
		PacketAnalyser analyser = (PacketAnalyser) analysers.get("Packet");
		CallbackMappingData read = analyser.getCallbackMappingDatas()[1];
		for(MethodNode m : methods(cn)) {
			if (read.getObfuscatedName().equals(m.name) && read.getObfuscatedDesc().equals(m.desc))
				return m;
		}
		return null;
	}
}
