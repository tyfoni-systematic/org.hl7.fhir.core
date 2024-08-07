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

public class ReasonMedicationNotGivenCodesEnumFactory implements EnumFactory<ReasonMedicationNotGivenCodes> {

  public ReasonMedicationNotGivenCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("a".equals(codeString))
      return ReasonMedicationNotGivenCodes.A;
    if ("b".equals(codeString))
      return ReasonMedicationNotGivenCodes.B;
    if ("c".equals(codeString))
      return ReasonMedicationNotGivenCodes.C;
    if ("d".equals(codeString))
      return ReasonMedicationNotGivenCodes.D;
    throw new IllegalArgumentException("Unknown ReasonMedicationNotGivenCodes code '" + codeString + "'");
  }

  public String toCode(ReasonMedicationNotGivenCodes code) {
       if (code == ReasonMedicationNotGivenCodes.NULL)
           return null;
       if (code == ReasonMedicationNotGivenCodes.A)
      return "a";
    if (code == ReasonMedicationNotGivenCodes.B)
      return "b";
    if (code == ReasonMedicationNotGivenCodes.C)
      return "c";
    if (code == ReasonMedicationNotGivenCodes.D)
      return "d";
    return "?";
   }

  public String toSystem(ReasonMedicationNotGivenCodes code) {
    return code.getSystem();
  }

}