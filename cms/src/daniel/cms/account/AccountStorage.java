package daniel.cms.account;

import daniel.bdb.SerializingDatabase;
import daniel.cms.Config;
import daniel.data.option.Option;
import daniel.data.serialization.GsonSerializer;
import daniel.data.serialization.StringSerializer;

public class AccountStorage {
  private static final SerializingDatabase<String, Account> byUuid =
      new SerializingDatabase<>(Config.getDatabaseHome("accounts"),
          StringSerializer.singleton, new GsonSerializer<>(Account.class));

  private static final SerializingDatabase<String, String> indexByEmail =
      new SerializingDatabase<>(Config.getDatabaseHome("accounts-index-by-email"),
          StringSerializer.singleton, StringSerializer.singleton);

  private static final SerializingDatabase<String, String> indexByUsername =
      new SerializingDatabase<>(Config.getDatabaseHome("accounts-index-by-username"),
          StringSerializer.singleton, StringSerializer.singleton);

  private AccountStorage() {}

  public static synchronized Option<Account> getAccountByUuid(String uuid) {
    return byUuid.get(uuid);
  }

  public static synchronized Option<Account> getAccountByEmail(String email) {
    Option<String> optUuid = indexByEmail.get(email);
    if (optUuid.isEmpty())
      return Option.none();
    return getAccountByUuid(optUuid.getOrThrow());
  }

  public static synchronized Option<Account> getAccountByUsername(String username) {
    Option<String> optUuid = indexByUsername.get(username);
    if (optUuid.isEmpty())
      return Option.none();
    return getAccountByUuid(optUuid.getOrThrow());
  }

  public static synchronized void saveNewAccount(Account account) {
    byUuid.put(account.getUuid(), account);
  }

  public static synchronized void deleteAccount(Account account) {
    byUuid.delete(account.getUuid());
    indexByEmail.delete(account.getEmail());
    indexByUsername.delete(account.getUsername());
  }

  public static synchronized void updateAccount(Account account) {
    deleteAccount(account);
    saveNewAccount(account);
  }
}
