package daniel.bdb;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.OperationStatus;
import daniel.data.sequence.Sequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.data.util.Check;
import java.io.File;

public final class RawDatabase {
  private final Database db;

  public RawDatabase(File envHome) {
    Check.that(envHome.isDirectory() || envHome.mkdirs());

    EnvironmentConfig envConfig = new EnvironmentConfig();
    envConfig.setTransactional(true);
    envConfig.setAllowCreate(true);
    Environment env = new Environment(envHome, envConfig);

    DatabaseConfig dbConfig = new DatabaseConfig();
    dbConfig.setAllowCreate(true);
    db = env.openDatabase(null, "all", dbConfig);
  }

  public RawDatabase(String envHome) {
    this(new File(envHome));
  }

  public byte[] get(byte[] key) {
    DatabaseEntry result = new DatabaseEntry();
    db.get(null, new DatabaseEntry(key), result, null);
    return result.getData();
  }

  public void put(byte[] key, byte[] value) {
    db.put(null, new DatabaseEntry(key), new DatabaseEntry(value));
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
