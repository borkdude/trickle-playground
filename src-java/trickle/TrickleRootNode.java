package trickle;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;

@NodeInfo(language = "trickle", description = "The root of all Trickle execution trees")
public final class TrickleRootNode extends RootNode {
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private TrickleNode exprNode;

    public TrickleRootNode(TrickleNode exprNode) {
        super(null);

        this.exprNode = exprNode;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return this.exprNode.execute(frame);
    }
}
