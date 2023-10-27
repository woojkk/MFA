package com.woojkk.mfa.data.dto;

import com.woojkk.mfa.data.entity.MfaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MfaProveDto {
  private String secretKey;
  private String type;

  public MfaProveDto(MfaEntity mfaEntity) {
    this.secretKey = mfaEntity.getSecretKey();
    this.type = mfaEntity.getType();
  }

  @Builder
  public MfaProveDto(String secretKey, String type) {
    this.secretKey = secretKey;
    this.type = type;
  }
}
