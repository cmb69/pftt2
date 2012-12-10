package com.mostc.pftt.scenario;

import java.util.Map;

import com.mostc.pftt.host.Host;
import com.mostc.pftt.model.phpt.PhpBuild;
import com.mostc.pftt.results.ConsoleManager;

/** Scenario for testing the pdo_odbc and odbc extensions against a Microsoft Access database. (NOT IMPLEMENTED)
 * 
 * Access is one of 3 supported databases for the odbc and pdo_odbc extensions (the other 2 are SQL Server and IBM's DB2. We don't support DB2).
 * 
 * @see MSSQLODBCScenario
 * @author Matt Ficken
 *
 */

public class MSAccessScenario extends AbstractODBCScenario {

	@Override
	protected void name_exists(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "ODBC-Access";
	}
	
	@Override
	public boolean isImplemented() {
		return false;
	}

	@Override
	public boolean setup(ConsoleManager cm, Host host, PhpBuild build, ScenarioSet scenario_set) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getENV(Map<String, String> env) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EScenarioStartState start(ConsoleManager cm, Host host, PhpBuild build, ScenarioSet scenario_set) {
		return EScenarioStartState.SKIP;
	}

}
