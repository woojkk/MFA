package com.woojkk.mfa.data.dto;

import com.woojkk.mfa.data.entity.MfaEntity;
import java.io.Serializable;
import java.util.Optional;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class MfaProveDto implements Serializable {
  private String secretKey;
  private String type;

  public MfaProveDto(MfaEntity mfaEntity) {
    if(Optional.ofNullable(mfaEntity).isPresent()) {
      this.secretKey = mfaEntity.getSecretKey();
      this.type = mfaEntity.getType();
    }
  }

  @Builder
  public MfaProveDto(String secretKey, String type) {
    this.secretKey = secretKey;
    this.type = type;
  }
}
