package daniel.bdb;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.OperationStatus;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.data.util.Check;
import java.io.File;

final class RawDatabase {
  private final Database db;

  public RawDatabase(File envHome) {
    Check.that(envHome.isDirectory() || envHome.mkdirs());

    EnvironmentConfig envConfig = new EnvironmentConfig()
        .setAllowCreate(true)
        .setTransactional(false);
    DatabaseConfig dbConfig = new DatabaseConfig()
        .setAllowCreate(true)
        .setTransactional(false)
        .setDeferredWrite(false);

    Environment env = new Environment(envHome, envConfig);
    db = env.openDatabase(null, "all", dbConfig);
  }

  public RawDatabase(String envHome) {
    this(new File(envHome));
  }

  public Option<byte[]> tryGet(byte[] key) {
    DatabaseEntry result = new DatabaseEntry();
    OperationStatus status = db.get(null, new DatabaseEntry(key), result, null);
    return status == OperationStatus.SUCCESS
        ? Option.some(result.getData())
        : Option.<byte[]>none();
  }

  public void put(byte[] key, byte[] value) {
    db.put(null, new DatabaseEntry(key), new DatabaseEntry(value));
    db.getEnvironment().flushLog(false);
  }

  public Sequence<byte[]> getAllKeys() {
    CursorConfig cursorConfig = new CursorConfig();
    Cursor cursor = db.openCursor(null, cursorConfig);
    DatabaseEntry key = new DatabaseEntry(), value = new DatabaseEntry();
    value.setPartial(0, 0, true);
    MutableStack<byte[]> allKeys = DynamicArray.create();
    while (cursor.getNext(key, value, null) != OperationStatus.NOTFOUND)
      allKeys.pushBack(key.getData());
    cursor.close();
    return allKeys;
  }

  public Sequence<byte[]> getAllValues() {
    CursorConfig cursorConfig = new CursorConfig();
    Cursor cursor = db.openCursor(null, cursorConfig);
    DatabaseEntry key = new DatabaseEntry(), value = new DatabaseEntry();
    key.setPartial(0, 0, true);
    MutableStack<byte[]> allValues = DynamicArray.create();
    while (cursor.getNext(key, value, null) != OperationStatus.NOTFOUND)
      allValues.pushBack(value.getData());
    cursor.close();
    return allValues;
  }
}
