package com.mostc.pftt.runner;

import java.io.IOException;
import java.util.Map;

import com.mostc.pftt.host.AHost;
import com.mostc.pftt.host.AHost.ExecHandle;
import com.mostc.pftt.model.app.PhpUnitTestCase;
import com.mostc.pftt.model.core.PhpBuild;
import com.mostc.pftt.model.core.PhpIni;
import com.mostc.pftt.results.ConsoleManager;
import com.mostc.pftt.results.ITestResultReceiver;
import com.mostc.pftt.runner.LocalPhpUnitTestPackRunner.PhpUnitThread;
import com.mostc.pftt.scenario.CliScenario;
import com.mostc.pftt.scenario.ScenarioSet;
import com.mostc.pftt.util.NTStatus;

public class CliPhpUnitTestCaseRunner extends AbstractPhpUnitTestCaseRunner {
	protected ExecHandle running_test_handle;
	protected String output_str;

	public CliPhpUnitTestCaseRunner(CliScenario sapi_scenario, PhpUnitThread thread, ITestResultReceiver tmgr, Map<String, String> globals, Map<String, String> env, ConsoleManager cm, AHost host, ScenarioSet scenario_set, PhpBuild build, PhpUnitTestCase test_case, String my_temp_dir, Map<String, String> constants, String include_path, String[] include_files, PhpIni ini, boolean reflection_only) {
		super(sapi_scenario, thread, tmgr, globals, env, cm, host, scenario_set, build, test_case, my_temp_dir, constants, include_path, include_files, ini, reflection_only);
	}
	
	@Override
	protected void stop(boolean force) {
		if (running_test_handle==null)
			return;
		running_test_handle.close(force);
	}
	
	private void doExecute(String template_file, String ini_dir) throws Exception {
		running_test_handle = host.execThread(
				build.getPhpExe()+" -c "+ini_dir+" "+template_file,
				env,
				test_case.getPhpUnitDist().getPath().getAbsolutePath()
			);
		
		StringBuilder output_sb = new StringBuilder(128);
		
		running_test_handle.run(
				output_sb, 
				null, 
				getMaxTestRuntimeSeconds(), 
				null, 
				0, 
				cm.getSuspendSeconds()
			);
		
		output_str = output_sb.toString();
		
		is_crashed = running_test_handle.isCrashed();
	}
	
	@Override
	protected String execute(String template_file) throws IOException, Exception {
		final String ini_dir = build.prepare(cm, host); // XXX store PhpIni in my_temp_dir ?
		
		doExecute(template_file, ini_dir);
		if (is_crashed && running_test_handle.getExitCode() != -2
				&& running_test_handle.getExitCode() != NTStatus.STATUS_ACCESS_VIOLATION) {
			// try a second time to be sure
			is_crashed = false;
			
			doExecute(template_file, ini_dir);
		}
		
		if (is_crashed) {
			int exit_code = running_test_handle.getExitCode();
			
			output_str += "PFTT: crashed: exit_code="+exit_code+" status="+AHost.guessExitCodeStatus(host, exit_code);
		}
		
		running_test_handle = null;
		
		return output_str;
	}

	@Override
	public String getSAPIOutput() {
		return output_str;
	}

	@Override
	public String getSAPIConfig() {
		return null;
	}

} // end public class CliPhpUnitTestCaseRunner
