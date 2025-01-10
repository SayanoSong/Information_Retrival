import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.*;

public class Main {
    public static String STOPWORD_PATH = "Stopwords";
    public static String QUERY_PATH = "query.txt";
    public static String DATA_FOLDER_PATH = "AP_collection/coll";
    public static boolean isTableExist = true;

    public static void main(String[] args) {
        LinkedList<DocumentImp> docs = new LinkedList<DocumentImp>();
        ArrayList<QueryImp> queries = new ArrayList<QueryImp>();
        ArrayList<String> stopWords = FileReaderImp.readStopwords(STOPWORD_PATH);
        if (!isTableExist) {
            ArrayList<String> queryStr = FileReaderImp.readStopwords(QUERY_PATH);
            queryStr.forEach((e) -> {
                e.toLowerCase();
            });
            FileReaderImp.readQue(queries, queryStr, stopWords);
            FileWriterImp.writeTokenFile(queries);
            return;
        }
        HashMap<String, HashSet<String>> synonymsTable = new HashMap<String, HashSet<String>>();
        FileReaderImp.getSynonymsTable(synonymsTable, "Synonyms.txt");
        ArrayList<String> queryStr = FileReaderImp.readFile(QUERY_PATH);
        HashMap<String, HashMap<String, Double>> indexResult = new HashMap<String, HashMap<String, Double>>();
        FileReaderImp.readAllDocs(docs, DATA_FOLDER_PATH, stopWords);
        FileReaderImp.readQue(queries, queryStr, stopWords);
        double maxF = IndexImp.indexDoc(docs, indexResult);
        IndexImp.updateDocWeight(indexResult, maxF, docs.size());
        maxF = IndexImp.indexQueries(queries, synonymsTable);
        IndexImp.updateQueryWeight(queries, maxF, indexResult);
        RankImp.calculateCosSims(docs, queries, indexResult);
        String result = FileWriterImp.getResultStr(queries, "A2Run2_1");
        FileWriterImp.writeResults(result);
    }
}
