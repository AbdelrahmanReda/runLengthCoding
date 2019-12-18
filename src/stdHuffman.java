import java.util.ArrayList;
import java.util.List;

public class stdHuffman {


    private   ArrayList<Node> table = new ArrayList<>();
    private   boolean founded=false;
    private   ArrayList<Node> introduceNewLine (ArrayList <Node> input){
        ArrayList <Node> newLine = new ArrayList<Node>();
        for (int i = 0; i <input.size()-2 ; i++) {
            Node node = new Node();
            node=input.get(i);
            newLine.add(node);
        }
        Node node = new Node();
        node.proibability=input.get(input.size()-2).proibability+input.get(input.size()-1).proibability;
        node.symbol=input.get(input.size()-2).symbol+"+"+input.get(input.size()-1).symbol;
        newLine.add(node);
        return newLine;
    }
    
    private   ArrayList<Node> orderList (ArrayList <Node> input){

        for (int i = 1; i <input.size() ; i++) {
            for (int j = 0; j <input.size()-1 ; j++) {

                if (input.get(j).proibability<input.get(j+1).proibability)
                {
                    Node fNode = input.get(j);
                    Node sNode = input.get(j+1);
                    input.set(j,sNode);
                    input.set(j+1,fNode);
                }
            }
        }
        return input;
    }
    
    public  ArrayList<Node> returnTable (){
        return table;
    }
    
    public ArrayList<ArrayList<Node>> getOuterList(String linearTable){
        ArrayList<ArrayList<Node>> outerList = new ArrayList<>();
       ArrayList<Node> oneColumn = new ArrayList<>();
        String[] parser = linearTable.split(" ");
        for (int i = 0; i <parser.length ; i++) {
            String[] record = parser[i].split(",");
            Node node = new Node();
            node.symbol=record[0];
            node.proibability=Double.parseDouble(record[1]);
            oneColumn.add(node);
        }
        outerList.add(oneColumn);
        return  outerList;
    }

    public stdHuffman (String messageIn,boolean compressionFlag)
    {
        ArrayList<ArrayList<Node>> aList = new ArrayList<ArrayList<Node>>();
            if(compressionFlag)
            messageIn += "-EOB";
            ArrayList<String> charecters = new ArrayList<>();
            ArrayList<Double> repeatation = new ArrayList<Double>();
            String message = messageIn;
            String[] parsedMessage = message.split("-");
            for (int i = 0; i < parsedMessage.length; i++) {
                Double counter = 1.0;
                for (int j = i + 1; j < parsedMessage.length; j++) {
                    if (parsedMessage[i].equals(parsedMessage[j]))
                        counter++;
                }
                if (!charecters.contains(parsedMessage[i])) {
                    charecters.add(parsedMessage[i]);
                    repeatation.add(counter);
                }
                counter = 1.0;
            }
            ArrayList<Node> allNodes = new ArrayList<Node>();
            if (compressionFlag) {
                for (int i = 0; i < charecters.size(); i++) {
                    Node node = new Node();
                    node.symbol = charecters.get(i);
                    node.proibability = repeatation.get(i) / (parsedMessage.length);
                    allNodes.add(node);
                }
                aList.add(orderList(allNodes));
            }
            else
            {
                String[] parsed = message.split(" ");
                for (int i = 0; i <parsed.length ; i++) {
                    Node node = new Node();
                    String[] parsedTwo = parsed[i].split(",");
                    node.symbol=parsedTwo[0];
                    node.proibability = Double.parseDouble(parsedTwo[1]);
                    allNodes.add(node);
                }
                aList.add(orderList(allNodes));
                for (int i = 0; i <parsed.length-2 ; i++) {
                    aList.add(orderList(introduceNewLine(aList.get(i))));
                }
            }
            for (int i = 0; i <charecters.size()-2 ; i++) {
                aList.add(orderList(introduceNewLine(aList.get(i))));
            }
        aList.get(aList.size()-1).get(0).code="0";
        aList.get(aList.size()-1).get(1).code="1";
        for (int i = aList.size() -1; i >=0 ; i--) {
            for (int j = 0; j <aList.get(i).size() ; j++) {
                if (i-1 >=0) {
                    for (int k = 0; k<aList.get(i-1).size()  ; k++) {
                        if (aList.get(i).get(j).symbol.equals(aList.get(i-1).get(k).symbol)) {
                            aList.get(i-1).get(k).code=aList.get(i).get(j).code;
                        }
                        else if (aList.get(i).get(j).symbol.contains(aList.get(i-1).get(k).symbol) && founded==false) {
                            founded=true;
                            aList.get(i-1).get(k).code=aList.get(i).get(j).code+"0";
                        }
                        else if (aList.get(i).get(j).symbol.contains(aList.get(i-1).get(k).symbol) && founded==true) {
                            founded=false;
                            aList.get(i-1).get(k).code=aList.get(i).get(j).code+"1";
                        }
                    }
                }
            }
        }
        table=aList.get(0);
    }

}
