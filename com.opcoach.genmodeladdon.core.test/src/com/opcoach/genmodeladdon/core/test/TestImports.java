package com.opcoach.genmodeladdon.core.test;

import org.junit.Test;

public class TestImports extends GenModelAddonTestCase
{


	// --------------------------------------------------------------------
	// ------------------- Test imports of dev class in the gen classses --
	// --------------------------------------------------------------------

	@Test
	public void folderImplementationMustImportFolder()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MFolderImpl.java",
				"import com.opcoach.project.Folder");
	}

	
	@Test
	public void projectImplementationMustImportPersonProjectAndTask()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java",
				"import com.opcoach.project.Project");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java",
				"import com.opcoach.project.Person");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java",
				"import com.opcoach.project.Task");
	}

	
	

}
