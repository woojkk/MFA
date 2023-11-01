package com.woojkk.mfa.data.entity;

import com.woojkk.mfa.data.dto.MfaDto;
import com.woojkk.mfa.data.dto.MfaInitDto;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "mfa", schema = "security")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MfaEntity implements Serializable {
  @Id
  @Column
  private long id;

  @Column(length = 50, nullable = false)
  private String username;

  @Column(length = 512, name = "secret_key")
  private String secretKey;

  @Column(length = 100)
  private String type;

  public MfaEntity(MfaDto mfaDto) {
    this.id = mfaDto.getId();
    this.username = mfaDto.getUsername();
    this.secretKey = mfaDto.getSecretKey();
    this.type = mfaDto.getType();
  }

  public MfaEntity(MfaInitDto mfaInitDto) {
    this.username = mfaInitDto.getUsername();
    this.secretKey = mfaInitDto.getSecretKey();
    this.type = mfaInitDto.getType();
  }

  @Builder
  public MfaEntity(long id, String username, String secretKey, String type) {
    this.id = id;
    this.username = username;
    this.secretKey = secretKey;
    this.type = type;
  }
}
