import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class IndexImp {
    public static double indexDoc(LinkedList<DocumentImp> docs, HashMap<String, HashMap<String, Double>> outputs) {
        Iterator<DocumentImp> it = docs.iterator();
        double tmpMax = -1;
        System.out.println(">>>>>Start indexing Documents");
        while (it.hasNext()) {
            DocumentImp tmpDoc = it.next();
            ArrayList<String> tokens = tmpDoc.getTokens();
            String id = tmpDoc.getDocNum();
            for (String str : tokens) {
                str = str.toLowerCase();
                if (outputs.containsKey(str)) {
                    HashMap<String, Double> tmpRow = outputs.get(str);
                    if (tmpRow.containsKey(id)) {
                        Double tmpVal = tmpRow.get(id) + 1;
                        tmpRow.replace(id, tmpVal);
                        if (tmpVal > tmpMax) {
                            tmpMax = tmpVal;
                        }
                    } else {
                        tmpRow.put(id, 1.0);
                    }
                } else {
                    HashMap<String, Double> newRow = new HashMap<String, Double>();
                    newRow.put(id, 1.0);
                    outputs.put(str, newRow);
                    if (tmpMax < 1) {
                        tmpMax = 1;
                    }
                }
            }
        }
        System.out.println(">>>>>Indexing Documents is completed.");
        System.out.println("Max F for documents: " + tmpMax);
        System.out.println("Total Token number: " + outputs.size());
        return tmpMax;
    }

    public static void updateDocWeight(HashMap<String, HashMap<String, Double>> outputs, double maxF, int docSize) {
        System.out.println(">>>>>Start updating document weights");
        for (Entry<String, HashMap<String, Double>> column : outputs.entrySet()) {
            HashMap<String, Double> currentRow = column.getValue();
            int df = currentRow.size();
            double idf = Math.log(docSize / df) / Math.log(2);
            currentRow.forEach((docID, tmpF) -> {
                currentRow.replace(docID, ((tmpF / maxF) * idf));
            });
            currentRow.put("0", idf);
        }
        System.out.println(">>>>>Updated document weights");
    }

    public static double indexQueries(ArrayList<QueryImp> queries, HashMap<String, HashSet<String>> synonymsTable) {
        double maxF = 0;
        System.out.println(">>>>>Start indexing queries");
        for (QueryImp query : queries) {
            for (String token : query.getTokens()) {
                token = token.toLowerCase();
                Double num = query.tokenSet.get(token);
                if (num != null) {
                    Double newNum = num + 1;
                    query.tokenSet.replace(token, newNum);
                } else {
                    query.tokenSet.put(token, 1.0);
                    if (maxF == 0) {
                        maxF = 1;
                    }
                }
                if (num != null && num > maxF) {
                    maxF = num;
                }
                HashSet<String> synonyms = synonymsTable.get(token);
                if (synonyms == null || synonyms.size() == 0) {
                    continue;
                }
                for (String synonym : synonyms) {
                    num = query.tokenSet.get(synonym);
                    if (num != null) {
                        Double newNum = num + 1;
                        query.tokenSet.replace(synonym, newNum);
                    } else {
                        query.tokenSet.put(synonym, 1.0);
                    }
                    if (num != null && num > maxF) {
                        maxF = num;
                    }
                }
            }
        }
        System.out.println(">>>>>Indexing Queries is completed.");
        System.out.println("Max F for queries: " + maxF);
        return maxF;

    }

    public static void updateQueryWeight(ArrayList<QueryImp> queries, double maxF,
            HashMap<String, HashMap<String, Double>> indexResult) {
        System.out.println(">>>>>Start updating Query weights");
        for (QueryImp query : queries) {
            for (Entry<String, Double> token : query.tokenSet.entrySet()) {
                String tokenStr = token.getKey();
                Double tokenVal = token.getValue();
                if (indexResult.containsKey(tokenStr)) {
                    query.tokenSet.replace(tokenStr, (tokenVal / maxF) * indexResult.get(tokenStr).get("0"));
                }
            }
            double queryLength = 0.0;
            for (Entry<String, Double> token : query.tokenSet.entrySet()) {
                queryLength = queryLength + Math.pow(token.getValue(), 2);
            }
            query.length = queryLength;
        }
        System.out.println(">>>>>Updated query weights");
    }

    public static void main(String[] args) {
        HashMap<String, Integer> prices = new HashMap<>();
        prices.put("Shoes", 200);
        prices.put("Bag", 300);
        prices.put("Pant", 150);
        System.out.println("Normal Price: " + prices);

        System.out.print("Discounted Price: ");

        prices.forEach((key, value) -> {
            value = value - value * 10 / 100;
            System.out.print(key + "=" + value + " ");
        });

        System.out.println(prices.toString());
    }
}
