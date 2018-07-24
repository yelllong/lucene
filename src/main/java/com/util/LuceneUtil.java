package com.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 12:06
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class LuceneUtil {
    private static Directory directory;
    private static Analyzer analyzer;
    private static IndexWriterConfig config;
    private static IndexReader indexReader;
    static {
        try {
            directory = FSDirectory.open(new File("f:/index/next"));
//            directory = FSDirectory.open(new File("f:/index/dao"));
            analyzer = new IKAnalyzer();
            config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
            indexReader = DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回一个 索引创建对象
    public static IndexWriter getIndexWriter() {
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(directory, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }
    //返回一个索引检索对象
    public static IndexSearcher getIndexSearcher() {
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        return indexSearcher;
    }
    //提交
    public static void commit(IndexWriter indexWriter) {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //回滚
    public static void rollback(IndexWriter indexWriter) {
        try {
            indexWriter.rollback();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

