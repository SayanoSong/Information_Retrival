import java.util.stream.Collectors;
import java.lang.Math;
import java.util.*;

public class RankImp {

    public static void calculateCosSims(LinkedList<DocumentImp> docs, ArrayList<QueryImp> queries,
            HashMap<String, HashMap<String, Double>> indexResult) {
        for (QueryImp query : queries) {
            System.out.println(">>>>>Calculating CosSim for Query" + query.getID());
            calculateCosSim(docs, query, indexResult);
        }
    }

    public static void calculateCosSim(LinkedList<DocumentImp> docs, QueryImp query,
            HashMap<String, HashMap<String, Double>> indexResult) {
        Iterator<DocumentImp> it = docs.iterator();
        while (it.hasNext()) {
            double tmpSim = 0;
            double tmpCosSim;
            DocumentImp tmpDoc = it.next();
            String tmpDocID = tmpDoc.getDocNum();
            Set<String> tokens = new HashSet<>(tmpDoc.getTokens());
            double docLength = 0.0;
            for (String token : tokens) {
                double weightInDoc = indexResult.get(token).get(tmpDocID);
                docLength = docLength + Math.pow(weightInDoc, 2);
                if (query.tokenSet.containsKey(token)) {
                    tmpSim = tmpSim + weightInDoc * query.tokenSet.get(token);
                }
            }
            if (docLength == 0) {
                continue;
            }
            docLength = Math.sqrt(docLength);
            tmpCosSim = tmpSim / (query.length * docLength);
            query.rank.put(tmpDocID, tmpCosSim);
        }
        query.rank = query.rank.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
