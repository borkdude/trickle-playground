package trickle;

import com.oracle.truffle.api.frame.VirtualFrame;

public final class TrickleIntNode extends TrickleNode {
    private final int value;

    public TrickleIntNode(int value) {
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return (Object)this.value;
    }

    @Override
    public int executeInt(VirtualFrame frame) {
        return this.value;
    }
}
