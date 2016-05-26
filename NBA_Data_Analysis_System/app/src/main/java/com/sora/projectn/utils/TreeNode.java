package com.sora.projectn.utils;

/**
 * Created by lenovo on 2016/5/2.
 */

import java.util.ArrayList;

import java.util.Stack;

public class TreeNode {

    private TreeNode preNode=null;

    private ArrayList<TreeNode> sucNodes = new ArrayList<TreeNode>();

    private String type="";

    private double value=0;

    private String name="";

    private boolean isAvailable =true;

    public TreeNode getPreNode() {
        return preNode;
    }

    public void setPreNode(TreeNode preNode) {
        this.preNode = preNode;
    }

    public ArrayList<TreeNode> getSucNodes() {
        return sucNodes;
    }

    public void setSucNodes(ArrayList<TreeNode> sucNodes) {
        this.sucNodes = sucNodes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public boolean getIsAvailable(){
        return this.isAvailable;
    }

    public void setIsAvailable(boolean isAvailable){
        this.isAvailable=isAvailable;
    }

    public TreeNode(String name, String type){
        this.name=name;
        this.type=type;
    }

    public TreeNode(String name, String type, boolean isAvailable){
        this.name=name;
        this.type=type;
        this.isAvailable=isAvailable;
    }

    public TreeNode(String name, String type, boolean isAvailable, double value){
        this.name=name;
        this.type=type;
        this.isAvailable=isAvailable;
        this.value=value;
    }

    public TreeNode(){

    }


    public void addSucNode(TreeNode node){
        sucNodes.add(node);
        node.setPreNode(this);
    }

    public void preOrder(TreeNode root){
        System.out.println(root.getName());
        if(root.getPreNode()!=null){
            System.out.println("它的前驱是："+root.getPreNode().getName());
        }
        else{
            System.out.println("他的前驱是：null");
        }

        for(int i=0; i<root.getSucNodes().size();i++){
            preOrder(root.getSucNodes().get(i));
        }
    }

    public TreeNode getTheLastNodeOfPreOrder(TreeNode root){
        TreeNode theLast=null;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(stack.size()>0){
            TreeNode temp = stack.pop();
            if(temp!=null){
                theLast=temp;
                System.out.println("访问："+temp.getName());
                for(int i=temp.getSucNodes().size()-1; i>=0; i--){
                    stack.push(temp.getSucNodes().get(i));
                }
            }
        }

        return theLast;
    }

    public TreeNode getTheFirstAvailableNodeOfPreOrder(TreeNode root){
        TreeNode theFirst=null;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(stack.size()>0){
            TreeNode temp = stack.pop();
            if(temp!=null){
                if(temp.getIsAvailable()){
                    theFirst=temp;
                    return theFirst;
                }
                for(int i=temp.getSucNodes().size()-1; i>=0; i--){
                    stack.push(temp.getSucNodes().get(i));
                }
            }
        }

        return theFirst;
    }



    //删除后继是ε的节点
    public TreeNode deleteEpsilonBranches(TreeNode root){

        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(stack.size()>0){
            TreeNode temp = stack.pop();
            if(temp!=null){
                System.out.println("访问："+temp.getName());
                if(temp.getSucNodes().size()>0){
                    if(temp.getSucNodes().get(0).getName().equals("ε")){
                        temp.getPreNode().getSucNodes().remove(temp);
                        temp.setPreNode(null);
                        continue;
                    }
                }
                for(int i=temp.getSucNodes().size()-1; i>=0; i--){
                    stack.push(temp.getSucNodes().get(i));
                }
            }
        }
        return root;
    }

    public TreeNode convertTree(TreeNode root){

        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(stack.size()>0){
            TreeNode temp = stack.pop();
            if(temp!=null){
                System.out.println("访问："+temp.getName());
                if(temp.getSucNodes().size()>0){
                    if(temp.getSucNodes().get(0).getName().equals("+")||
                            temp.getSucNodes().get(0).getName().equals("-")||
                            temp.getSucNodes().get(0).getName().equals("*")||
                            temp.getSucNodes().get(0).getName().equals("/")){
                        temp.getSucNodes().get(0).setPreNode(temp.getPreNode());
                        temp.getPreNode().getSucNodes().add(1,temp.getSucNodes().get(0));
                        temp.getSucNodes().remove(0);
                    }
                }
                for(int i=temp.getSucNodes().size()-1; i>=0; i--){
                    stack.push(temp.getSucNodes().get(i));
                }
            }
        }
        return root;

    }

    public TreeNode deleteBrackets(TreeNode root){

        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(stack.size()>0){
            TreeNode temp = stack.pop();
            if(temp!=null){
                System.out.println("访问："+temp.getName());
                if(temp.getSucNodes().size()>0){
                    if(temp.getSucNodes().get(0).getName().equals("(")){
                        temp.getSucNodes().get(0).setPreNode(null);
                        temp.getSucNodes().remove(0);
                        temp.getSucNodes().get(1).setPreNode(null);
                        temp.getSucNodes().remove(1);
                    }
                }
                for(int i=temp.getSucNodes().size()-1; i>=0; i--){
                    stack.push(temp.getSucNodes().get(i));
                }
            }
        }
        return root;
    }


}

