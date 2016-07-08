package com.thehrmsinc.trapped.storyblock;

/**
 * Created by Acer on 6/7/2016.
 */

public class QuestionBlock {
    String question;
    String nextBlock;

    public QuestionBlock( String question,String nextBlock)
    {
        this.question=question;
        this.nextBlock=nextBlock;
    }
    @Override
    public String toString() {
        return "Question: "+question+"\nNextBlock: "+nextBlock;
    }

    public String getNextBlock() {        return nextBlock;    }

    public String getQuestion() {        return question;    }
}
