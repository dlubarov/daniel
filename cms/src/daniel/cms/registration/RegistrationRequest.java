package daniel.cms.registration;

public class RegistrationRequest {
  private final String email;
  private final String password;

  public RegistrationRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
