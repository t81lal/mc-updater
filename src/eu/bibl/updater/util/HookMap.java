package eu.bibl.updater.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.ByteTools;
import eu.bibl.bytetools.analysis.storage.hooks.BytecodeMethodHook;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.util.OpcodeInfo;
import eu.bibl.updater.analysis.Analyser;

public class HookMap extends eu.bibl.bytetools.analysis.storage.hooks.HookMap {
	
	public HookMap() {
	}
	
	@Override
	public void addFieldHook(FieldHook hook) {
		super.addFieldHook(hook.identify());
	}
	
	@Override
	public void addMethodHook(MethodHook hook) {
		super.addMethodHook(hook.identify());
	}
	
	public void print(String delim, boolean print, List<Analyser> analyserz, HashMap<String, ClassNode> nodes) {
		HashMap<String, Analyser> analysers = new HashMap<String, Analyser>();
		for(Analyser analyser : analyserz) {
			analyser.setHookMap(this);
			analysers.put(analyser.getName(), analyser);
		}
		
		int ffound = 0;
		int mfound = 0;
		int fexpected = 0;
		int mexpected = 0;
		ArrayList<ClassHook> hooks = getSortedClassHooks();
		for(ClassHook c : hooks) {
			Analyser analyser = analysers.get(c.getRefactoredName());
			String line = "";
			if (analyser != null) {
				int f1 = 0;
				int m1 = 0;
				for(FieldHook hook : analyser.getHooks())
					if (hook.identified())
						f1++;
				for(MethodHook hook : analyser.getMethodHooks())
					if (hook.identified())
						m1++;
				ffound += f1;
				mfound += m1;
				fexpected += analyser.getHooks().length;
				mexpected += analyser.getMethodHooks().length;
				line = " (" + (f1 + m1) + "/" + (analyser.getHooks().length + analyser.getMethodHooks().length) + ")";
			}
			if (print) {
				System.out.println("" + c.getRefactoredName() + " identified as " + c.getObfuscatedName() + line);
				if (c.getInterfaceHook() != null)
					System.out.println(" ^ implements " + c.getInterfaceHook().getInterfaceName());
			}
			for(String s : obfuscatedFields.keySet()) {
				for(FieldHook f : obfuscatedFields.get(s)) {
					String oName = f.getOwner().getObfuscatedName().split("\\$")[0];
					if (s.startsWith(c.getObfuscatedName()) || oName.equals(c.getRefactoredName())) {
						String type = Type.getType(f.getRefactoredDesc()).getClassName();
						if (type.startsWith(delim))
							type = type.substring(type.lastIndexOf('.') + 2);
						if (f.getOwner().getObfuscatedName().contains("$")) {
							String rName = f.getOwner().getRefactoredName().split("\\$")[0];
							if (print)
								System.out.println(" > " + type + " " + rName + "." + f.getRefactoredName() + " returns \'" + getNameSecondPart(f.getOwner().getObfuscatedName()) + "." + f.getObfuscatedName() + "\'");
						} else if (print)
							System.out.println(" > " + type + " " + f.getRefactoredName() + " returns \'" + getNameSecondPart(f.getOwner().getObfuscatedName()) + "." + f.getObfuscatedName() + "\'");
					}
				}
			}
			if (analyser != null)
				for(FieldHook hook : analyser.getHooks())
					if (!hook.identified())
						System.out.println(" > " + hook.getRefactoredName() + " broke.");
			for(String s : obfuscatedMethods.keySet()) {
				for(MethodHook m : obfuscatedMethods.get(s)) {
					String oName = m.getOwner().getObfuscatedName().split("\\$")[0];
					String rName = m.getOwner().getRefactoredName().split("\\$")[0];
					if (s.startsWith(c.getObfuscatedName()) || oName.equals(c.getRefactoredName())) {
						String opt = "";
						if (m instanceof BytecodeMethodHook) {
							opt = " ";
							opt += OpcodeInfo.opcodesToString(((BytecodeMethodHook) m).getInstructions().toArray());
						}
						if (print)
							System.out.println(" > " + rName + "." + m.getRefactoredName() + " " + m.getRefactoredDesc() + " as " + getNameSecondPart(m.getOwner().getObfuscatedName()) + "." + m.getObfuscatedName() + " " + m.getObfuscatedDesc() + opt);
					}
				}
			}
			if (analyser != null)
				for(MethodHook hook : analyser.getMethodHooks())
					if (!hook.identified())
						System.out.println(" > " + hook.getRefactoredName() + " broke.");
			if (print)
				System.out.println("                         ");
		}
		
		// for(Analyser analyser : analysers.values()) {
		// boolean b = false;
		// for(ClassHook hook : hooks)
		// if (hook.getRefactoredName().equals(analyser.getName())) {
		// b = true;
		// break;
		// }
		// if (!b)
		// System.out.println("Couldn't identify " + analyser.getName());
		// }
		// for(ClassHook hook : hooks) {
		// boolean b = false;
		// for(Analyser a : analysers.values()) {
		// if (hook.getRefactoredName().equals(a.getName())) {
		// b = true;
		// break;
		// }
		// }
		// if (!b)
		// System.out.println("Wat: " + hook.getRefactoredName());
		// }
		float fieldPerc = (float) ffound / (float) getTotalFields(nodes) * 100F;
		float methodPerc = (float) mfound / (float) getTotalMethods(nodes) * 100F;
		float classPerc = (float) hooks.size() / (float) nodes.size() * 100F;
		DecimalFormat format = new DecimalFormat("#0.00");
		System.out.println("Identified (" + ffound + "/" + fexpected + ") fields (" + format.format(fieldPerc) + "% of client found).");
		System.out.println("Identified (" + mfound + "/" + mexpected + ") methods (" + format.format(methodPerc) + "% of client found).");
		System.out.println("Identified (" + hooks.size() + "/" + analysers.size() + ") classes (" + format.format(classPerc) + "% of client found).");
	}
	
	public int getTotalFields(HashMap<String, ClassNode> nodes) {
		int totalFields = 0;
		for(ClassNode cn : nodes.values()) {
			totalFields += cn.fields.size();
		}
		return totalFields;
	}
	
	public int getTotalMethods(HashMap<String, ClassNode> nodes) {
		int totalMethods = 0;
		for(ClassNode cn : nodes.values()) {
			totalMethods += cn.methods.size();
		}
		return totalMethods;
	}
	
	public HashMap<String, ClassHook> filterClasses() {
		HashMap<String, ClassHook> classes = new HashMap<String, ClassHook>();
		for(String s : obfuscatedClasses.keySet()) {
			ClassHook c = obfuscatedClasses.get(s);
			String oName = getNameFirstPart(c.getObfuscatedName());
			String rName = getNameFirstPart(c.getRefactoredName());
			if (!classes.containsKey(oName.toLowerCase())) {
				ClassHook hook = new ClassHook(oName, rName.equals("client") ? "Client" : rName);
				hook.setInterfaceHook(c.getInterfaceHook());
				classes.put(oName.toLowerCase(), hook);
			}
		}
		return classes;
	}
	
	public ArrayList<ClassHook> getSortedClassHooks() {
		List<ClassHook> hooks = new ArrayList<ClassHook>(filterClasses().values());
		Collections.sort(hooks, new Comparator<ClassHook>() {
			@Override
			public int compare(ClassHook o1, ClassHook o2) {
				return o1.getRefactoredName().compareTo(o2.getRefactoredName());
			}
		});
		return (ArrayList<ClassHook>) hooks;
	}
	
	public HashMap<String, ArrayList<FieldHook>> getObfFieldHooks() {
		return obfuscatedFields;
	}
	
	public HashMap<String, ArrayList<MethodHook>> getObfMethodHooks() {
		return obfuscatedMethods;
	}
	
	@Override
	public void save(File out) {
		try {
			String gsonString = ByteTools.GSON.toJson(this);
			FileOutputStream writer = new FileOutputStream(out);
			byte[] line = compress(gsonString.getBytes());
			writer.write(line);
			writer.close();
			writer = new FileOutputStream(out.getAbsolutePath().replace(".json", "raw.json"));
			writer.write(decompress(line));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] compress(final byte[] pToCompress) {
		byte[] compressed = new byte[] {};
		final Deflater compressor = new Deflater();
		compressor.setLevel(Deflater.BEST_COMPRESSION);
		compressor.setInput(pToCompress);
		compressor.finish();
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(pToCompress.length)) {
			final byte[] buf = new byte[1024];
			while (!compressor.finished()) {
				final int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
			compressed = bos.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return compressed;
	}
	
	public static byte[] decompress(final byte[] pCompressed) {
		final Inflater decompressor = new Inflater();
		decompressor.setInput(pCompressed);
		byte[] decompressed = new byte[] {};
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(pCompressed.length);
		try {
			final byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				try {
					final int count = decompressor.inflate(buf);
					bos.write(buf, 0, count);
				} catch (final DataFormatException e) {
					throw new RuntimeException(e);
				}
			}
			decompressed = bos.toByteArray();
			bos.close();
		} catch (final IOException e) {}
		return decompressed;
	}
}