package eu.bibl.updaterimpl.rev170.analysers.network.packet;
public class PacketBufferAnalyser extends Analyser {
	
	public PacketBufferAnalyser(ClassContainer container, HookMap hookMap) {
		super("PacketBuffer", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getPacketBuffer", "Lio/netty/buffer/ByteBuf;", "Lio/netty/buffer/ByteBuf;") };
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "VarInt too big");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/IPacketBuffer"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Lio/netty/buffer/ByteBuf;").get(0)));
	}
}
