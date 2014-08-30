package eu.bibl.updater.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public abstract class Analyser extends eu.bibl.bytetools.analysis.Analyser {
	
	public static String INTERFACES = "eu/bibl/mc/accessors/";
	
	public static HashMap<String, Analyser> analysers = new HashMap<String, Analyser>();
	
	protected String name;
	protected AnalysisMap analysisMap;
	protected FieldHook[] hooks;
	protected MethodHook[] methodHooks;
	protected ClassHook classHook;
	
	public Analyser(String name) {
		name = name.replace("Analyser", "");
		this.name = name;
		classHook = new ClassHook(name);
		analysers.put(name, this);
		hooks = new FieldHook[0];
		methodHooks = new MethodHook[0];
	}
	
	@Override
	public void checkAnalyser(ClassNode cn) {
		if (accept(cn)) {
			this.cn = cn;
			classHook.setObfuscatedName(cn.name);
			run();
			map.addClass(classHook);
		}
	}
	
	public void setAnalysisMap(AnalysisMap analysisMap) {
		this.analysisMap = analysisMap;
	}
	
	public void addHook(FieldHook hook) {
		if (classHook != null)
			hook.setOwner(classHook);
		map.addFieldHook(hook);
	}
	
	public void addHook(MethodHook hook) {
		if (classHook != null)
			hook.setOwner(classHook);
		map.addMethodHook(hook);
	}
	
	public void addHook(FieldHook hook, boolean b) {
		if (b && classHook != null)
			hook.setOwner(classHook);
		map.addFieldHook(hook);
	}
	
	public void addHook(MethodHook hook, boolean b) {
		if (b && classHook != null)
			hook.setOwner(classHook);
		map.addMethodHook(hook);
	}
	
	public void addMinecraftHook(FieldHook hook) {
		addMinecraftHook(hook, cn.name);
	}
	
	public void addMinecraftHook(FieldHook hook, String localName) {
		hook.setOwner(createHook("Minecraft$" + localName, "Minecraft$" + localName));
		MinecraftAnalyser mcAnalyser = (MinecraftAnalyser) analysers.get("Minecraft");
		mcAnalyser.map.addFieldHook(hook);
	}
	
	public void addMinecraftHook(MethodHook hook) {
		addMinecraftHook(hook, cn.name);
	}
	
	public void addMinecraftHook(MethodHook hook, String localName) {
		hook.setOwner(createHook("Minecraft$" + localName, "Minecraft$" + localName));
		MinecraftAnalyser mcAnalyser = (MinecraftAnalyser) analysers.get("Minecraft");
		mcAnalyser.map.addMethodHook(hook);
	}
	
	private ClassHook createHook(String obf, String refac) {
		if (map.containsObfuscatedClassName(obf))
			return map.getClassByObfuscatedName(obf);
		return new ClassHook(obf, refac);
	}
	
	public FieldHook[] getHooks() {
		return hooks;
	}
	
	public MethodHook[] getMethodHooks() {
		return methodHooks;
	}
	
	public String getName() {
		return name;
	}
	
	public ClassHook getClassHook() {
		return classHook;
	}
	
	public boolean containsLdc(ClassNode cn, Object cst) {
		for(MethodNode m : methods(cn)) {
			if (containsLdc(m, cst))
				return true;
		}
		return false;
	}
	
	public boolean containsLdc(MethodNode m, Object cst) {
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain instanceof LdcInsnNode) {
				LdcInsnNode ldc = (LdcInsnNode) ain;
				if (ldc.cst.equals(cst))
					return true;
			}
		}
		return false;
	}
	
	public AbstractInsnNode getNext(AbstractInsnNode ain, int opcode) {
		while (ain != null) {
			if (ain.getOpcode() == opcode) {
				return ain;
			} else {
				ain = ain.getNext();
			}
		}
		return null;
	}
	
	public AbstractInsnNode[] getNodes(MethodNode m, int opcode) {
		ArrayList<AbstractInsnNode> ains = new ArrayList<AbstractInsnNode>();
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain.getOpcode() == opcode)
				ains.add(ain);
		}
		return ains.toArray(new AbstractInsnNode[ains.size()]);
	}
	
	public FieldInsnNode[] getFieldNodes(MethodNode m, int opcode) {
		ArrayList<FieldInsnNode> ains = new ArrayList<FieldInsnNode>();
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain.getOpcode() == opcode)
				ains.add((FieldInsnNode) ain);
		}
		return ains.toArray(new FieldInsnNode[ains.size()]);
	}
	
	public MethodInsnNode[] getMethodNodes(MethodNode m, int opcode) {
		ArrayList<MethodInsnNode> ains = new ArrayList<MethodInsnNode>();
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain.getOpcode() == opcode)
				ains.add((MethodInsnNode) ain);
		}
		return ains.toArray(new MethodInsnNode[ains.size()]);
	}
	
	public MethodNode getMethod(MethodHook m) {
		return getMethod(m.getObfuscatedName(), m.getObfuscatedDesc());
	}
	
	public MethodNode getMethod(MethodInsnNode min) {
		return getMethod(min.name, min.desc);
	}
	
	public MethodNode getMethod(String name, String desc) {
		for(MethodNode m : methods(cn)) {
			if (m.name.equals(name) && m.desc.equals(desc))
				return m;
		}
		return null;
	}
	
	public AbstractInsnNode getPrev(AbstractInsnNode ain, int opcode) {
		while (ain != null) {
			if (ain.getOpcode() == opcode) {
				return ain;
			} else {
				ain = ain.getPrevious();
			}
		}
		return null;
	}
}