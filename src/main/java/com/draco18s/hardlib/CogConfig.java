package com.draco18s.hardlib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.Loader;

public class CogConfig {
	private static List<String> extraModules = new ArrayList<String>(); /*new String[] {
        	"HarderOres.xml",
        	"HarderIC2.xml"
        };*/
	//private static boolean unpacked = false;

	public static void addCogModule(String name) {
		extraModules.add(name);
		//unpacked = false;
	}

	public static void unpackConfigs() {
		unpackConfigs(false);
	}

	public static void unpackConfigs(boolean clean) {
		//if(unpacked && !force) return;
		//unpacked = true;
		File configPath = getConfigDir();
		File modulesDir = new File(configPath, "modules");

		if(clean) {
			File defaultCustomDir = new File(modulesDir, "custom");
			if (defaultCustomDir.exists()) {
				for (String module : extraModules) {
					File writeFile = new File(defaultCustomDir, module);
					if(writeFile.exists()) {
						writeFile.delete();
					}
				}
			}
		}

		File defaultModulesDir = new File(modulesDir, "mods");
		if (defaultModulesDir.exists()) {
			File[] defaultModules = defaultModulesDir.listFiles();
        	for (File defaultModule : defaultModules) {
        		System.out.println("Deleted file " + defaultModule.getName());
        		defaultModule.delete();
        	}
		} else {
			configPath.mkdir();
			modulesDir.mkdir();
			defaultModulesDir.mkdir();
		}

		for (String module : extraModules) {
			File writeFile = new File(defaultModulesDir, module);
			if(!writeFile.exists()) {
				unpackConfigFile(module, writeFile);
			}
		}
		//extraModules.clear();
	}

	public static void unpackDefaultConfig() {
		/*String configName = "CustomOreGen_Config.xml";
		File configPath = getConfigDir();
		File destination = new File(configPath, configName);
    	if(!destination.exists()) {
    		String resourceName = "cog_config/" + configName;
            try {
        	    InputStream ex = HardLib.class.getClassLoader().getResourceAsStream(resourceName);
                BufferedOutputStream streamOut = new BufferedOutputStream(new FileOutputStream(destination));
                byte[] buffer = new byte[1024];
                boolean len = false;
                int len1;

                while ((len1 = ex.read(buffer)) >= 0)
                {
                    streamOut.write(buffer, 0, len1);
                }

                ex.close();
                streamOut.close();
            }
            catch (Exception var6) {
                throw new RuntimeException("Failed to unpack resource \'" + resourceName + "\'", var6);
            }
    	}*/
	}

	private static boolean unpackConfigFile(String configName, File destination) {
		String resourceName = "cog_config/" + configName;
		try {
			InputStream ex = HardLib.class.getClassLoader().getResourceAsStream(resourceName);
			BufferedOutputStream streamOut = new BufferedOutputStream(new FileOutputStream(destination));
			byte[] buffer = new byte[1024];
			boolean len = false;
			int len1;

			while ((len1 = ex.read(buffer)) >= 0)
			{
				streamOut.write(buffer, 0, len1);
			}

			ex.close();
			streamOut.close();
			return true;
		}
		catch (Exception var6) {
			throw new RuntimeException("Failed to unpack resource \'" + resourceName + "\'", var6);
		}
	}

	private static File getConfigDir() {
		return new File(Loader.instance().getConfigDir(), "CustomOreGen");
	}
}
