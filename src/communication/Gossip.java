package communication;

/**
 * Created with IntelliJ IDEA.
 * User: naveed
 * Date: 11/4/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */

import com.sun.tools.javac.comp.MemberEnter;
import membership.MemberList;
import org.apache.log4j.pattern.IntegerPatternConverter;

import java.awt.*;

public class Gossip {

    private Integer noOfTargets;
    MemberList memberList = null;




    void setNoOfTargets(Integer noOfTargets){
        this.noOfTargets = noOfTargets;
    }

    void getMemberList(MemberList memberList){
        this.memberList = memberList;

    }

     selectRandomTarget(Integer noOfTargets){



    }



}
