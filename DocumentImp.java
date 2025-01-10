import java.util.ArrayList;

public class DocumentImp {
    private String documentNum;
    private String fileId;
    private String line1;
    private String line2;
    private String header;
    private String[] byLine;
    private String dateLine;
    private String text;
    private ArrayList<String> tokens;

    public DocumentImp() {
        documentNum = "";
        fileId = "";
        line1 = "";
        line2 = "";
        header = "";
        byLine = new String[2];
        dateLine = "";
        text = "";
        tokens = new ArrayList<String>();
    }

    public DocumentImp(String docNum, String fileId, String line1, String line2, String header, String[] byLine,
            String dateLine,
            String text, ArrayList<String> tokens) {
        this.documentNum = docNum;
        this.fileId = fileId;
        this.line1 = line1;
        this.line2 = line2;
        this.header = header;
        this.byLine = byLine;
        this.dateLine = dateLine;
        this.text = text;
        this.tokens = tokens;
    }

    public void setDocNum(String num) {
        documentNum = num;
    }

    public void setFileId(String id) {
        fileId = id;
    }

    public void setLine1(String line) {
        line1 = line;
    }

    public void setLine2(String line) {
        line2 = line;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setByLine(String[] lines) {
        this.byLine = lines;
    }

    public void setDate(String date) {
        this.dateLine = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    public String getDocNum() {
        return documentNum;
    }

    public String getFileId() {
        return fileId;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getHeader() {
        return header;
    }

    public String getByLine(int index) {
        return index == 1 ? byLine[0] : byLine[1];
    }

    public String getDate() {
        return dateLine;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void printTokens() {
        System.out.println(tokens.toString());
    }
}