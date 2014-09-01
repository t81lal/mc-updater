package eu.bibl.updaterimpl.rev170.analysers.chat;

import org.objectweb.asm.Opcodes;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ChatVisibilityAnalyser extends Analyser {
	
	public ChatVisibilityAnalyser(ClassContainer container, HookMap hookMap) {
		super("ChatVisibility", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return ((cn.access & Opcodes.ACC_ENUM) != 0) && InsnUtil.containsLdc(cn, "FULL");
	}
	
	@Override
	public InterfaceMappingData run() {
		MinecraftAnalyser analyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
		analyser.addField(analyser.getFieldHooks()[9].buildObf(InsnUtil.fields(cn, "[L" + cn.name + ";").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "chat/IChatVisibility");
	}
}
