import codecs
import fasttext
model_en = fasttext.load_model("cc.en.300.bin")
PROCESS_NUM = 5
class DocumentImp:
    documentNum = ""
    fileId = ""
    line1 = ""
    line2 = ""
    header = ""
    byLine = [None, None]
    dateLine = ""
    tokens = []
    cosSim = []

class QueryImp:
    num = -1
    title = ""
    desc = ""
    narrative = ""
    tokens = []
    tokenDic = {}
    rank = {}

def readFile(filePath):
    result = []
    file = open(filePath,"r")
    for line in file:
        result.append(line.strip())
    file.close()
    return result

def getAllSynonyms(data: str):
    data = data[1:-1].split(",")
    tokenSet = {}
    tokens = list(data)
    for token in tokens:
        token = token.strip()
        print("Calculating " + token)
        synonyms = model_en.get_nearest_neighbors(token)
        synonymList = []
        for synonym in synonyms:
            if synonym[0] > 0.8 and synonym[1].isalpha() and (synonym[1] not in tokenSet):
                synonymList.append(synonym[1])
        tokenSet[token] = synonymList
    dataStr = ""
    for key in tokenSet:
        dataStr += key + ":"
        for token in tokenSet[key]:
            dataStr += token + ","
        dataStr += "\n"
    write_file("Synonyms.txt", dataStr)

def write_file(file_path: str, data: str):
    try:
        file_handle = codecs.open(file_path, "w", "utf-8")
        file_handle.write(data)
        file_handle.flush()
    except Exception as err:
        return err
    return True

if (__name__ == "__main__"):
    synonymsData = readFile("QueryTokens")
    getAllSynonyms(synonymsData[0])
