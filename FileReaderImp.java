import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.File;

public class FileReaderImp {
    public static ArrayList<String> readStopwords(String filePath) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String str;
            while ((str = reader.readLine()) != null) {
                result.add(str);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return null;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        return result;
    }

    public static ArrayList<String> readFile(String filePath) {
        ArrayList<String> result;
        try {
            result = Stemmer.readFile(filePath);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getAllFilePath(String folderPath) {
        ArrayList<String> filePaths = new ArrayList<String>();
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            String currentPath = folder.getPath();
            String list[] = folder.list();
            for (int i = 0; i < list.length; i++) {
                filePaths.add(currentPath + '\\' + list[i]);
            }
        }
        return filePaths;
    }

    public static void readAllDocs(LinkedList<DocumentImp> docs, String folderPath, ArrayList<String> stopwords) {
        ArrayList<String> allPath = getAllFilePath(folderPath);
        Iterator<String> iterator = allPath.iterator();
        System.out.println(">>>>>Starting Step 1.");
        while (iterator.hasNext()) {
            String tmpPath = iterator.next();
            readDoc(docs, readFile(tmpPath), stopwords);
            System.out.println("File " + tmpPath + " has been read.");
        }
        System.out.println(">>>>>Step 1: Reading files is completed.");
        System.out.println("Total Document Number: " + docs.size());
    }

    public static void readDoc(LinkedList<DocumentImp> docs, ArrayList<String> data, ArrayList<String> stopwords) {
        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next().strip();
            if (line.equals("<doc>")) {
                DocumentImp newDoc = new DocumentImp();
                String[] byLines = new String[2];
                Boolean textFlag = false;
                String textStr = "";
                while (iterator.hasNext() && !textFlag) {
                    line = iterator.next().strip();
                    if (line.contains("<docno>") && line.contains("</docno>")) {
                        line = line.replace("<docno>", "").replace("</docno>", "").strip();
                        newDoc.setDocNum(line);
                    } else if (line.contains("<fileid>") && line.contains("</fileid>")) {
                        line = line.replace("<fileid>", "").replace("</fileid>", "").strip();
                        newDoc.setFileId(line);
                    } else if (line.contains("<1st_line>") && line.contains("</1st_line>")) {
                        line = line.replace("<1st_line>", "").replace("</1st_line>", "").strip();
                        newDoc.setLine1(line);
                    } else if (line.contains("<2nd_line>") && line.contains("</2nd_line>")) {
                        line = line.replace("<2nd_line>", "").replace("</2nd_line>", "").strip();
                        newDoc.setLine2(line);
                    } else if (line.contains("<head>") && line.contains("</head>")) {
                        line = line.replace("<head>", "").replace("</head>", "").strip();
                        newDoc.setHeader(line);
                    } else if (line.contains("<bylin>") && line.contains("</bylin>")) {
                        line = line.replace("<bylin>", "").replace("</bylin>", "").strip();
                        if (byLines[0] == null) {
                            byLines[0] = line;
                        } else {
                            byLines[1] = line;
                            newDoc.setByLine(byLines);
                        }
                    } else if (line.contains("<datelin>") && line.contains("</datelin>")) {
                        line = line.replace("<datelin>", "").replace("</datelin>", "").strip();
                        newDoc.setDate(line);
                    } else if (line.equals("<text>")) {
                        while (iterator.hasNext()) {
                            line = iterator.next().strip();
                            if (line.equals("</text>")) {
                                break;
                            }
                            line = line.replaceAll("-", " ").replaceAll("\\p{Punct}", "").replaceAll("\\d", "")
                                    .toLowerCase();
                            textStr += line + " ";
                        }
                    } else if (line.contains("</doc>")) {
                        break;
                    }
                }
                newDoc.setText(textStr);
                String[] tokens = textStr.replaceAll("\\p{Punct}", "").replaceAll("\\d", "")
                        .split("\\s+");
                ArrayList<String> tokenList = new ArrayList<String>();
                for (String i : tokens) {
                    tokenList.add(i);
                }
                tokenList.removeAll(stopwords);
                newDoc.setTokens(tokenList);
                docs.add(newDoc);
            }
        }
    }

    public static void readQue(ArrayList<QueryImp> queries, ArrayList<String> data, ArrayList<String> stopwords) {
        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next().strip();
            if (line.equals("<top>")) {
                QueryImp tmpQuery = new QueryImp();
                boolean textFlag = false;
                String desc = "";
                String narr = "";
                while (iterator.hasNext() && !textFlag) {
                    line = iterator.next().strip().toLowerCase();
                    if (line.contains("<num>")) {
                        line = line.replace("<num>", "").strip();
                        tmpQuery.setID(Integer.parseInt(line));
                    } else if (line.contains("<titl>")) {
                        line = line.replace("<titl>", "").strip();
                        tmpQuery.setTitle(line);
                    } else if (line.contains("<title>")) {
                        line = line.replace("<title>", "").strip();
                        tmpQuery.setTitle(line);
                    } else if (line.contains("<desc>")) {
                        while (iterator.hasNext() && !textFlag) {
                            if ((line = iterator.next().toLowerCase()) != "") {
                                if (line.contains("<narr>")) {
                                    while (iterator.hasNext() && !textFlag) {
                                        if ((line = iterator.next().toLowerCase()) != "") {
                                            if (line.contains("</top>")) {
                                                tmpQuery.setDesc(desc);
                                                tmpQuery.setNarrative(narr);
                                                textFlag = true;
                                            } else {
                                                narr += line;
                                            }
                                        }
                                    }
                                } else {
                                    desc += line;
                                }
                            }
                        }
                    }
                }
                String[] tokens = (tmpQuery.getTitle() + " " + tmpQuery.getNarrative() + " " + tmpQuery.getDesc())
                        .replaceAll("\\p{Punct}", "").replaceAll("\\d", "")
                        .split("\\s+");
                ArrayList<String> tokenList = new ArrayList<String>();
                for (String i : tokens) {
                    tokenList.add(i);
                }
                tokenList.removeAll(stopwords);
                tmpQuery.setTokens(tokenList);
                queries.add(tmpQuery);
            }
        }
    }

    public static void getSynonymsTable(HashMap<String, HashSet<String>> synonymsTable, String filePath) {
        ArrayList<String> data = readFile(filePath);
        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String[] line = iterator.next().split(":");
            if (line.length < 2) {
                continue;
            }
            String key = line[0];
            String[] synonyms = line[1].split(",");
            HashSet<String> tokens = new HashSet<String>();
            for (String synonym : synonyms) {
                if (synonym != "" && !synonym.equals(key)) {
                    tokens.add(synonym);
                }
            }
            synonymsTable.put(key, tokens);
        }
    }

    public static void main(String[] args) {

    }
}
