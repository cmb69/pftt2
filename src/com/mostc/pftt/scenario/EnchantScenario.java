package com.mostc.pftt.scenario;

import com.mostc.pftt.host.AHost;
import com.mostc.pftt.host.Host;
import com.mostc.pftt.model.core.PhpBuild;
import com.mostc.pftt.model.core.PhpIni;
import com.mostc.pftt.results.ConsoleManager;
import com.mostc.pftt.results.EPrintType;

/** 
 * 
 * @author Matt Ficken
 *
 */

public class EnchantScenario extends AbstractINIScenario {

	@Override
	public IScenarioSetup setup(ConsoleManager cm, Host host, PhpBuild build, PhpIni ini) {
		// copy dictionary to where the build expects it
		// enchant will be able to find it there, so PHPTs will work
		
		if (!host.isWindows())
			return SETUP_SUCCESS;
		
		// this is only needed for Windows
		try {
			String dst = build.getBuildPath()+"/share/";
			if (!host.exists(dst)) {
				host.copyElevated(host.getPfttDir()+"/cache/util/enchant/share", dst);
			
				
				// make sure libraries are where they need to be also
				host.copy(build.getBuildPath()+"/libenchant*", build.getBuildPath()+"/lib/enchant/");
			}
			
			return SETUP_SUCCESS;
		} catch ( Exception ex ) {
			cm.addGlobalException(EPrintType.CANT_CONTINUE, getClass(), "setup", ex, "Unable to install spell checker dictionary");
			return null;
		}
	}
	
	@Override
	public boolean isPlaceholder() {
		return true;
	}

	@Override
	public Class<?> getSerialKey(EScenarioSetPermutationLayer layer) {
		return getClass();
	}

	@Override
	public String getName() {
		return "Enchant";
	}

	@Override
	public boolean isImplemented() {
		return true;
	}

	public static String getDictionaryFile(AHost host) {
		return host.getPfttDir()+"/cache/util/enchant/share/myspell/dicts/en_US.dic";
	}

} // end public class EnchantScenario