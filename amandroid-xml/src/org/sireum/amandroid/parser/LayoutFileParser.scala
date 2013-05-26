package org.sireum.amandroid.parser

import java.io.ByteArrayOutputStream
import java.io.InputStream
import pxb.android.axml.AxmlReader
import pxb.android.axml.AxmlVisitor
import pxb.android.axml.AxmlVisitor.NodeVisitor
import org.sireum.util._
import org.sireum.amandroid.AndroidSymbolResolver.AndroidLibInfoTables

/**
 * Parser for analyzing the layout XML files inside an android application
 * 
 * adapted from Steven Arzt
 * modified by: Fengguo Wei
 */
object LayoutFileParser extends AbstractAndroidXMLParser {
	
  var androidLibInfoTables : AndroidLibInfoTables = null
	private final val DEBUG = true
	
	private final var userControls : Map[Int, LayoutControl] = Map()
	private final var callbackMethods : Map[String, MSet[String]] = Map()
	private final var packageName : String = ""
	
	private final val TYPE_NUMBER_VARIATION_PASSWORD = 0x00000010;
	private final val TYPE_TEXT_VARIATION_PASSWORD = 0x00000080;
	private final val TYPE_TEXT_VARIATION_VISIBLE_PASSWORD = 0x00000090;
	private final val TYPE_TEXT_VARIATION_WEB_PASSWORD = 0x000000e0;
	
	def setPackageName(packageName : String) {
		this.packageName = packageName;
	}
	
	def toPilarRecord(str : String) : String = "[|" + str.replaceAll("\\.", ":") + "|]"
	
	private def getLayoutClass(className : String) : String =
//		SootClass sc = Scene.v().forceResolve(className, SootClass.BODIES);
//		if ((sc == null || sc.isPhantom()) && !packageName.isEmpty())
//			sc = Scene.v().forceResolve(packageName + "." + className, SootClass.BODIES);
//		if (sc == null || sc.isPhantom())
//			sc = Scene.v().forceResolve("android.widget." + className, SootClass.BODIES);
//		if (sc == null || sc.isPhantom())
//			sc = Scene.v().forceResolve("android.webkit." + className, SootClass.BODIES);
//		if (sc == null || sc.isPhantom())
//   			System.err.println("Could not find layout class " + className);
//		return sc;
	  if(androidLibInfoTables.containsRecord(toPilarRecord(className)))
	    toPilarRecord(className)
	  else if(!packageName.isEmpty() && androidLibInfoTables.containsRecord(toPilarRecord(packageName + "." + className)))
	    toPilarRecord(packageName + "." + className)
	  else if(androidLibInfoTables.containsRecord(toPilarRecord("android.widget." + className)))
	    toPilarRecord("android.widget." + className)
	  else if(androidLibInfoTables.containsRecord(toPilarRecord("android.webkit." + className)))
	    toPilarRecord("android.webkit." + className)
	  else {
	    System.err.println("Could not find layout class " + className)
	    ""
	  }
	
	private def isLayoutClass(theClass : String) : Boolean = {
		if (theClass == null)
			return false
   		// To make sure that nothing all wonky is going on here, we
   		// check the hierarchy to find the android view class
   		val rUri = androidLibInfoTables.getRecordUri("[|android:view:ViewGroup|]")
   		val found = androidLibInfoTables.getAncestors(theClass).contains(rUri)
   		return found
	}
	
	private def isViewClass(theClass : String) : Boolean = {
		if (theClass == null)
			return false

		// To make sure that nothing all wonky is going on here, we
   		// check the hierarchy to find the android view class
			val viewUri = androidLibInfoTables.getRecordUri("[|android:view:View|]")
   		val webviewUri = androidLibInfoTables.getRecordUri("[|android:webkit:WebView|]")
   		val ancestors = androidLibInfoTables.getAncestors(theClass)
   		val found = (androidLibInfoTables.getAncestors(theClass).contains(viewUri) ||
   		    				 androidLibInfoTables.getAncestors(theClass).contains(webviewUri))
   		if (!found) {
   			System.err.println("Layout class " + theClass + " is not derived from "
   					+ "android.view.View");
   			return false
   		}
   		return true
	}
	
	private class LayoutParser(layoutFile : String, theClass : String) extends NodeVisitor {

  	private var id = -1
  	private var isSensitive = false

  	override def child(ns : String, name : String) : NodeVisitor = {
			if (name == null) {
				System.err.println("Encountered a null node name "
						+ "in file " + layoutFile + ", skipping node...")
				return null
			}
	  			
			val childClass = androidLibInfoTables.getRecordUri(getLayoutClass(name.trim()))
			if (isLayoutClass(childClass) || isViewClass(childClass))
	      new LayoutParser(layoutFile, childClass);
			else
				super.child(ns, name);
    }
	        	
  	override def attr(ns : String, name : String, resourceId : Int, typ : Int, obj : Object) : Unit = {
  		// Check that we're actually working on an android attribute
  		if (ns == null)
  			return
  	  var tempNS = ns
  		tempNS = tempNS.trim()
  		if (tempNS.startsWith("*"))
  			tempNS = tempNS.substring(1)
  		if (!tempNS.equals("http://schemas.android.com/apk/res/android"))
  			return

  		// Read out the field data
  		var tempName = name
  		tempName = tempName.trim();
  		if (tempName.equals("id") && typ == AxmlVisitor.TYPE_REFERENCE)
  			this.id = obj.asInstanceOf[Int]
  		else if (tempName.equals("password") && typ == AxmlVisitor.TYPE_INT_BOOLEAN)
  			isSensitive = (obj.asInstanceOf[Int]) != 0; // -1 for true, 0 for false
  		else if (!isSensitive && tempName.equals("inputType") && typ == AxmlVisitor.TYPE_INT_HEX) {
  			val tp = obj.asInstanceOf[Int]
  			isSensitive = (((tp & TYPE_NUMBER_VARIATION_PASSWORD) == TYPE_NUMBER_VARIATION_PASSWORD)
  					|| ((tp & TYPE_TEXT_VARIATION_PASSWORD) == TYPE_TEXT_VARIATION_PASSWORD)
  					|| ((tp & TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
  					|| ((tp & TYPE_TEXT_VARIATION_WEB_PASSWORD) == TYPE_TEXT_VARIATION_WEB_PASSWORD))
  		}
  		else if (isActionListener(tempName) && typ == AxmlVisitor.TYPE_STRING && obj.isInstanceOf[String]) {
  			val strData = obj.asInstanceOf[String].trim();
  			if (callbackMethods.keySet.contains(layoutFile))
  				callbackMethods(layoutFile) += strData
  			else {
  				val callbackSet : MSet[String] = msetEmpty
  				callbackSet += strData
  				callbackMethods += (layoutFile -> callbackSet)
  			}
  		}
  		else {
  			if (DEBUG && typ == AxmlVisitor.TYPE_STRING)
  				System.out.println("Found unrecognized XML attribute:  " + tempName)
  		}
  	}
  	
	/**
  	 * Checks whether this name is the name of a well-known Android listener
  	 * attribute. This is a function to allow for future extension.
  	 * @param name The attribute name to check. This name is guaranteed to
  	 * be in the android namespace.
  	 * @return True if the given attribute name corresponds to a listener,
  	 * otherwise false.
  	 */
  	private def isActionListener(name : String) : Boolean = name.equals("onClick")

  	override def end() = {
  		if (id > 0)
  			userControls += (id -> new LayoutControl(id, theClass, isSensitive))
  	}
	}
	
	/**
	 * Parses all layout XML files in the given APK file and loads the IDs of
	 * the user controls in it.
	 * @param fileName The APK file in which to look for user controls
	 */
	def parseLayoutFile(fileName : String, classes : Set[String]) {
				handleAndroidXMLFiles(fileName, null, new AndroidXMLHandler() {
					
					override def handleXMLFile(fileName : String, fileNameFilter : Set[String], stream : InputStream) : Unit = {
						// We only process valid layout XML files
					  
						if (!fileName.startsWith("res/layout"))
							return
						if (!fileName.endsWith(".xml")) {
							System.err.println("Skipping file " + fileName + " in layout folder...")
							return
						}
						println("fname-->" + fileName + " " + classes)
						// Get the fully-qualified class name
						var entryClass = fileName.substring(0, fileName.lastIndexOf("."))
						if (!packageName.isEmpty())
							entryClass = packageName + "." + entryClass
						println("enclass-->" + entryClass)
						// We are dealing with resource files
						if (!fileName.startsWith("res/layout"))
							return;
						if (fileNameFilter != null) {
							var found = false
							for (s <- fileNameFilter)
								if (s.equalsIgnoreCase(entryClass)) {
									found = true
								}
							if (!found)
								return
						}

						try {
							val bos = new ByteArrayOutputStream();
							var in : Int = 0
							in = stream.read()
							while (in >= 0){
								bos.write(in)
								in = stream.read()
							}
							bos.flush()
							val data = bos.toByteArray()
							if (data == null || data.length == 0)	// File empty?
								return
							
							val rdr = new AxmlReader(data)
							rdr.accept(new AxmlVisitor() {
								
								override def first(ns : String, name : String) : NodeVisitor = {
									val theClass = if(name == null) null else getLayoutClass(name.trim())
									if (theClass == null || isLayoutClass(theClass))
										new LayoutParser(fileName, theClass)
									else
										super.first(ns, name)
								}
							})
							
							System.out.println("Found " + userControls.size + " layout controls in file "
									+ fileName);
						}
						catch {
						  case ex : Exception =>
							  System.err.println("Could not read binary XML file: " + ex.getMessage())
								ex.printStackTrace()
						}
					}
				})
	}
	
	/**
	 * Gets the user controls found in the layout XML file. The result is a
	 * mapping from the id to the respective layout control.
	 * @return The layout controls found in the XML file.
	 */
	def getUserControls : Map[Int, LayoutControl] = this.userControls

	/**
	 * Gets the callback methods found in the layout XML file. The result is a
	 * mapping from the file name to the set of found callback methods.
	 * @return The callback methods found in the XML file.
	 */
	def getCallbackMethods : Map[String, MSet[String]] = this.callbackMethods
}