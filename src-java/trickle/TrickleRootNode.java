package trickle;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

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
