package eu.bibl.updaterimpl.rev170.analysers.network.packet;
public class PacketAnalyser extends Analyser {
	
	public PacketAnalyser(ClassContainer container, HookMap hookMap) {
		super("Packet", container, hookMap);
		methodHooks = new CallbackMappingData[] {
				new CallbackMappingData("readPacket", "(L" + MinecraftAnalyser.INTERFACES + "network/packet/IPacketBuffer;)V"),
				new CallbackMappingData("writePacket", "(L" + MinecraftAnalyser.INTERFACES + "network/packet/IPacketBuffer;)V") };
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "Key was smaller than nothing!  Weird key!");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/IPacket"));
		
		int methods = 0;
		for(MethodNode m : methods(cn, "(L" + hookMap.getClassByRefactoredName("PacketBuffer").getObfuscatedName() + ";)V")) {
			if (!Access.isAbstract(m.access))
				continue;
			addMethodHook(methodHooks[methods++].buildObfMn(m));
		}
	}
}
