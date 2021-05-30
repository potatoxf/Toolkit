package pxf.extsweb.admin.framework.web.entity;

import java.util.Objects;
import pxf.toolkit.basic.util.Compare;

/**
 * @author potatoxf
 * @date 2021/4/17
 */
public class EmployeeInformation extends AbstractEntity<EmployeeInformation> {

  private String username;
  private String name;
  private String password;
  private String salt;
  private String phone;
  private String email;
  private String address;
  private String avatar;
  private Integer gender;
  private Integer accountStatus;

  @Override
  public int compareTo(EmployeeInformation other) {
    int compareToValue = Compare.objectOrComparableForPriorityNull(username, other.username);
    if (compareToValue == 0) {
      compareToValue = super.compareTo(other);
    }
    return compareToValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    EmployeeInformation that = (EmployeeInformation) o;
    return Objects.equals(getUsername(), that.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getUsername());
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public Integer getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(Integer accountStatus) {
    this.accountStatus = accountStatus;
  }
}
