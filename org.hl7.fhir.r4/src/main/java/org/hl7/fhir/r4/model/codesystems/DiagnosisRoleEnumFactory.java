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

public class DiagnosisRoleEnumFactory implements EnumFactory<DiagnosisRole> {

  public DiagnosisRole fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AD".equals(codeString))
      return DiagnosisRole.AD;
    if ("DD".equals(codeString))
      return DiagnosisRole.DD;
    if ("CC".equals(codeString))
      return DiagnosisRole.CC;
    if ("CM".equals(codeString))
      return DiagnosisRole.CM;
    if ("pre-op".equals(codeString))
      return DiagnosisRole.PREOP;
    if ("post-op".equals(codeString))
      return DiagnosisRole.POSTOP;
    if ("billing".equals(codeString))
      return DiagnosisRole.BILLING;
    throw new IllegalArgumentException("Unknown DiagnosisRole code '" + codeString + "'");
  }

  public String toCode(DiagnosisRole code) {
       if (code == DiagnosisRole.NULL)
           return null;
       if (code == DiagnosisRole.AD)
      return "AD";
    if (code == DiagnosisRole.DD)
      return "DD";
    if (code == DiagnosisRole.CC)
      return "CC";
    if (code == DiagnosisRole.CM)
      return "CM";
    if (code == DiagnosisRole.PREOP)
      return "pre-op";
    if (code == DiagnosisRole.POSTOP)
      return "post-op";
    if (code == DiagnosisRole.BILLING)
      return "billing";
    return "?";
   }

  public String toSystem(DiagnosisRole code) {
    return code.getSystem();
  }

}