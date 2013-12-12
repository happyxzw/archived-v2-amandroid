package org.sireum.amandroid.android.libPilarFiles

import org.sireum.util._
import org.sireum.jawa.util.MyFileUtil

object AndroidLibPilarFiles{
  protected def sourceDirUri(path : String) = { 
    FileUtil.toUri(path)
  }
  protected def listFiles(dirUri : FileResourceUri,
                   ext : String) : ISeq[FileResourceUri] =
    FileUtil.listFiles(dirUri, ext, true)
	
  val ANDROID_PILAR_FILE_EXT = ".pilar"
    
  def pilarModelFiles(path : String) = listFiles(sourceDirUri(path), ANDROID_PILAR_FILE_EXT)
}