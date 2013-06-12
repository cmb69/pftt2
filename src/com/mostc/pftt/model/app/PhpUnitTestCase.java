package com.mostc.pftt.model.app;

import com.github.mattficken.io.StringUtil;
import com.mostc.pftt.host.Host;
import com.mostc.pftt.model.TestCase;

/** a PhpUnitTestCase
 * 
 * @see EPhpUnitTestStatus
 * @author Matt Ficken
 *
 */

public class PhpUnitTestCase extends TestCase {
	public static final int MAX_TEST_TIME_SECONDS = 60;
	protected final PhpUnitDist php_unit_dist;
	protected final String abs_filename, rel_filename, className, methodName;
	protected final int arg_count;
	
	protected PhpUnitTestCase(PhpUnitDist php_unit_dist, String abs_filename, String rel_filename, String className, String methodName, int arg_count) {
		this.php_unit_dist = php_unit_dist;
		// don't need to call #normalizeFilename here usually. it's called in PhpUnitSourcetestPack#readDir...
		// calling it (again )here would be a performance hit
		this.abs_filename = abs_filename;
		this.rel_filename = rel_filename;
		// don't need to normalize classname:
		// if it has \ thats ok b/c its legal PHP (namespaces) whereas it won't be / b/c that's illegal in PHP
		this.className = className;
		this.methodName = methodName;
		this.arg_count = arg_count;
	}
	
	public int getArgCount() {
		return arg_count;
	}
	
	public PhpUnitDist getPhpUnitDist() {
		return php_unit_dist;
	}
	
	/** name of file.
	 * 
	 * file is case preserved to avoid breaking posix.
	 * 
	 * windows slashes \\ are standardized to / posix.
	 * 
	 * for case-insensitive matching @see #isFileName or @see #fileNameStartsWithAny
	 * 
	 * @return
	 */
	public String getFileName() {
		return rel_filename;
	}
	
	public String getAbsoluteFileName() {
		return abs_filename;
	}
	
	public boolean fileNameStartsWithAny(String[] ext_names) {
		return StringUtil.startsWithAnyIC(getFileName(), ext_names);
	}
	
	public boolean fileNameStartsWithAny(String ext_name) {
		return StringUtil.startsWithIC(getFileName(), ext_name);
	}
	
	/** search file for classes that extend PhpUnit_Framework_TestCase
	 * 
	 * @return
	 */
	public String getClassName() {
		return className;
	}
	
	/** search class for all methods that start with 'test'
	 * 
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}
	
	public boolean isFileName(String file_name) {
		return this.rel_filename.toLowerCase().equals(file_name.toLowerCase()) 
				|| this.abs_filename.toLowerCase().equals(file_name.toLowerCase());
	}
	
	public boolean isFileName(String... file_names) {
		for (String file_name : file_names) {
			if (isFileName(file_name))
				return true;
		}
		return false;
	}
	
	public boolean equals(String file_name, String class_name) {
		return isFileName(file_name) && this.className.equals(class_name);
	}
	
	public boolean equals(String file_name, String class_name, String method_name) {
		return equals(file_name, class_name) && this.methodName.equals(method_name);
	}

	@Override
	public String getName() {
		return className + "::" + methodName + "(" + rel_filename + ")";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/** fixes slashes to Posix forward slash /.
	 * 
	 * this is case-preserving (to avoid breaking on posix). case is not changed.
	 * 
	 * to do case-insenstive matching @see #isFileName
	 * 
	 * @param test_name
	 * @return
	 */
	public static String normalizeFileName(String test_name) {
		return Host.toUnixPath(test_name);
	}

} // end public class PhpUnitTestCase