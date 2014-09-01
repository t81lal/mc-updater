package eu.bibl.updaterimpl.rev170.analysers.entity;
public class DataWatcherAnalyser extends Analyser{
	public DataWatcherAnalyser(ClassContainer container, HookMap hookMap) {
		super("DataWatcher", container, hookMap);
	}
	@Override
public boolean accept() {
		return containsLdc(cn, "Duplicate id value for ");
	}
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IDataWatcher"));
	}
}
