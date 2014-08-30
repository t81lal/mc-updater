package eu.bibl.updaterimpl.rev170.analysers.chat;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ChatVisibilityAnalyser extends Analyser {
	
	public ChatVisibilityAnalyser() {
		super("ChatVisibility");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "FULL");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "chat/IChatVisibility"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[9].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
	}
}