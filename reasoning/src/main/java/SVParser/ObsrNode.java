package SVParser;

import java.util.ArrayList;
import java.util.List;

public class ObsrNode {
    List<ObsrNode> preCondition = null;
    List<ObsrNode> consequence = null;

    DomainNode domain = null;
    PrimitiveNode primitive = null;

    Boolean orFlag = false;

    String stage = "N/A";

    public ObsrNode(){
        domain = new DomainNode();
        primitive = new PrimitiveNode();
        preCondition = new ArrayList<ObsrNode>();
        consequence = new ArrayList<ObsrNode>();
    }

    public Boolean isEndNode(){
        if(this.consequence == null || this.consequence.size() == 0)
            return (this.primitive.op_type == 3);
        else
            return (this.consequence.get(0).isEndNode());
    }

    public Boolean isPredecessorOf(ObsrNode target, Boolean rec){
        Boolean result = false;

        if(this.stage.equals("final") || target.stage.equals("toe"))
            return false;

        // if it's a recursive call
        if(rec){
            if(target.preCondition == null || target.preCondition.size() == 0){
                if(consequence == null || consequence.size() == 0){
                    if(this.stage.equals("final"))
                        result = false;
                    else
                        result = this.domain.isContained(target.domain) & this.primitive.isMatched(target.primitive);
                } else {
                    for (int i = 0; i < this.consequence.size(); i++) {
                        result = this.consequence.get(i).isPredecessorOf(target, true);

                        if (result)
                            break;
                    }
                }
            } else {
                for(int i = 0; i < target.preCondition.size(); i++){
                    result = this.isPredecessorOf(target.preCondition.get(i), true);
                }
            }
        } else {
            // if it's called as the initial call.

            // If the target has no precondition, then it must be a successor of current node.
            if(target.preCondition == null || target.preCondition.size() == 0){
                if(this.stage.equals("final"))
                    result = false;
                else
                    result = true;
            }

            else {
                result = this.isPredecessorOf(target, true);
            }
        }

        return result;
    }

    @Override
    public String toString(){
//        return String.format("\n[ObsrNode]\n" +
//                        "    preCondition #: %d,\n" +
//                        "    consequence #: %d,\n" +
//                        "    domain: \n" +
//                        "    %s\n" +
//                        "    primitive: \n" +
//                        "    %s\n" +
//                        "    stage: %s.",
//                preCondition.size(), consequence.size(), domain.toString(), primitive.toString(), stage);
        return String.format("[ObsrNode]" +
                            //"   precondition #: %d, " +
                            "   if %s::%s %d -> %s" +
                            //"   consequence #: %d, "+
                            "   then %s::%s %d -> %s",
                //preCondition.size(),
                preCondition.get(0).domain.domain_type, preCondition.get(0).domain.pid.getPathString(), preCondition.get(0).primitive.op_type, "var",
                //consequence.size(),
                consequence.get(0).domain.domain_type, consequence.get(0).domain.pid.getPathString(), consequence.get(0).primitive.op_type, "var");
    }
}
