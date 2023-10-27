package com.woojkk.mfa.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "mfa", schema = "security")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MfaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(length = 10, nullable = false)
  private String username;

  @Column(length = 100, name = "secret_key")
  private String secretKey;

  @Column(length = 100)
  private String type;

  @Builder
  public MfaEntity(long id, String username, String secretKey, String type) {
    this.id = id;
    this.username = username;
    this.secretKey = secretKey;
    this.type = type;
  }
}
