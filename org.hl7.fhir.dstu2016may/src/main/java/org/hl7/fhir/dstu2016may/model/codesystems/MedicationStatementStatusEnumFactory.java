package org.hl7.fhir.dstu2016may.model.codesystems;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0

import org.hl7.fhir.dstu2016may.model.EnumFactory;

public class MedicationStatementStatusEnumFactory implements EnumFactory<MedicationStatementStatus> {

  public MedicationStatementStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return MedicationStatementStatus.ACTIVE;
    if ("completed".equals(codeString))
      return MedicationStatementStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return MedicationStatementStatus.ENTEREDINERROR;
    if ("intended".equals(codeString))
      return MedicationStatementStatus.INTENDED;
    throw new IllegalArgumentException("Unknown MedicationStatementStatus code '" + codeString + "'");
  }

  public String toCode(MedicationStatementStatus code) {
       if (code == MedicationStatementStatus.NULL)
           return null;
       if (code == MedicationStatementStatus.ACTIVE)
      return "active";
    if (code == MedicationStatementStatus.COMPLETED)
      return "completed";
    if (code == MedicationStatementStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == MedicationStatementStatus.INTENDED)
      return "intended";
    return "?";
   }

  public String toSystem(MedicationStatementStatus code) {
    return code.getSystem();
  }

}