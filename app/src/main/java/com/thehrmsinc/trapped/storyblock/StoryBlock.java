package com.thehrmsinc.trapped.storyblock;

import android.content.Context;
import android.util.Log;

import com.thehrmsinc.trapped.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class StoryBlock
{

    private static final String TAG = StoryBlock.class.getSimpleName();
    String blockName="";
    ArrayList<OptionsBlock> options=new ArrayList<>();
    ArrayList<QuestionBlock> questions=new ArrayList<>();




    public void questionsAdd(String question,String nextBlock)
    {
        this.questions.add(new QuestionBlock(question,nextBlock));
    }
    public void optionsAdd(OptionsBlock ob)
    {
        this.options.add(ob);
    }

    public static StoryBlock getNextBlock(String nextBlock,Context context)
    {
        try {
            Log.e(TAG,"Inside parse file");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(context.getAssets().open(context.getString(R.string.story_file)));
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            String query = "/story/block[@name=\"" + nextBlock + "\"]";
            XPathExpression expr = xpath.compile(query);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;


            StoryBlock block = new StoryBlock();
            block.setBlockName(nextBlock);

            nodes = nodes.item(0).getChildNodes();

            Log.e(TAG," node length: "+(String.valueOf( nodes.getLength())));
            long delay = 0;


            for(int i=0;i<nodes.getLength();i++)
            {
                Log.e(TAG," node: "+(nodes.item(i).getNodeName()));
                if(nodes.item(i).getNodeName().startsWith("ques"))
                {
                    block.questionsAdd(nodes.item(i).getTextContent(),nodes.item(i).getAttributes().getNamedItem("next").getTextContent().toString());
                    Log.e(TAG,"Question: "+nodes.item(i).getTextContent());

                }
                else if(nodes.item(i).getNodeName().startsWith("option"))
                {
                    ArrayList<Bot> bot=new ArrayList<Bot>();
                    NodeList n=nodes.item(i).getChildNodes();
                    for(int j=0;j<n.getLength();j++)
                    {
                        if (n.item(j).getNodeName().startsWith("bot")) {
                            delay=0;
                            if (n.item(j).hasAttributes())
                                delay = Long.parseLong(n.item(j).getAttributes().getNamedItem("delay").getNodeValue());
                            bot.add(new Bot(n.item(j).getTextContent(),delay));
                        }
                    }
                    block.getOptions().add(new OptionsBlock(bot));
                }
            }
            return block;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }





    public ArrayList<OptionsBlock> getOptions() {
        return options;
    }

    public ArrayList<QuestionBlock> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        String block="Block name: "+blockName;
        System.out.println(options.size());
        for(OptionsBlock ob:options)
        {
            block=block.concat("\n"+ob.toString());
        }
        for(QuestionBlock ques:questions)
        {
            block+="\nQuestions: "+ques.toString()+" nextBlock: "+ques.nextBlock;
        }
        return block;
    }
}