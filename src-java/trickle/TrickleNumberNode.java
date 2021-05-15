package trickle;

import com.oracle.truffle.api.frame.VirtualFrame;

public final class TrickleNumberNode extends TrickleNode {
    private final Number value;

    public TrickleNumberNode(Number value, String description) {
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return (Object)this.value;
    }

    @Override
    public int executeInt(VirtualFrame frame) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
