package org.hl7.fhir.r4.model.codesystems;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Wed, Jan 30, 2019 16:19-0500 for FHIR v4.0.0

import org.hl7.fhir.r4.model.EnumFactory;

public class ListExampleCodesEnumFactory implements EnumFactory<ListExampleCodes> {

  public ListExampleCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("alerts".equals(codeString))
      return ListExampleCodes.ALERTS;
    if ("adverserxns".equals(codeString))
      return ListExampleCodes.ADVERSERXNS;
    if ("allergies".equals(codeString))
      return ListExampleCodes.ALLERGIES;
    if ("medications".equals(codeString))
      return ListExampleCodes.MEDICATIONS;
    if ("problems".equals(codeString))
      return ListExampleCodes.PROBLEMS;
    if ("worklist".equals(codeString))
      return ListExampleCodes.WORKLIST;
    if ("waiting".equals(codeString))
      return ListExampleCodes.WAITING;
    if ("protocols".equals(codeString))
      return ListExampleCodes.PROTOCOLS;
    if ("plans".equals(codeString))
      return ListExampleCodes.PLANS;
    throw new IllegalArgumentException("Unknown ListExampleCodes code '" + codeString + "'");
  }

  public String toCode(ListExampleCodes code) {
       if (code == ListExampleCodes.NULL)
           return null;
       if (code == ListExampleCodes.ALERTS)
      return "alerts";
    if (code == ListExampleCodes.ADVERSERXNS)
      return "adverserxns";
    if (code == ListExampleCodes.ALLERGIES)
      return "allergies";
    if (code == ListExampleCodes.MEDICATIONS)
      return "medications";
    if (code == ListExampleCodes.PROBLEMS)
      return "problems";
    if (code == ListExampleCodes.WORKLIST)
      return "worklist";
    if (code == ListExampleCodes.WAITING)
      return "waiting";
    if (code == ListExampleCodes.PROTOCOLS)
      return "protocols";
    if (code == ListExampleCodes.PLANS)
      return "plans";
    return "?";
   }

  public String toSystem(ListExampleCodes code) {
    return code.getSystem();
  }

}