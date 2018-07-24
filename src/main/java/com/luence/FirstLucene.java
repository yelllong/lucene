package com.luence;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 8:34
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class FirstLucene {
    @Test
    public void createIndex() throws IOException {
        //创建 一个 索引创建对象 IndexWriter
        //标注分词器
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
        //指定Lucene版本  分词器
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        //索引输出目录
        Directory directory = FSDirectory.open(new File("f:/index"));
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //Document文档对象(代表一个文件) field域对象(文件相关属性：名字，作者，正文)
        //正文
        Document document = new Document();
        Field title = new StringField("title", "春晓2", Field.Store.YES);
        Field author = new StringField("author", "贺知章2", Field.Store.YES);
        Field context = new StringField("context", "春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。", Field.Store.YES);
        document.add(title);
        document.add(author);
        document.add(context);
        //建立索引
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
    }
    @Test
    public void useIndex() throws IOException {
        //创建索引检索对象  indexSeacher
        //指定索引所在目录
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("f:/index")));
        //创建 索引 查询方式对象
        IndexSearcher searcher = new IndexSearcher(reader);
        //搜索指定目录的索引
        TermQuery termQuery = new TermQuery(new Term("author", "贺知章2"));
        //参数一：查询方式 参数二：查询后返回的数量
        //返回的topDocs 对象封装了查询结果
        TopDocs topDocs = searcher.search(termQuery, 10);
        //查询文档根据得分排名后的结果 集合 相关度评分 分数越高，相关度越高
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //查询文档符合结果的数量
        int totalHits = topDocs.totalHits;
        System.out.println("一共得到了多少个文档："+totalHits);
        //遍历文档数组， 获取文档信息
        for (ScoreDoc scoreDoc : scoreDocs) {
            //对应文档的id
            int doc = scoreDoc.doc;
            //该文档的相关度得分
            float score = scoreDoc.score;
            System.out.println("这篇文章的得分为："+score);
            Document doc1 = searcher.doc(doc);
            System.out.println("文章的标题为："+doc1.get("title"));
            System.out.println("文章的标题为："+doc1.get("author"));
            System.out.println("文章的标题为："+doc1.get("context"));
        }
    }
}

