import java.util.ArrayList;

import jdk.jshell.SourceCodeAnalysis.Suggestion;

public class DLB{
    public int SUGGESTIONS = 5; 
    public static int i=0; 
    public Node root; 
    public Node searchNode; 
    public Node searchNodeParent; 
    public StringBuilder prefix= new StringBuilder(); 
    public int wordcout; 

    // Node class 
    private static class Node{
        char value; // the current character at the position
        Node children; // the node at the lower level
        Node sibling; // the node exists at the same level
        boolean isAword; // save at the char inorder to indicate it is a word   
        int frequent;  // the  
        

        public Node(char value, boolean isAword, int frequent){
            this.value = value; 
            this.children = null;
            this.sibling = null; 
            this.isAword = isAword;
            this.frequent = frequent; 
        }
    }

    public void add(String str,int Frequency){
        add(str,0,Frequency); 
    }

    public void add(String value, int index,int Frequency){
        char val; 
        Node currentNode = root; 
        while(index < value.length()){
            val = value.charAt(index); 
            currentNode = addtochildren(val,currentNode,index);
            index ++; 
        }
        if(Frequency != 0){
            currentNode.frequent += Frequency; 
        }
        if(currentNode.isAword != true){
            wordcout ++; 
            currentNode.isAword = true;
        }
        //System.out.println(currentNode.word);
    }

    private Node addtochildren(char val, Node currentNode,int index){
        if(index == 0){
            if(currentNode == null){
                root = new Node(val,false,0); 
                currentNode = root; 
                return currentNode; 
            }else{
                return(addtosibling(currentNode,val)); 
            }
        }
        if(currentNode.children == null){
            currentNode.children = new Node(val,false,0); 
            currentNode = currentNode.children; 
            return currentNode; 
        }else{
            currentNode = addtosibling(currentNode.children, val); 
            return currentNode; 
        }
    }

    private Node addtosibling(Node currentNode, char val){
        if(currentNode.value == val){
            return currentNode; 
        }
        else if(currentNode.sibling == null){
            currentNode.sibling = new Node(val,false,0); 
            currentNode = currentNode.sibling; 
            return currentNode;  
        }else if(currentNode.sibling.value == val){
            currentNode = currentNode.sibling; 
            return currentNode; 
        }
        else{
            currentNode = addtosibling(currentNode.sibling, val);
            return currentNode;      
        }
    }

    // this method is for adding the user input DLB to the user_history.txt file 

    public void addAll(int numberofwords, ArrayList<String> prediction, Node currentNode, StringBuilder builder){
        if(currentNode == null ){
            return; // in this case, we cannnot fing any user input 
        }
        if(currentNode.isAword == true){
            builder.append(currentNode.value); 
            prediction.add(currentNode.frequent+ " " + builder.toString()); 
            builder.deleteCharAt(builder.length()-1); 
        }
        while(currentNode != null){
            builder.append(currentNode.value); 
            addAll(numberofwords, prediction, currentNode.children, builder);
            builder.deleteCharAt(builder.length()-1); 
            currentNode = currentNode.sibling; 
        }
    }

    public ArrayList<String> getAll(){
        ArrayList<String> theWhole = new ArrayList<String>(); 
        addAll(wordcout, theWhole, root,new StringBuilder(""));
        return theWhole; 
    }

    public ArrayList<String> search(char currentChar, int SUGGESTIONS){
        this.SUGGESTIONS = SUGGESTIONS; // the max number can be put in an Array List is 5 
        return search(currentChar); 
    }

    public ArrayList<String> search(char currentChar){
        prefix.append(currentChar); 
        StringBuilder currentString = new StringBuilder(prefix);
        ArrayList<String> predictions = new ArrayList<String>(SUGGESTIONS);  
        Node currentNode = searchNode; 
        String value; 
        while(currentNode != null){
            if(currentNode.value == currentChar){
                break; 
            }
            currentNode = currentNode.sibling; 
        }

        if(currentNode == null){
            searchNode = null; 
            return null; 
        }

        //System.out.println("The length of the prefix is " + prefix.length()); 
        if(currentNode.isAword == true && prefix.length() == 1){
            value = Character.toString(currentNode.value); 
            predictions.add(value); 
        }else if(currentNode.isAword == true){
            value = prefix.toString(); 
            predictions.add(value); 
        }

        searchNodeParent = currentNode; 
        searchNode = currentNode.children; 

        returnWords(searchNode, currentString, predictions); 
        return predictions; 
    }

    public void returnWords(Node current, StringBuilder currentString, ArrayList<String> predictions){
        if(current == null) {
            return; 
        }if(predictions.size() >= SUGGESTIONS){
            return; 
        }
        if(current.isAword == true){
            //System.out.println(current.value);
            currentString.append(current.value); 
            predictions.add(currentString.toString());
            currentString.deleteCharAt(currentString.length()-1); 
        }

        while(current != null){
            currentString.append(current.value); 
            returnWords(current.children, currentString, predictions);
            currentString.deleteCharAt(currentString.length()-1); 
            current = current.sibling; 
        }
    } 

    public void deleteCharAt(String suffix){
        delete(suffix, searchNodeParent); 
    }

    public void delete(String suffix, Node current){
        Node child = current.children; 
        int i=0; 
        char currentChar; 
        while(i<suffix.length()){
            currentChar = suffix.charAt(i); 
            while(child != null){
                if(child.value == currentChar){
                    break; 
                }
                child = child.sibling; 
            }
            current = child; 
            child = child.children; 
        }
        if(child.isAword == true){
            // the case find a word 
            current.children = child.sibling; 
        }
    }

    public void initialSearch(){
        searchNode = root; 
        prefix = new StringBuilder(""); 
    }
    
}