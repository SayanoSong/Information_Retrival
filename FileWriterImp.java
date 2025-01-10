import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Iterator;

public class FileWriterImp {
    public static void writeFile(String filePath, String data) {
        File resultFile = new File(filePath);
        try {
            FileOutputStream fop = new FileOutputStream(resultFile);
            OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
            writer.append(data);
            writer.close();
            fop.close();
        } catch (Exception e) {
            System.out.println("Writing fail: " + e);
        }
    }

    public static String getOutputFormat(Integer queryNum, String docId, Integer rank, Double score, String tag) {
        String output = String.format("%d Q0 %s %d %f %s", queryNum, docId, rank, score, tag);
        return output;
    }

    public static void writeResults(String resultStr) {
        System.out.println(">>>>>Start writing results");
        writeFile("Results", resultStr);
        System.out.println(">>>>>Writing results completed");
    }

    public static String getResultStr(ArrayList<QueryImp> queries, String tag) {
        System.out.println(">>>>>Start collecting results");
        String resultStr = "";
        String status = String.join("", Collections.nCopies(50, "|"));
        System.out.println(status);
        for (QueryImp query : queries) {
            int i = 1;
            for (Entry<String, Double> result : query.rank.entrySet()) {
                if (i > 1000) {
                    break;
                }
                resultStr += FileWriterImp.getOutputFormat(query.getID(), result.getKey().toUpperCase(), i,
                        (double) Math.round(result.getValue() * 100000d) / 100000d, tag) + "\r\n";
                i++;
            }
            System.out.print("|");
        }
        System.out.println("\r\n>>>>>Collecting results completed");
        return resultStr;
    }

    public static void writeTokenFile(ArrayList<QueryImp> queries) {
        Iterator<QueryImp> iterator = queries.iterator();
        HashSet<String> sites = new HashSet<String>();
        while (iterator.hasNext()) {
            QueryImp tmpQuery = iterator.next();
            for (String token : tmpQuery.getTokens()) {
                sites.add(token);
            }
        }
        String outputs = sites.toString();
        writeFile("QueryTokens", outputs);
    }

    public static void main(String[] args) {
    }
}
