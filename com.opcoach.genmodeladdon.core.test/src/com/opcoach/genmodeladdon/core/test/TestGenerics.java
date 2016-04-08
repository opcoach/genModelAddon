package com.opcoach.genmodeladdon.core.test;

import org.junit.Test;

public class TestGenerics extends GenModelAddonTestCase
{


	// -------------------------------------------------------------
	// ------------------- Test gen classes for generic types --
	// -------------------------------------------------------------
	@Test
	public void theGenFolderInterfaceMustBeGeneric()
	{
		assertFileContains("src-gen/com/opcoach/project/MFolder.java", "interface MFolder<T> extends EObject");
	}

	@Test
	public void theGenFolderClassMustBeGeneric()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MFolderImpl.java",
				"class MFolderImpl<T> extends MinimalEObjectImpl.Container implements Folder<T>");
	}

	@Test
	public void theGenTaskFolderInterfaceMustExtendsFolderOfTask()
	{
		assertFileContains("src-gen/com/opcoach/project/MTaskFolder.java",
				"public interface MTaskFolder extends Folder<Task>");
	}

	@Test
	public void theGenTaskFolderClassMustBeGeneric()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MTaskFolderImpl.java",
				"class MTaskFolderImpl extends FolderImpl<Task> implements TaskFolder");
	}

	// -------------------------------------------------------------
	// ------------------- Test dev classes for generic types --
	// -------------------------------------------------------------

	@Test
	public void theDevFolderInterfaceMustBeGeneric()
	{
		assertFileContains("src/com/opcoach/project/Folder.java", "interface Folder<T> extends MFolder<T>");
	}

	@Test
	public void theDevFolderClassMustBeGeneric()
	{
		assertFileContains("src/com/opcoach/project/impl/FolderImpl.java",
				"class FolderImpl<T> extends MFolderImpl<T> implements Folder<T>");
	}

	@Test
	public void theDevTaskFolderInterfaceMustBeGeneric()
	{
		assertFileContains("src/com/opcoach/project/TaskFolder.java", "interface TaskFolder extends MTaskFolder");
	}

	@Test
	public void theDevTaskFolderClassMustBeGeneric()
	{
		assertFileContains("src/com/opcoach/project/impl/TaskFolderImpl.java",
				"class TaskFolderImpl extends MTaskFolderImpl implements TaskFolder");
	}

	@Test
	public void theDevFactoryInterfaceMustReturnGenericFolder()
	{
		assertFileContains("src/com/opcoach/project/ProjectFactory.java",
				"public <T> Folder<T> createFolder()");
	}

	@Test
	public void theDevFactoryClassMustReturnGenericFolder()
	{
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java",
				"public <T> Folder<T> createFolder()");
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java",
				"Folder<T> result = new FolderImpl<T>()");
	}

}
