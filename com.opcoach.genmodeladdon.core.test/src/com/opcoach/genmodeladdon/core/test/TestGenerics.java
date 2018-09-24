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

	@Test
	public void theGenStoreInterfaceMustBeGeneric()
	{
		assertFileContains("src-gen/com/opcoach/project/MStore.java",
				"public interface MStore<T, U> extends EObject");
	}
	
	@Test
	public void theGenStoreClassMustBeGeneric()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MStoreImpl.java",
				"public class MStoreImpl<T, U> extends MinimalEObjectImpl.Container implements Store<T, U>");
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
	public void theEMfGeneratedFactoryInterfaceMustReturnGenericFolder()
	{
		assertFileContains("src-gen/com/opcoach/project/MProjectFactory.java",
				"<T> Folder<T> createFolder()");
	}

	@Test
	public void theEMfGeneratedFactoryClassMustReturnGenericFolder()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectFactoryImpl.java",
				"public <T> Folder<T> createFolder()");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectFactoryImpl.java",
				"FolderImpl<T> folder = new FolderImpl<T>();");
	}
	
	@Test
	public void theDevStoreInterfaceMustBeGeneric()
	{
		assertFileContains("src/com/opcoach/project/Store.java", "public interface Store<T,U> extends MStore<T,U>");
	}

	@Test
	public void theDevStoreClassMustBeGeneric()
	{
		assertFileContains("src/com/opcoach/project/impl/StoreImpl.java",
				"public class StoreImpl<T,U> extends MStoreImpl<T,U> implements Store<T,U>");
	}

	@Test
	public void theDevProjectFolderMustBeGenericAndExtendProject()
	{
		assertFileContains("src/com/opcoach/project/ProjectFolder.java",
				"public interface ProjectFolder<T extends Project> extends MProjectFolder<T extends Project>");
	}

	@Test
	public void theDevProjectFolderImplMustBeGenericAndExtendProject()
	{
		assertFileContains("src/com/opcoach/project/impl/ProjectFolderImpl.java",
				"public class ProjectFolderImpl<T extends Project> extends MProjectFolderImpl<T extends Project> implements ProjectFolder<T extends Project>");
	}

	@Test
	public void theGenProjectFolderImplMustBeGenericAndExtendProject()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectFolderImpl.java",
				"public class MProjectFolderImpl<T extends Project> extends MinimalEObjectImpl.Container implements ProjectFolder<T>");
	}



}
