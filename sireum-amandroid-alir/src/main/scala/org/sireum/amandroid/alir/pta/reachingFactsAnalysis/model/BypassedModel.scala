/*******************************************************************************
 * Copyright (c) 2013 - 2016 Fengguo Wei and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Detailed contributors are listed in the CONTRIBUTOR.md
 ******************************************************************************/
package org.sireum.amandroid.alir.pta.reachingFactsAnalysis.model

import org.sireum.jawa.alir.pta.PTAResult
import org.sireum.jawa.JawaMethod
import org.sireum.jawa.alir.Context
import org.sireum.util._
import org.sireum.jawa.alir.pta.reachingFactsAnalysis.RFAFact

object BypassedModel {
  def handleBypass(s : PTAResult, calleeMethod : JawaMethod, args : List[String], retVars : Seq[String], currentContext : Context) : (ISet[RFAFact], ISet[RFAFact], Boolean) = {
    (isetEmpty, isetEmpty, true)
  }
}
