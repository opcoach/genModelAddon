package com.opcoach.genmodeladdon.core.genmodel.impl

import com.opcoach.genmodeladdon.core.genmodel.GMATransform
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenPackageImpl

class GMAGenPackageImpl extends GenPackageImpl {
	
	override getImportedPackageClassName() {
		GMATransform.replaceDevName(this,super.getImportedPackageClassName())
	}
	
	override getImportedPackageInterfaceName() {
		GMATransform.replaceDevName(this,super.getImportedPackageInterfaceName())
	}
	

}