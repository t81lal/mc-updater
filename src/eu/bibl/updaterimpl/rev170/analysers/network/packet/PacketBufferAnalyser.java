package eu.bibl.updaterimpl.rev170.analysers.network.packet;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class PacketBufferAnalyser extends Analyser {
	
	public PacketBufferAnalyser(ClassContainer container, HookMap hookMap) {
		super("PacketBuffer", container, hookMap);
		fieldHooks = new FieldMappingData[] { /* 0 */new FieldMappingData(new MappingData("getPacketBuffer"), new MappingData("Lio/netty/buffer/ByteBuf;", "Lio/netty/buffer/ByteBuf;"), false), };
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "VarInt too big");
	}
	
	@Override
	public InterfaceMappingData run() {
		addField(fieldHooks[0].buildObf(InsnUtil.fields(cn, "Lio/netty/buffer/ByteBuf;").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "network/packet/IPacketBuffer");
	}
}
