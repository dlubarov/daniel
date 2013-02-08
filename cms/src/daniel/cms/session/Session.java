package daniel.cms.session;

import daniel.data.option.Option;
import daniel.web.util.UuidUtils;

public final class Session {
  public static final class Builder {
    private Option<String> uuid = Option.none();
    private Option<String> accountUuid = Option.none();

    public Builder setUuid(String uuid) {
      this.uuid = Option.some(uuid);
      return this;
    }

    public Builder setRandomUiid() {
      return setUuid(UuidUtils.randomAlphanumericUuid());
    }

    public Builder setAccountUuid(Option<String> accountUuid) {
      this.accountUuid = accountUuid;
      return this;
    }

    public Builder setAccountUuid(String accountUuid) {
      return setAccountUuid(Option.some(accountUuid));
    }

    public Session build() {
      return new Session(this);
    }
  }

  private final String uuid;
  private final Option<String> accountUuid;

  private Session(Builder builder) {
    uuid = builder.uuid.getOrThrow();
    accountUuid = builder.accountUuid;
  }

  public String getUuid() {
    return uuid;
  }

  public Option<String> getAccountUuid() {
    return accountUuid;
  }
}
