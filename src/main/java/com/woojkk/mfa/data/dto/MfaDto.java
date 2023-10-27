package com.woojkk.mfa.data.dto;

import com.woojkk.mfa.data.entity.MfaEntity;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MfaDto {
  private long id;
  private String username;
  private String secretKey;
  private String type;
  private String otpNumber;

  public MfaDto(MfaEntity mfaEntity) {
    if (Optional.ofNullable(mfaEntity).isPresent()) {
      this.id = mfaEntity.getId();
      this.username = mfaEntity.getUsername();
      this.secretKey = mfaEntity.getSecretKey();
      this.type = mfaEntity.getType();
    }
  }

  @Builder
  public MfaDto(long id, String username, String secretKey, String type,
      String otpNumber) {
    this.id = id;
    this.username = username;
    this.secretKey = secretKey;
    this.type = type;
    this.otpNumber = otpNumber;
  }
}
