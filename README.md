# Information_Retrival

This program implemented an Information Retrieval (IR) system based on the vector space model, for a collection of documents. It implemented an indexing scheme based on the vector space model.

## How to Run

1. Go to the Main.java.
2. Change the "DATAPATH" to your data folder path. (E.G. ./AP_collection/coll)
3. Run Main.main(paras)

---

Explaination:
In the Step 1, firstly the FileReaderImp.getAllFilePath(paras) will list all file paths in the {{DATAPATH}}. Then FileReaderImp.readAllDocs() will call FileReaderImp.readFile() with each path to get all texts in each file. After getting the texts, FileReaderImp.readDoc(paras) will process texts, including removing all punctuation tokens, numbers, stopwords in the <TEXT>. The processed data of documents is stored in the DocumentImp class, all tokens of a document are also stored into the DocumentImp instance, named as "DocumentImp.tokens". One instance of DocumentImp represents only one document. Then all DocumentImp instances are added into a LinkedList to store them. The reason I used LinkedList type is the add(ele) of LinkedList takes a constant time cost (O(1)). The variable name of the place that stores the step 1 result is "docs".
In the Step 2, the "docs" in Step1 is used to index. IndexImp.indexDoc(0) traverses all documents in the "docs". For each DocumentImp, the function traverses all tokens stored in the "DocumentImp.tokens". For each token in a document, according to the table provided in the assignment instruction:

1. if program does not find the index terms which is equals to the current token string, the program will firstly create a new "row", which the index term is the current token string, and a new "column" will be added as the value of the current token, which the key is current document ID and the value is 1.
2. if program finds the index terms, it means this token has appeared in one of the provious documents and/or the current document. If current token appeared in one of the provious documents ONLY, it mean this token is the first time appearing in the current document. The program will add a new "column" in the current "row" with the key is the current document id (D) and the value of 1 (tf); If this token has appeared once or more in the current document, the program will increment the value of the current "column" (tf). During traversing all document, the program will continuly update the maximum term frequency of all tokens. The maximum tf will be returned as the result of the IndexImp.indexDoc(paras). The document frequency of each token is equals to the size of each row.

For the Step 2, I used the data structure HashMap<String, HashMap<String, Integer>>, which the first String is "Index term", the second String is Document ID, and the Integer is the term frequency. The reason I used HashMap is there will be a lot request data of the term frequency of a certain Document of a certain index term. Using HashMap decreases the time complextity of using getter method. The average time complextity is O(1) and the highest time complextity is O(N) or O(logN), based on the implementation.

In the Step 3,I used the data structure HashMap<String, HashMap<String, Float>> to find the value of tf and weight for all tokens by using algorithm IndexImp.updateTf () and Rank.calWeig(fin, weights, num) . And then, dealing with the query data is the same as dealing with documents. I store tokens of each query in String[] and all querys in ArrayList<String[]> which is conventient to call the data later. In the algorithm calQueryWeig(), I calculated the value of fij ,and then tfij, finally traversing all index terms again to find the weight of all queries. Because the tokens of query is much less than each documents, I just keep the existing tokens in query. Therefore, it is easy to calculate the length of each query by calculating the sum of squares. For the length of each documents, I traversed all weights of all tokens to extract all values which is appeared on the same document. Finally, I used the data structure HashMap<String, Float>[] to store cosineSimilarity for the all results. The idex of it represents the relation between current query and all documents. With the algorithm of Rank.rankDoc(), I got the ordered List<Map.Entry<String, Float>> to print similarity scores in decreasing order.
