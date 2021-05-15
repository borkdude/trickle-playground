package trickle;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public abstract class TrickleNode extends Node {

    public TrickleNode() {
    }
    
    public abstract Object execute(VirtualFrame frame);

    public abstract int executeInt(VirtualFrame frame);
}

