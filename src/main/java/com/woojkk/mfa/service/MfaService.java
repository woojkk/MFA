package com.woojkk.mfa.service;

import com.woojkk.mfa.data.dto.MfaDto;
import com.woojkk.mfa.data.dto.MfaInitDto;
import com.woojkk.mfa.data.dto.MfaProveDto;

public interface MfaService {

  MfaDto getMfa(String username);

  MfaProveDto getMfaSecretKey(String username);

  MfaInitDto setMfa(MfaInitDto mfaInitDto);

  MfaDto setMfa(MfaDto mfaDto);

  void deleteMfa(String username);
}
