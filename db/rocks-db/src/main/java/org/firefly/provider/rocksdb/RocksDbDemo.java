package org.firefly.provider.rocksdb;

import org.rocksdb.BackupEngine;
import org.rocksdb.BackupableDBOptions;
import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.BloomFilter;
import org.rocksdb.CompressionType;
import org.rocksdb.Env;
import org.rocksdb.Options;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;

import java.nio.charset.StandardCharsets;

public class RocksDbDemo {
    static {
        RocksDB.loadLibrary();
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.setCreateIfMissing(true);
        options.setCompressionType(CompressionType.NO_COMPRESSION);
        options.setBottommostCompressionType(CompressionType.ZSTD_COMPRESSION);
        options.setLevelCompactionDynamicLevelBytes(true);
        options.setMaxOpenFiles(32);
        options.setIncreaseParallelism(1);
        options.useFixedLengthPrefixExtractor(16);

        BlockBasedTableConfig blockBasedTableConfig = new BlockBasedTableConfig();
        blockBasedTableConfig.setBlockSize(16 * 1024);
        blockBasedTableConfig.setBlockCacheSize(32 * 1024 * 1024);
        blockBasedTableConfig.setCacheIndexAndFilterBlocks(true);
        blockBasedTableConfig.setPinL0FilterAndIndexBlocksInCache(true);
        blockBasedTableConfig.setFilter(new BloomFilter(10, false));
        options.setTableFormatConfig(blockBasedTableConfig);

        RocksDB db = null;
        try {
            db = RocksDB.open(options, ".\\rocksdb\\target\\MyRocksDb");


            db.put("db".getBytes(StandardCharsets.UTF_8), "rocksdb".getBytes(StandardCharsets.UTF_8));
            System.out.println("db: " + new String(db.get("db".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        } catch (RocksDBException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private static void backup(RocksDB db) {
        BackupableDBOptions backupableDBOptions = new BackupableDBOptions(".\\rocksdb\\target\\MyRocksDbBackUp");
        BackupEngine backupEngine = null;
        try {
            backupEngine = BackupEngine.open(Env.getDefault(), backupableDBOptions);
            backupEngine.createNewBackup(db, true);
        } catch (RocksDBException e) {
            e.printStackTrace();
        } finally {
            if (backupEngine != null) {
                backupEngine.close();
            }
            backupableDBOptions.close();
        }
    }

    private static void bulkRead(RocksDB db) {
        RocksIterator iterator = db.newIterator();
        try {
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                System.out.println(new String(iterator.key(), StandardCharsets.UTF_8));
            }
        } finally {
            iterator.close();
        }
    }

    private byte[] prefixLoopup(RocksDB db, byte[] prefixBytes) {
        ReadOptions readOpts = null;
        try {
            readOpts = new ReadOptions();
            readOpts.setPrefixSameAsStart(true);
            readOpts.setVerifyChecksums(false);

            RocksIterator it = db.newIterator(readOpts);
            it.seek(prefixBytes);
            if (it.isValid()) {
                return it.value();
            }
        } finally {
            if (readOpts != null) {
                readOpts.close();
            }
        }
        return null;
    }

    private static void batchWrite(RocksDB db) {
        WriteBatch writeBatch = new WriteBatch();
        WriteOptions writeOptions = new WriteOptions();
        try {
            writeBatch.delete("db".getBytes(StandardCharsets.UTF_8));
            writeBatch.put("db".getBytes(StandardCharsets.UTF_8), "rocksdb".getBytes(StandardCharsets.UTF_8));
            db.write(writeOptions, writeBatch);
        } catch (RocksDBException e) {
            e.printStackTrace();
        } finally {
            writeOptions.close();
            writeBatch.clear();
        }
    }
}
