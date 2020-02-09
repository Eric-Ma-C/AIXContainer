package zju.vipa.container.center.db.entity;


public class User {

  private String id;
  private String oAuthId;
  private String oAuthType;
  private String firstName;
  private String secondName;
  private String city;
  private String phone;
  private String email;
  private String profileLink;
  private String profilePic;
  private String status;
  private String notificationToken;
  private String password;
  private String notes;
  private java.sql.Timestamp createdTimestamp;
  private java.sql.Timestamp updatedTimestamp;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getOAuthId() {
    return oAuthId;
  }

  public void setOAuthId(String oAuthId) {
    this.oAuthId = oAuthId;
  }


  public String getOAuthType() {
    return oAuthType;
  }

  public void setOAuthType(String oAuthType) {
    this.oAuthType = oAuthType;
  }


  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }


  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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


  public String getProfileLink() {
    return profileLink;
  }

  public void setProfileLink(String profileLink) {
    this.profileLink = profileLink;
  }


  public String getProfilePic() {
    return profilePic;
  }

  public void setProfilePic(String profilePic) {
    this.profilePic = profilePic;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getNotificationToken() {
    return notificationToken;
  }

  public void setNotificationToken(String notificationToken) {
    this.notificationToken = notificationToken;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }


  public java.sql.Timestamp getCreatedTimestamp() {
    return createdTimestamp;
  }

  public void setCreatedTimestamp(java.sql.Timestamp createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }


  public java.sql.Timestamp getUpdatedTimestamp() {
    return updatedTimestamp;
  }

  public void setUpdatedTimestamp(java.sql.Timestamp updatedTimestamp) {
    this.updatedTimestamp = updatedTimestamp;
  }

  @Override
  public String toString() {
    return "User{" +
            "id='" + id + '\'' +
            ", oAuthId='" + oAuthId + '\'' +
            ", oAuthType='" + oAuthType + '\'' +
            ", firstName='" + firstName + '\'' +
            ", secondName='" + secondName + '\'' +
            ", city='" + city + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", profileLink='" + profileLink + '\'' +
            ", profilePic='" + profilePic + '\'' +
            ", status='" + status + '\'' +
            ", notificationToken='" + notificationToken + '\'' +
            ", password='" + password + '\'' +
            ", notes='" + notes + '\'' +
            ", createdTimestamp=" + createdTimestamp +
            ", updatedTimestamp=" + updatedTimestamp +
            '}';
  }
}
