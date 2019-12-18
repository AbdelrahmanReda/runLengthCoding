import java.io.*;
import java.net.PortUnreachableException;
import java.util.ArrayList;

public class main {

    private static int getCategory(int number)
    {
        return Integer.toBinaryString(number).length();
    }
    private static  ArrayList<Pair> getZerosBefore(String input){
        ArrayList<Pair> pairs = new ArrayList<>();
        int zeroBeforeCharecterCounter = 0 ;
        boolean minus = false;
        for (int i = 0; i <input.length(); i++) {

            if (input.charAt(i)=='0')
            {
                zeroBeforeCharecterCounter ++ ;
            }
            else if (input.charAt(i)=='-')
            {
               continue;
            }
            else
            {
                Pair pair= new Pair();
                pair.zerosBefor = zeroBeforeCharecterCounter;
                pair.number=getCategory(Integer.parseInt(Character.toString(input.charAt(i))));
                pairs.add(pair);
                zeroBeforeCharecterCounter=0;
            }
        }
        return pairs;
    }
    private static  ArrayList <Node> getHuffmanCodes(ArrayList<Pair> table){
        String prepareMessage ="";
        for (int i = 0; i <table.size() ; i++){
            prepareMessage+=table.get(i).zerosBefor+"/"+table.get(i).number+"-";
        }
        prepareMessage=prepareMessage.substring(0,prepareMessage.length()-1);
        stdHuffman stdHuffmanObj = new stdHuffman(prepareMessage,true);
        return stdHuffmanObj.returnTable();
    }
    private static String getAdditionalBit(int number){
        String binaryNumber = Integer.toBinaryString(Math.abs(number));
        if (number<0)
        {
            StringBuilder binaryNumberBuler = new StringBuilder(binaryNumber);
            for (int i = 0; i <binaryNumberBuler.length() ; i++) {
                if (binaryNumber.charAt(i)=='0')
                    binaryNumberBuler.setCharAt(i,'1');
                else
                    binaryNumberBuler.setCharAt(i,'0');
            }
            binaryNumber=binaryNumberBuler.toString();
        }
        return binaryNumber ;
    }
    private static void writeTofile(ArrayList<Node> firstLine, String compressedMessage) throws IOException {
        String firstLinerData ="";
        for (int i = 0; i <firstLine.size() ; i++) {
            firstLinerData+=firstLine.get(i).symbol+','+firstLine.get(i).proibability+" ";
        }
        firstLinerData.substring(0,firstLinerData.length()-1);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "\\Desktop\\compressed.txt"));
        writer.write(firstLinerData);
        writer.write("\n"+compressedMessage);
        writer.close();
    }
    private static ArrayList<String> readFile() throws IOException{
        ArrayList<String>fileLines = new ArrayList<>();
        File file = new File(System.getProperty("user.home") + "\\Desktop\\compressed.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String lineReader;
        while ((lineReader = br.readLine()) != null) {fileLines.add(lineReader);}
        return fileLines;
    }
    private static String getOnecComplement(String binaryNumber) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i <binaryNumber.length() ; i++){
            if (binaryNumber.charAt(i)=='1')
                temp.append('0');
            else
                temp.append('1');
        }
        return temp.toString();
    }
    private static int getAcValue(String binaryNumber) {
        if (binaryNumber.charAt(0)=='0')//negative
             {
            return -1*Integer.parseInt(getOnecComplement(binaryNumber),2);
        }
        else{
            return Integer.parseInt(binaryNumber,2);
        }
    }
    private static String compress(String message){
        ArrayList<Pair> pairs = getZerosBefore(message);
        ArrayList<pairAnother> pairAnotherirs = new ArrayList<>();
        for (int i = 0; i <message.length() ; i++) {
            if (message.charAt(i)!='0') {
                pairAnother pairAnotheObj = new pairAnother();
                if (message.charAt(i)=='-') {
                    pairAnotheObj.additionalBits=getAdditionalBit(-1*(Integer.parseInt(Character.toString(message.charAt(i+1)))));
                    i=i+1;
                }
                else {
                    pairAnotheObj.additionalBits=getAdditionalBit(Integer.parseInt(Character.toString(message.charAt(i))));
                }
                pairAnotherirs.add(pairAnotheObj);
            }
        }
        ArrayList<Node> huffmanTable = getHuffmanCodes(pairs);
        for (int i = 0; i <pairs.size() ; i++) {
            String zerosOverCategorry = pairs.get(i).zerosBefor+"/"+pairs.get(i).number ;
            for (Node node : huffmanTable) {
                if (zerosOverCategorry.equals(node.symbol)) {
                    pairAnotherirs.get(i).huffmanCode = node.code;
                    break;
                }
            }
        }
        StringBuilder compressedMessage = new StringBuilder();
        for (pairAnother pairAnotherir : pairAnotherirs) {
            compressedMessage.append(pairAnotherir.huffmanCode).append(",").append(pairAnotherir.additionalBits).append(" ");
        }
        for (Node node : huffmanTable) {
            if (node.symbol.equals("EOB")) {
                compressedMessage.append(node.code);
            }
        }
        try {
            writeTofile(huffmanTable, compressedMessage.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedMessage.toString();
    }
    private static void decompress()  {
        ArrayList<String> fileLines = new ArrayList<>(); //stores file lines 
        try {
            fileLines = readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stdHuffman huffman = new stdHuffman(fileLines.get(0),false);
        ArrayList<Node> huffmanTable= huffman.returnTable();
        String[] parsedLine = fileLines.get(1).split(" ");
        ArrayList<String>  greenLine = new ArrayList<>();
        ArrayList<Integer> intigerMirror = new ArrayList<>();
        parsedLine[parsedLine.length-1]+=",!";
        for (String value : parsedLine) {
            String[] parsedTwoo = value.split(",");
            if (!parsedTwoo[1].equals("!")) {
                intigerMirror.add(getAcValue(parsedTwoo[1]));
            }
        }
        for (String s : parsedLine) {
            for (Node node : huffmanTable) {
                String[] parsed_of_ParsedLine = s.split(",");
                if (node.code.equals(parsed_of_ParsedLine[0])) {
                    greenLine.add(node.symbol);
                }
            }
        }
        String decompressed="";
        for (int i = 0; i <greenLine.size()-1 ; i++) {
            String[] parsed = greenLine.get(i).split("/");
            for (int j = 0; j <Integer.parseInt(parsed[0]) ; j++) {
               decompressed+="0";
            }
            decompressed+=intigerMirror.get(i);
        }
        decompressed+="EOB";
        System.out.println(decompressed);
    }

    public static void main (String args[])
    {
        String message = "-500200320100-20-100100-1";
        System.out.println(compress(message));
        decompress();
    }
}
