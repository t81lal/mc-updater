package eu.bibl.updaterimpl.rev170.analysers.client;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ClientStateAnalyser extends Analyser {
	
	public ClientStateAnalyser() {
		super("ClientState");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "PERFORM_RESPAWN");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "client/IClientState"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[11].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
	}
}