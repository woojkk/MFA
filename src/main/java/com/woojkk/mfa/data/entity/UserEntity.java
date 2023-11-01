package com.woojkk.mfa.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users", schema = "security")
@EqualsAndHashCode(callSuper = false)
public class UserEntity {
  @Id
  @Column(nullable = false)
  private long id;

  @Column(length = 50)
  private String username;

  @Column(length = 512)
  private String password;

  @Column(length = 1000)
  private String roles;

  @Builder
  public UserEntity(long id, String username, String password, String roles) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
