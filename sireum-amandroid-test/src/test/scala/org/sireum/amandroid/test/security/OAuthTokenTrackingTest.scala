package org.sireum.amandroid.test.security

import org.junit.runner.RunWith
import org.sireum.jawa.JawaCodeSource
import org.sireum.amandroid.example.interprocedural.InterproceduralExamples
import org.sireum.amandroid.android.libPilarFiles.AndroidLibPilarFiles
import org.sireum.amandroid.alir.AndroidGlobalConfig
import java.util.zip.GZIPInputStream
import java.io.FileInputStream
import org.sireum.jawa.xml.AndroidXStream
import org.sireum.jawa.alir.interProcedural.sideEffectAnalysis.InterProceduralSideEffectAnalysisResult
import org.scalatest.junit.JUnitRunner
import org.sireum.jawa.alir.LibSideEffectProvider
import org.sireum.util.FileUtil
import org.sireum.jawa.GlobalConfig
import org.sireum.amandroid.test.framework.security.OAuthTokenTrackingTestFramework



/**
 * @author <a href="mailto:sroy@k-state.edu">Sankar Roy</a>
 */
@RunWith(classOf[JUnitRunner])
class OAuthTokenTrackingTest extends OAuthTokenTrackingTestFramework {
  var i = 0
  val androidLibDir = System.getenv(AndroidGlobalConfig.ANDROID_LIB_DIR)
  if(androidLibDir != null){
		JawaCodeSource.preLoad(FileUtil.toUri(androidLibDir), GlobalConfig.PILAR_FILE_EXT)
		
//		LibSideEffectProvider.init
		
	  InterproceduralExamples.testAPKFiles.
	  //filter { s => s.endsWith("com.zoosk.zoosk.apk") }. // LocationFlow2.apk
	  foreach { resfile =>
	    Analyzing title resfile file resfile
	  }
//	  InterproceduralExamples.popularAPKFiles.
////	  filter { s => s.contains("la.droid.qr.apk") }.
//	  foreach { resfile =>
//	    if(i > 500) 
//	    Analyzing title resfile file resfile
//	    i+=1
//	  }
//		InterproceduralExamples.testFiles.
////	  filter { s => s.endsWith("acctsvcs.us.apk")}.
//	  foreach { resfile =>
////	    if(i < 10) 
//	    Analyzing title resfile file resfile
////	    i+=1
//	  }
//	  InterproceduralExamples.randomAPKFiles.
//	  //filter { s => s.endsWith("enterprise.dmagent.apk") }.
//	  foreach { resfile =>
//	   // if(i < 89) i += 1
//	   // if(resfile.endsWith("app.kazoebito.com.apk"))
//	    Analyzing title resfile file resfile
//	  }
//	  InterproceduralExamples.normalAPKFiles.
//	//  filter { s => s.name.endsWith("android-1.apk") }.
//	  foreach { resRet =>
//	//    if(i < 37) i += 1
//	    Analyzing title resRet.name file resRet
//	  }
//	  InterproceduralExamples.maliciousAPKRets.
//	//  filter { s => s.name.endsWith("86add.apk")}.
//	  foreach { resRet =>
//	//    if(i < 7) i += 1
//	    Analyzing title resRet.name file resRet
//	  }
//	  InterproceduralExamples.maliciousArborFiles.
////	  filter { s => s.endsWith("6ba36c93.apk")}.
//	  foreach { resfile =>
////	    if(i < 10) 
//	    Analyzing title resfile file resfile
////	    i+=1
//	  }
//	  InterproceduralExamples.benchAPKFiles.
//	  filter { s => s.endsWith("PrivateDataLeak2.apk") }.
//	  foreach { fileUri =>
//	    Analyzing title fileUri file fileUri
//	  }
  } else {
    System.err.println("Does not have env var: " + AndroidGlobalConfig.ANDROID_LIB_DIR)
  }
}

