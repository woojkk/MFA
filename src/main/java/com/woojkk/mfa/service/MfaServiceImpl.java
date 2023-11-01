package com.woojkk.mfa.service;

import com.woojkk.mfa.data.dto.MfaDto;
import com.woojkk.mfa.data.dto.MfaInitDto;
import com.woojkk.mfa.data.dto.MfaProveDto;
import com.woojkk.mfa.data.entity.MfaEntity;
import com.woojkk.mfa.repository.MfaRepository;
import org.springframework.stereotype.Service;

@Service
public class MfaServiceImpl implements MfaService {

  private final MfaRepository mfaRepository;

  public MfaServiceImpl(MfaRepository mfaRepository) {
    this.mfaRepository = mfaRepository;
  }

  @Override
  public MfaDto getMfa(String username) {
    return new MfaDto(mfaRepository.findByUsername(username));
  }

  @Override
  public MfaProveDto getMfaSecretKey(String username) {
    return new MfaProveDto(mfaRepository.findByUsername(username));
  }

  @Override
  public MfaInitDto setMfa(MfaInitDto mfaInitDto) {
    mfaRepository.save(new MfaEntity(mfaInitDto));
    return mfaInitDto;
  }

  @Override
  public MfaDto setMfa(MfaDto mfaDto) {
    mfaRepository.save(new MfaEntity(mfaDto));
    return mfaDto;
  }

  @Override
  public void deleteMfa(String username) {
    mfaRepository.delete(mfaRepository.findByUsername(username));
  }
}
