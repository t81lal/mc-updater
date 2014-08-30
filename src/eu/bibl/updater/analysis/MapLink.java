package eu.bibl.updater.analysis;


import eu.bibl.bio.JarInfo;
import eu.bibl.bio.jfile.in.JarDownloader;

import java.io.File;

public class MapLink {
	
	public static final String OUT_DIR = "res/";
	
	private JarDownloader jar;
	private int version;
	
	public MapLink(int version){
		this.version = version;
		jar = new JarDownloader(new JarInfo (new File(OUT_DIR + version + "/" + version + ".jar")));
	}
	
	public MapLink(String jarLocation){
		jar = new JarDownloader(new JarInfo(new File(jarLocation)));
	}

	public boolean successful(){
		return jar.parse();
	}
	
	public JarDownloader getDownloader(){
		return jar;
	}
	
	public int getVersion(){
		return version;
	}
}