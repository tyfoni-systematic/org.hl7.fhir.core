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

public class ResearchStudyPhaseEnumFactory implements EnumFactory<ResearchStudyPhase> {

  public ResearchStudyPhase fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("n-a".equals(codeString))
      return ResearchStudyPhase.NA;
    if ("early-phase-1".equals(codeString))
      return ResearchStudyPhase.EARLYPHASE1;
    if ("phase-1".equals(codeString))
      return ResearchStudyPhase.PHASE1;
    if ("phase-1-phase-2".equals(codeString))
      return ResearchStudyPhase.PHASE1PHASE2;
    if ("phase-2".equals(codeString))
      return ResearchStudyPhase.PHASE2;
    if ("phase-2-phase-3".equals(codeString))
      return ResearchStudyPhase.PHASE2PHASE3;
    if ("phase-3".equals(codeString))
      return ResearchStudyPhase.PHASE3;
    if ("phase-4".equals(codeString))
      return ResearchStudyPhase.PHASE4;
    throw new IllegalArgumentException("Unknown ResearchStudyPhase code '" + codeString + "'");
  }

  public String toCode(ResearchStudyPhase code) {
       if (code == ResearchStudyPhase.NULL)
           return null;
       if (code == ResearchStudyPhase.NA)
      return "n-a";
    if (code == ResearchStudyPhase.EARLYPHASE1)
      return "early-phase-1";
    if (code == ResearchStudyPhase.PHASE1)
      return "phase-1";
    if (code == ResearchStudyPhase.PHASE1PHASE2)
      return "phase-1-phase-2";
    if (code == ResearchStudyPhase.PHASE2)
      return "phase-2";
    if (code == ResearchStudyPhase.PHASE2PHASE3)
      return "phase-2-phase-3";
    if (code == ResearchStudyPhase.PHASE3)
      return "phase-3";
    if (code == ResearchStudyPhase.PHASE4)
      return "phase-4";
    return "?";
   }

  public String toSystem(ResearchStudyPhase code) {
    return code.getSystem();
  }

}