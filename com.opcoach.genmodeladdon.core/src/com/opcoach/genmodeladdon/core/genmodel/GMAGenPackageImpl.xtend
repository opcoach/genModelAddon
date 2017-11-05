package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenPackageImpl

class GMAGenPackageImpl extends GenPackageImpl {
	
	override getImportedPackageClassName() {
		GMATransform.replaceDevName(this,super.getImportedPackageClassName())
	}
	
	override getImportedPackageInterfaceName() {
		GMATransform.replaceDevName(this,super.getImportedPackageInterfaceName())
	}
	
	override getFactoryClassName() {
		GMATransform.replaceDevName(this,super.getFactoryClassName())
	}
	
	override getFactoryInstanceName() {
		GMATransform.replaceDevName(this,super.getFactoryInstanceName())
	}
	
}